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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import iso.std.iso._20022.tech.xsd.pain_008_002_02.ActiveOrHistoricCurrencyCodeEUR;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.ChargeBearerTypeSEPACode;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.CustomerDirectDebitInitiationV02;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.DirectDebitTransactionInformationSDD;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.Document;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.GroupHeaderSDD;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.IdentificationSchemeNameSEPA;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.LocalInstrumentSEPACode;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.ObjectFactory;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.PaymentInstructionInformationSDD;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.PaymentMethod2Code;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.SequenceType1Code;
import iso.std.iso._20022.tech.xsd.pain_008_002_02.ServiceLevelSEPACode;

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

public class SEPAWriterPain_008_002_02 implements SEPAWriter {

	ObjectFactory objectFactory;
	private Log log;
	
	public SEPAWriterPain_008_002_02(){
		
		log = LogFactory.getLog(SEPAWriterPain_008_002_02.class);
		
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
		GroupHeaderSDD grpHdr = objectFactory.createGroupHeaderSDD();
		grpHdr.setMsgId(debitMessage.getMessageId());
		grpHdr.setCreDtTm(debitMessage.getCreateDate());
		grpHdr.setNbOfTxs(String.valueOf(debitMessage.getNbrOfTransaction()));
		grpHdr.setCtrlSum(debitMessage.getCtlSum());
		
		grpHdr.setInitgPty(objectFactory.createPartyIdentificationSEPA1());
		grpHdr.getInitgPty().setNm(debitMessage.getName());
		
		cstmrCdtTrfInitn.setGrpHdr(grpHdr);
		
		// Pro Kreditor als DebitTransaction wird PaymentInstructionInformationSDD erstellt
		// PaymentInstructionInformationSDD entspricht DebitTransaction
		for (SEPATransaction trns : debitMessage.getTransactionsList()) {
			
			DebitTransaction dtrns = null;
			if(trns instanceof DebitTransaction){
				dtrns = (DebitTransaction) trns;
			}
			
			PaymentInstructionInformationSDD pmts = objectFactory.createPaymentInstructionInformationSDD();
			
			// Debitorendaten
			pmts.setPmtInfId(dtrns.getPaymentInformation());
			pmts.setPmtMtd(PaymentMethod2Code.DD);
			pmts.setNbOfTxs(String.valueOf(dtrns.getNbrOfTransaction()));
			pmts.setCtrlSum(dtrns.getCtlSum());
			
			pmts.setPmtTpInf(objectFactory.createPaymentTypeInformationSDD());
			pmts.getPmtTpInf().setSvcLvl(objectFactory.createServiceLevelSEPA());
			pmts.getPmtTpInf().getSvcLvl().setCd(ServiceLevelSEPACode.SEPA);
			pmts.getPmtTpInf().setLclInstrm(objectFactory.createLocalInstrumentSEPA());
			// TODO: In der Version gibt es nicht COR1 -> Ist OK in CORE zu wandeln
			if(dtrns.getType().equals(Type.B2B)){
				pmts.getPmtTpInf().getLclInstrm().setCd(LocalInstrumentSEPACode.B_2_B);
			} else {
				pmts.getPmtTpInf().getLclInstrm().setCd(LocalInstrumentSEPACode.CORE);
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
			
			pmts.setCdtr(objectFactory.createPartyIdentificationSEPA5());
			pmts.getCdtr().setNm(dtrns.getName());
			
			pmts.setCdtrAcct(objectFactory.createCashAccountSEPA1());
			pmts.getCdtrAcct().setId(objectFactory.createAccountIdentificationSEPA());
			pmts.getCdtrAcct().getId().setIBAN(dtrns.getIBAN());
			
			pmts.setCdtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentificationSEPA1());
			pmts.getCdtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentificationSEPA1());
			pmts.getCdtrAgt().getFinInstnId().setBIC(dtrns.getBIC());
			
			pmts.setChrgBr(ChargeBearerTypeSEPACode.SLEV);
			
			// Gläubiger-Id
			pmts.setCdtrSchmeId(objectFactory.createPartyIdentificationSEPA3());
			pmts.getCdtrSchmeId().setId(objectFactory.createPartySEPA2());
			pmts.getCdtrSchmeId().getId().setPrvtId(objectFactory.createPersonIdentificationSEPA2());
			pmts.getCdtrSchmeId().getId().getPrvtId().setOthr(objectFactory.createRestrictedPersonIdentificationSEPA());
			pmts.getCdtrSchmeId().getId().getPrvtId().getOthr().setId(dtrns.getCreditorId());
			pmts.getCdtrSchmeId().getId().getPrvtId().getOthr().setSchmeNm(objectFactory.createRestrictedPersonIdentificationSchemeNameSEPA());
			pmts.getCdtrSchmeId().getId().getPrvtId().getOthr().getSchmeNm().setPrtry(IdentificationSchemeNameSEPA.SEPA);
			
			// DrctDbtTrfTxInf
			// Hier werden die einzelne Transaktionen pro Kreditor verarbeitet
			for (DebitTransactionItem ti : dtrns.getDebitTransactionList()) {
				
				DirectDebitTransactionInformationSDD dtts = objectFactory.createDirectDebitTransactionInformationSDD();

				dtts.setPmtId(objectFactory.createPaymentIdentificationSEPA());
				dtts.getPmtId().setEndToEndId(ti.getEndToEndId());
				
				dtts.setInstdAmt(objectFactory.createActiveOrHistoricCurrencyAndAmountSEPA());
				dtts.getInstdAmt().setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
				dtts.getInstdAmt().setValue(ti.getAmount());
				
				// Mandat
				dtts.setDrctDbtTx(objectFactory.createDirectDebitTransactionSDD());
				dtts.getDrctDbtTx().setMndtRltdInf(objectFactory.createMandateRelatedInformationSDD());
				dtts.getDrctDbtTx().getMndtRltdInf().setMndtId(ti.getMandate().getReferenz());
				dtts.getDrctDbtTx().getMndtRltdInf().setDtOfSgntr(SEPAUtility.convertYearOnly(ti.getMandate().getSignaturDate()));
				dtts.getDrctDbtTx().getMndtRltdInf().setAmdmntInd(ti.getMandate().isAmendmentIndicator());
				
				dtts.setDbtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentificationSEPA1());
				dtts.getDbtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentificationSEPA1());
				dtts.getDbtrAgt().getFinInstnId().setBIC(ti.getBIC());
				
				dtts.setDbtr(objectFactory.createPartyIdentificationSEPA2());
				dtts.getDbtr().setNm(ti.getName());
				
				dtts.setDbtrAcct(objectFactory.createCashAccountSEPA2());
				dtts.getDbtrAcct().setId(objectFactory.createAccountIdentificationSEPA());
				dtts.getDbtrAcct().getId().setIBAN(ti.getIBAN());
				
				dtts.setRmtInf(objectFactory.createRemittanceInformationSEPA1Choice());
				dtts.getRmtInf().setUstrd(ti.getRemitanceInformation());
				
				if(ti.getPurposeCode() != null){
					dtts.setPurp(objectFactory.createPurposeSEPA());
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
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:iso:std:iso:20022:tech:xsd:pain.008.002.02 pain.008.002.02.xsd");
			
			m.marshal(document, os);
			
			return os;
			
			
		} catch (JAXBException e) {
			log.error(e);
		}
		return null;
	}
	
	
}
