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

import iso.std.iso._20022.tech.xsd.pain_001_002_03.CreditTransferTransactionInformationSCT;
import iso.std.iso._20022.tech.xsd.pain_001_002_03.Document;
import iso.std.iso._20022.tech.xsd.pain_001_002_03.PaymentInstructionInformationSCT;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.CreditTransaction;
import ws.michalski.sepa.pain.CreditTransactionItem;
import ws.michalski.sepa.pain.Currency;
import ws.michalski.sepa.pain.Purpose;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.SEPAReader;

public class SEPAReaderPain_001_002_03 implements SEPAReader {

	Document doc;
	
	private Log log;

	public SEPAReaderPain_001_002_03(){
		super();
		log = LogFactory.getLog(SEPAReaderPain_001_002_03.class);
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
			
			
			sm.setMessageId(doc.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
			sm.setName(doc.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getNm());
			sm.setCreateDate(doc.getCstmrCdtTrfInitn().getGrpHdr().getCreDtTm());
			sm.setCtlSum(doc.getCstmrCdtTrfInitn().getGrpHdr().getCtrlSum());
			sm.setNbrOfTransaction(Integer.parseInt(doc.getCstmrCdtTrfInitn().getGrpHdr().getNbOfTxs()));
			
			// alle PmtInf holen.
			List<PaymentInstructionInformationSCT> crdList = doc.getCstmrCdtTrfInitn().getPmtInf();
			for (PaymentInstructionInformationSCT payment : crdList) {
				
				CreditTransaction ct = new CreditTransaction();
				
				ct.setPaymentInformation(payment.getPmtInfId());
				if(payment.getNbOfTxs() != null){
					ct.setNbrOfTransaction(Integer.parseInt(payment.getNbOfTxs()));
				} else {
					ct.setNbrOfTransaction(0);
				}
				ct.setCtlSum(payment.getCtrlSum());
				ct.setName(payment.getDbtr().getNm());
				ct.setExecutionDate(payment.getReqdExctnDt().toGregorianCalendar());
				ct.setIBAN(payment.getDbtrAcct().getId().getIBAN());
				ct.setBIC(payment.getDbtrAgt().getFinInstnId().getBIC());

				
				// Alle Transaktionen
				List<CreditTransferTransactionInformationSCT> ctil = payment.getCdtTrfTxInf();
				for (CreditTransferTransactionInformationSCT cti : ctil) {
					
					CreditTransactionItem ti = new CreditTransactionItem();
					ti.setEndToEndId(cti.getPmtId().getEndToEndId());
					ti.setAmount(cti.getAmt().getInstdAmt().getValue());
					ti.setCurrency(Currency.EUR);
					ti.setBIC(cti.getCdtrAgt().getFinInstnId().getBIC());
					ti.setName(cti.getCdtr().getNm());
					ti.setIBAN(cti.getCdtrAcct().getId().getIBAN());
					if(cti.getRmtInf() != null){
						ti.setRemitanceInformation(cti.getRmtInf().getUstrd());
					}
					if(cti.getPurp() != null){
						ti.setPurposeCode(Purpose.getPurposeCode(cti.getPurp().getCd()));
					}
					
					ct.addCreditTransationItem(ti);
					
				}
				
				sm.addTransaction(ct);
			}
			
		} catch (JAXBException e) {
			log.error(e);
		}
		
		
		return sm;
	}

	/**
	 * Liefert root als Objekt zurück
	 */
	@Override
	public Object getRootObject() {
		return doc;
	}



}
