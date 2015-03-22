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

import iso.std.iso._20022.tech.xsd.pain_001_003_03.ActiveOrHistoricCurrencyCodeEUR;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.ChargeBearerTypeSEPACode;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.CreditTransferTransactionInformationSCT;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.CustomerCreditTransferInitiationV03;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.Document;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.GroupHeaderSCT;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.ObjectFactory;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.PaymentInstructionInformationSCT;
import iso.std.iso._20022.tech.xsd.pain_001_003_03.PaymentMethodSCTCode;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.CreditTransaction;
import ws.michalski.sepa.pain.CreditTransactionItem;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.SEPATransaction;
import ws.michalski.sepa.pain.SEPAUtility;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.SEPAWriter;

public class SEPAWriterPain_001_003_03 implements SEPAWriter {

	ObjectFactory objectFactory;
	private Log log;
	
	public SEPAWriterPain_001_003_03(){
		
		log = LogFactory.getLog(SEPAWriterPain_001_003_03.class);
		
		objectFactory = new ObjectFactory();
	}
	
	/* (non-Javadoc)
	 * @see de.efdis.sepa.SEPAWriterIntf#export(de.efdis.sepa.CreditMessage)
	 */
	@Override
	public OutputStream export(SEPAMessage creditMessage) throws SEPAException{
		
		// Übergabenachricht wird geprüft
		creditMessage.validate();
		
		// Document erstellen
		Document document = objectFactory.createDocument();

		// CstmrCdtTrfInitn erstellen
		CustomerCreditTransferInitiationV03 cstmrCdtTrfInitn = objectFactory.createCustomerCreditTransferInitiationV03();
		document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);

		// GrpHdr erstellen
		GroupHeaderSCT grpHdr = objectFactory.createGroupHeaderSCT();
		grpHdr.setMsgId(creditMessage.getMessageId());
		grpHdr.setCreDtTm(creditMessage.getCreateDate());
		grpHdr.setNbOfTxs(String.valueOf(creditMessage.getNbrOfTransaction()));
		grpHdr.setCtrlSum(creditMessage.getCtlSum());
		
		grpHdr.setInitgPty(objectFactory.createPartyIdentificationSEPA1());
		grpHdr.getInitgPty().setNm(creditMessage.getName());
		
		cstmrCdtTrfInitn.setGrpHdr(grpHdr);
		
		// Pro Debitor als CreditTransaction wird PaymentInstructionInformationSCT erstellt
		// PaymentInstructionInformationSCT entspricht CreditTransaction
		for (SEPATransaction trns : creditMessage.getTransactionsList()) {
			
			CreditTransaction ctrns = null;
			if(trns instanceof CreditTransaction){
				ctrns = (CreditTransaction) trns;
			}
			
			PaymentInstructionInformationSCT pmts = objectFactory.createPaymentInstructionInformationSCT();
			
			// Debitorendaten
			pmts.setPmtInfId(ctrns.getPaymentInformation());
			pmts.setPmtMtd(PaymentMethodSCTCode.TRF);
			pmts.setNbOfTxs(String.valueOf(ctrns.getNbrOfTransaction()));
			pmts.setCtrlSum(ctrns.getCtlSum());
			
			pmts.setPmtTpInf(objectFactory.createPaymentTypeInformationSCT1());
			pmts.getPmtTpInf().setSvcLvl(objectFactory.createServiceLevelSEPA());
			if(ctrns.isUrgendServLev()){
				pmts.getPmtTpInf().getSvcLvl().setCd("URGP");				
			} else {
				pmts.getPmtTpInf().getSvcLvl().setCd("SEPA"); 
			}
			
			// Ausführungsdatum von Gregorian auf XMLGregorian
			pmts.setReqdExctnDt(SEPAUtility.convertYearOnly(ctrns.getExecutionDate()));
			
			pmts.setDbtr(objectFactory.createPartyIdentificationSEPA2());
			pmts.getDbtr().setNm(ctrns.getName());
			
			pmts.setDbtrAcct(objectFactory.createCashAccountSEPA1());
			pmts.getDbtrAcct().setId(objectFactory.createAccountIdentificationSEPA());
			pmts.getDbtrAcct().getId().setIBAN(ctrns.getIBAN());
			
			pmts.setDbtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentificationSEPA3());
			pmts.getDbtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentificationSEPA3());
			pmts.getDbtrAgt().getFinInstnId().setBIC(ctrns.getBIC());
			
			pmts.setChrgBr(ChargeBearerTypeSEPACode.SLEV);
			
			// CdtTrfTxInf
			// Hier werden die einzelne Transaktionen pro Debitor verarbeitet
			for (CreditTransactionItem ti : ctrns.getCreditTransactionList()) {
				
				CreditTransferTransactionInformationSCT ctts = objectFactory.createCreditTransferTransactionInformationSCT();

				ctts.setPmtId(objectFactory.createPaymentIdentificationSEPA());
				ctts.getPmtId().setEndToEndId(ti.getEndToEndId());
				
				ctts.setAmt(objectFactory.createAmountTypeSEPA());
				ctts.getAmt().setInstdAmt(objectFactory.createActiveOrHistoricCurrencyAndAmountSEPA());
				ctts.getAmt().getInstdAmt().setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
				ctts.getAmt().getInstdAmt().setValue(ti.getAmount());
				
				ctts.setCdtrAgt(objectFactory.createBranchAndFinancialInstitutionIdentificationSEPA1());
				ctts.getCdtrAgt().setFinInstnId(objectFactory.createFinancialInstitutionIdentificationSEPA1());
				ctts.getCdtrAgt().getFinInstnId().setBIC(ti.getBIC());
				
				ctts.setCdtr(objectFactory.createPartyIdentificationSEPA2());
				ctts.getCdtr().setNm(ti.getName());
				
				ctts.setCdtrAcct(objectFactory.createCashAccountSEPA2());
				ctts.getCdtrAcct().setId(objectFactory.createAccountIdentificationSEPA());
				ctts.getCdtrAcct().getId().setIBAN(ti.getIBAN());
				
				ctts.setRmtInf(objectFactory.createRemittanceInformationSEPA1Choice());
				ctts.getRmtInf().setUstrd(ti.getRemitanceInformation());
				
				if(ti.getPurposeCode() != null){
					ctts.setPurp(objectFactory.createPurposeSEPA());
					ctts.getPurp().setCd(ti.getPurposeCode().toString());
				}
				
				// Einzeltransaktion einfügen
				pmts.getCdtTrfTxInf().add(ctts);
			}
			
			
			// Debitor Einfügen
			cstmrCdtTrfInitn.getPmtInf().add(pmts);
			
		}
		
		
		// Exportieren
		try {
			
			OutputStream os = new ByteArrayOutputStream();
			
			JAXBContext jc = JAXBContext.newInstance(Document.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:iso:std:iso:20022:tech:xsd:pain.001.003.03 pain.001.003.03.xsd");
			
			m.marshal(document, os);
			
			return os;
			
			
		} catch (JAXBException e) {
			log.error(e);
		}
		return null;
	}
	
	
}
