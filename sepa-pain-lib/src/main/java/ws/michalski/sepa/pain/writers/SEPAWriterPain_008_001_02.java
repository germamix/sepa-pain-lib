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
package ws.michalski.sepa.pain.writers;

import iso.std.iso._20022.tech.xsd.pain_008_001_02.ChargeBearerType1Code;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.CustomerDirectDebitInitiationV02;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.Document;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.GenericPersonIdentification1;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.GroupHeader39;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.ObjectFactory;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.PaymentInstructionInformation4;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.PaymentMethod2Code;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.SequenceType1Code;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.DebitTransaction;
import ws.michalski.sepa.pain.DebitTransactionItem;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.SEPATransaction;
import ws.michalski.sepa.pain.SEPAUtility;
import ws.michalski.sepa.pain.DebitTransaction.SeqType;
import ws.michalski.sepa.pain.DebitTransaction.Type;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.SEPAWriter;

public class SEPAWriterPain_008_001_02 implements SEPAWriter {

	ObjectFactory objectFactory;
	private Log log;
	
	public SEPAWriterPain_008_001_02(){
		log = LogFactory.getLog(SEPAWriterPain_008_001_02.class);
		
		objectFactory = new ObjectFactory();
	}
	
	/* (non-Javadoc)
	 * @see de.efdis.sepa.SEPAWriterIntf#export(de.efdis.sepa.CreditMessage)
	 */
	@Override
	public OutputStream export(SEPAMessage debitMessage) throws SEPAException{
		
		// Übergabenachricht wird geprüft
		debitMessage.validate();
		
		// Document erstellen
		Document document = objectFactory.createDocument();

		// CstmrCdtTrfInitn erstellen
		CustomerDirectDebitInitiationV02 cstmrCdtTrfInitn = objectFactory.createCustomerDirectDebitInitiationV02();
		document.setCstmrDrctDbtInitn(cstmrCdtTrfInitn);

		// GrpHdr erstellen
		GroupHeader39 grpHdr = objectFactory.createGroupHeader39();
		grpHdr.setMsgId(debitMessage.getMessageId());
		grpHdr.setCreDtTm(debitMessage.getCreateDate());
		grpHdr.setNbOfTxs(String.valueOf(debitMessage.getNbrOfTransaction()));
		grpHdr.setCtrlSum(debitMessage.getCtlSum());
		
		grpHdr.setInitgPty(objectFactory.createPartyIdentification32());
		grpHdr.getInitgPty().setNm(debitMessage.getName());
		
		cstmrCdtTrfInitn.setGrpHdr(grpHdr);
		
		// Pro Kreditor als DebitTransaction wird PaymentInstructionInformationSDD erstellt
		// PaymentInstructionInformationSDD entspricht DebitTransaction
		for (SEPATransaction trns : debitMessage.getTransactionsList()) {
			
			DebitTransaction dtrns = null;
			if(trns instanceof DebitTransaction){
				dtrns = (DebitTransaction) trns;
			}
			
			PaymentInstructionInformation4 pmts = objectFactory.createPaymentInstructionInformation4();
			
			// Debitorendaten
			pmts.setPmtInfId(dtrns.getPaymentInformation());
			pmts.setPmtMtd(PaymentMethod2Code.DD);
			pmts.setNbOfTxs(String.valueOf(dtrns.getNbrOfTransaction()));
			pmts.setCtrlSum(dtrns.getCtlSum());
			
			pmts.setPmtTpInf(objectFactory.createPaymentTypeInformation20());
			pmts.getPmtTpInf().setSvcLvl(objectFactory.createServiceLevel8Choice());
			pmts.getPmtTpInf().getSvcLvl().setCd("SEPA");
			pmts.getPmtTpInf().setLclInstrm(objectFactory.createLocalInstrument2Choice());
			// TODO: In der Version gibt es nicht COR1 -> Ist OK in CORE zu wandeln
			if(dtrns.getType().equals(Type.B2B)){
				pmts.getPmtTpInf().getLclInstrm().setCd("B2B");
			} else {
				pmts.getPmtTpInf().getLclInstrm().setCd("CORE");
			}
			
			// Sequenz-Typ
			SeqType sq = dtrns.getSeqType();
			switch (sq) {
			case FNAL:
				pmts.getPmtTpInf().setSeqTp(SequenceType1Code.FNAL);
				break;
			case FRST:
				pmts.getPmtTpInf().setSeqTp(SequenceType1Code.FRST);
				break;
			case OOFF:
				pmts.getPmtTpInf().setSeqTp(SequenceType1Code.OOFF);
				break;
			case RCUR:
				pmts.getPmtTpInf().setSeqTp(SequenceType1Code.RCUR);
				break;
			}
			// Ausführungsdatum von Gregorian auf XMLGregorian
			pmts.setReqdColltnDt(SEPAUtility.convertYearOnly(dtrns.getExecutionDate()));
			
			pmts.setCdtr(objectFactory.createPartyIdentification32());
			pmts.getCdtr().setNm(dtrns.getName());
			
			pmts.setCdtrAcct(objectFactory.createCashAccount16());
			pmts.getCdtrAcct().setId(objectFactory.createAccountIdentification4Choice());
			pmts.getCdtrAcct().getId().setIBAN(dtrns.getIBAN());
			
			pmts.setCdtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentification4());
			pmts.getCdtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentification7());
			pmts.getCdtrAgt().getFinInstnId().setBIC(dtrns.getBIC());
			
