/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at 

     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ws.michalski.sepa.pain.readers;

import iso.std.iso._20022.tech.xsd.pain_008_003_02.DirectDebitTransactionInformationSDD;
import iso.std.iso._20022.tech.xsd.pain_008_003_02.Document;
import iso.std.iso._20022.tech.xsd.pain_008_003_02.PaymentInstructionInformationSDD;
import iso.std.iso._20022.tech.xsd.pain_008_003_02.SequenceType1Code;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.Currency;
import ws.michalski.sepa.pain.DebitTransaction;
import ws.michalski.sepa.pain.DebitTransactionItem;
import ws.michalski.sepa.pain.Mandate;
import ws.michalski.sepa.pain.Purpose;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.DebitTransaction.Type;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.SEPAReader;

public class SEPAReaderPain_008_003_02 implements SEPAReader {

	
	
	private Document doc;
	private Log log;

	public SEPAReaderPain_008_003_02(){
		super();
		log = LogFactory.getLog(SEPAReaderPain_008_003_02.class);
	}
	
	@Override
	public SEPAMessage parse(InputStream is) throws SEPAException {
		
		// Alle Elemente einer SEPA Nachrit erzeugen 
		SEPAMessage sm = new SEPAMessage();
		
		try {
			// Unmarschal 
			JAXBContext jc = JAXBContext.newInstance(Document.class);
			Unmarshaller u = jc.createUnmarshaller();
			doc = (Document) u.unmarshal(is);
			
			
			sm.setMessageId(doc.getCstmrDrctDbtInitn().getGrpHdr().getMsgId());
			sm.setName(doc.getCstmrDrctDbtInitn().getGrpHdr().getInitgPty().getNm());
			sm.setCreateDate(doc.getCstmrDrctDbtInitn().getGrpHdr().getCreDtTm());
			sm.setCtlSum(doc.getCstmrDrctDbtInitn().getGrpHdr().getCtrlSum());
			sm.setNbrOfTransaction(Integer.parseInt(doc.getCstmrDrctDbtInitn().getGrpHdr().getNbOfTxs()));
			
			// alle PmtInf holen.
			List<PaymentInstructionInformationSDD> debList = doc.getCstmrDrctDbtInitn().getPmtInf();
			for (PaymentInstructionInformationSDD payment : debList) {
				
				DebitTransaction dt = new DebitTransaction();
				
				dt.setPaymentInformation(payment.getPmtInfId());
				if( payment.getNbOfTxs() != null){
					dt.setNbrOfTransaction(Integer.parseInt(payment.getNbOfTxs()));
				} else {
					dt.setNbrOfTransaction(0);
				}
				dt.setCtlSum(payment.getCtrlSum());
				dt.setName(payment.getCdtr().getNm());
				dt.setExecutionDate(payment.getReqdColltnDt().toGregorianCalendar());
				dt.setIBAN(payment.getCdtrAcct().getId().getIBAN());
				dt.setBIC(payment.getCdtrAgt().getFinInstnId().getBIC());
				
				// CrdSchmeId kann auch in DrctDbtTxINf stehen, obwohl das nicht toll ist.
				// Ist auf der Stelle NULL, muss noch mal in DrctDbtTxInf nachgeschaut werden
				// Der Wert wird es allerdings nur aus dem ersten Satz genommen.
				if(! (payment.getCdtrSchmeId() == null)){
					dt.setCreditorId(payment.getCdtrSchmeId().getId().getPrvtId().getOthr().getId());
				}
				
				// Typ wählen
				String typ = payment.getPmtTpInf().getSvcLvl().getCd();
				if(typ.equals("CORE")){
					dt.setType(Type.CORE);
				} else if(typ.equals("COR1")){
					dt.setType(Type.COR1);
				} else if(typ.equals("B2B")){
					dt.setType(Type.B2B);
				}
				
				// Sequenz wählen
				SequenceType1Code seq = payment.getPmtTpInf().getSeqTp();
				switch (seq) {
				case OOFF:
					dt.setSeqenceOOFF();
					break;
				case FNAL:
					dt.setSeqenceFNAL();
					break;
				case FRST:
					dt.setSeqenceFRST();
					break;
				case RCUR:
					dt.setSeqenceRCUR();
					break;
				}
				
				// Alle Transaktionen
				List<DirectDebitTransactionInformationSDD> dtil = payment.getDrctDbtTxInf();
				for (DirectDebitTransactionInformationSDD dti : dtil) {
					
					DebitTransactionItem ti = new DebitTransactionItem();
					ti.setEndToEndId(dti.getPmtId().getEndToEndId());
					ti.setAmount(dti.getInstdAmt().getValue());
					ti.setCurrency(Currency.EUR);
					ti.setBIC(dti.getDbtrAgt().getFinInstnId().getBIC());
					ti.setName(dti.getDbtr().getNm());
					ti.setIBAN(dti.getDbtrAcct().getId().getIBAN());
					if(dti.getRmtInf() != null){
						ti.setRemitanceInformation(dti.getRmtInf().getUstrd());
					}
					if(dti.getPurp() != null){
						ti.setPurposeCode(Purpose.getPurposeCode(dti.getPurp().getCd()));
					}
					
					// Mandat
					Mandate man = new Mandate(dti.getDrctDbtTx().getMndtRltdInf().getMndtId(), 
							dti.getDrctDbtTx().getMndtRltdInf().getDtOfSgntr().toGregorianCalendar()); 
					ti.setMandate(man);
					
					// Prüfen, ob CdtrSchmeId in dem DrctDbtTxInf vorhanden ist und in der 
					// DebitTransaction noch nicht belegt ist
					if(dt.getCreditorId() == null){
						if(dti.getDrctDbtTx().getCdtrSchmeId() != null){
							dt.setCreditorId(dti.getDrctDbtTx().getCdtrSchmeId().getId().getPrvtId().getOthr().getId());
						}
					}
					
					
					dt.addDebitTransationItem(ti);
					
				}
				
				sm.addTransaction(dt);
			}
			
		} catch (JAXBException e) {
			log.error(e);
		}
		
		
		return sm;
	}

	@Override
	public Object getRootObject() {
		return doc;
	}

}