			pmts.setChrgBr(ChargeBearerType1Code.SLEV);
			// Gläubiger-Id
			pmts.setCdtrSchmeId(objectFactory.createPartyIdentification32());
			pmts.getCdtrSchmeId().setId(objectFactory.createParty6Choice());
			pmts.getCdtrSchmeId().getId().setPrvtId(objectFactory.createPersonIdentification5());
			GenericPersonIdentification1 gpi = new GenericPersonIdentification1();
			gpi.setId(dtrns.getCreditorId());
			gpi.setSchmeNm(objectFactory.createPersonIdentificationSchemeName1Choice());
			gpi.getSchmeNm().setPrtry("SEPA");
			pmts.getCdtrSchmeId().getId().getPrvtId().getOthr().add(gpi);
			
			// DrctDbtTrfTxInf
			// Hier werden die einzelne Transaktionen pro Kreditor verarbeitet
			for (DebitTransactionItem ti : dtrns.getDebitTransactionList()) {
				
				DirectDebitTransactionInformation9 dtts = objectFactory.createDirectDebitTransactionInformation9();

				dtts.setPmtId(objectFactory.createPaymentIdentification1());
				dtts.getPmtId().setEndToEndId(ti.getEndToEndId());
				
				dtts.setInstdAmt(objectFactory.createActiveOrHistoricCurrencyAndAmount());
				dtts.getInstdAmt().setCcy("EUR");
				dtts.getInstdAmt().setValue(ti.getAmount());
				
				// Mandat
				dtts.setDrctDbtTx(objectFactory.createDirectDebitTransaction6());
				dtts.getDrctDbtTx().setMndtRltdInf(objectFactory.createMandateRelatedInformation6());
				dtts.getDrctDbtTx().getMndtRltdInf().setMndtId(ti.getMandate().getReferenz());
				dtts.getDrctDbtTx().getMndtRltdInf().setDtOfSgntr(SEPAUtility.convertYearOnly(ti.getMandate().getSignaturDate()));
				dtts.getDrctDbtTx().getMndtRltdInf().setAmdmntInd(ti.getMandate().isAmendmentIndicator());
				
				dtts.setDbtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentification4());
				dtts.getDbtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentification7());
				dtts.getDbtrAgt().getFinInstnId().setBIC(ti.getBIC());
				
				dtts.setDbtr(objectFactory.createPartyIdentification32());
				dtts.getDbtr().setNm(ti.getName());
				
				dtts.setDbtrAcct(objectFactory.createCashAccount16());
				dtts.getDbtrAcct().setId(objectFactory.createAccountIdentification4Choice());
				dtts.getDbtrAcct().getId().setIBAN(ti.getIBAN());
				
				dtts.setRmtInf(objectFactory.createRemittanceInformation5());
				dtts.getRmtInf().getUstrd().add(ti.getRemitanceInformation());
				
				if(ti.getPurposeCode() != null){
					dtts.setPurp(objectFactory.createPurpose2Choice());
					dtts.getPurp().setCd(ti.getPurposeCode().toString());
				}
				
				// Einzeltransaktion einfügen
				pmts.getDrctDbtTxInf().add(dtts);
			}
			
			
			// Debitor Einfügen
			cstmrCdtTrfInitn.getPmtInf().add(pmts);
			
		}
		
		
		// Exportieren
		try {
			
			OutputStream os = new ByteArrayOutputStream();
			
			JAXBContext jc = JAXBContext.newInstance(Document.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02 pain.008.001.02.xsd");
			
			m.marshal(document, os);
			
			return os;
			
			
		} catch (JAXBException e) {
			log.error(e);
		}
		return null;
	}
	
	
}
