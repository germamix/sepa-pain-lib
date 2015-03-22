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
package ws.michalski.sepa.pain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.exeptions.SEPAException;

/**
 * Diese Klasse implementiert DebitTransfer
 * Hier sind nur die Felder von Zahlungsempfänger vorhanden
 * Die Klasse beinhaltet List von einzelnen DebitTransction Positionen
 *  
 * @author markus
 * @version 20131110
 */

public class DebitTransaction extends SEPATransaction{

	
	public enum Type {CORE, COR1, B2B}
	
	public enum SeqType {FRST, RCUR, OOFF, FNAL}
	
	// Hilfsfeld
	Calendar calendar = new GregorianCalendar(); 
	
	/**
	 * Tag PmtInfld
	 * Referenz zur eindeutigen Identifizierung des Sammlers
	 *  
	 */
	private String paymentInformation;
	
	/**
	 * Tag BIC
	 * 
	 * BIC von Kreditinstitut des Auftraggebers 
	 * 
	 */
	private String BIC;
	
	/**
	 * Tag Cdtr/Nm 
	 * 
	 * Name Auftraggebers
	 */
	private String name;
	
	/**
	 * Tag Id/IBAN
	 * 
	 * IBAN Auftraggebers
	 */
	private String IBAN;
	
	/**
	 * Tag ReqdExctnDt
	 * Ausführungsdatum
	 */
	private GregorianCalendar executionDate;
	
	/**
	 * Tag LxlInstrm/Cd
	 * Typ der Lastschrift CORE, COR1 bzw. B2B
	 */
	private Type type;
	
	/**
	 * Tag SeqTp
	 * Sequenz Typ FRST erste, RCUR wiederholte, OOFF einmalige, FNAL letzte
	 */
	private SeqType seqType;
	
	/**
	 * CdtrSchmeld/Id/PrvtId/Othr/Id
	 */
	private String creditorId;
	
	/**
	 * Liste von Transaktionen
	 */
	private List<DebitTransactionItem> debitTransactionList;

	private Log log;
	
	public DebitTransaction(){
		super();
		
		log = LogFactory.getLog(DebitTransaction.class);
		
		type = Type.CORE;
		debitTransactionList = new ArrayList<DebitTransactionItem>();

		ctlSum = new BigDecimal(0);
		ctlSum.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		if(log.isTraceEnabled()){
			log.trace("Konstruktor DebitTransaction.class");
		}
		
	}

	/**
	 * Neue Position wird eingefügt
	 * @param item
	 * @throws SEPAException 
	 */
	public DebitTransaction addDebitTransationItem(DebitTransactionItem item) throws SEPAException{
		item.validate();
		debitTransactionList.add(item);
		return this;
	}

	/**
	 * Fehlerprüfung.
	 */
	@Override
	public void validate() throws SEPAException {
		
		if(type == null){
			type = Type.CORE;
		}
		
		if(seqType == null){
			seqType = SeqType.OOFF;
		}

		if(executionDate == null){
			
			int plusDays = 0;
			
			switch (seqType) {
			case FRST:
				plusDays = 5;
				break;
			case FNAL:
				plusDays = 2;
				break;
			case OOFF:
				plusDays = 5;
				break;
			case RCUR:
				plusDays = 2;
				break;
			
			}
			executionDate = (GregorianCalendar) GregorianCalendar.getInstance();
			executionDate.add(Calendar.DATE, plusDays);
			
		}
		
		
		
		// Anzahl Positionen setzen 
		nbrOfTransaction = debitTransactionList.size();
		// Kontrollsumme berechnen
		ctlSum = BigDecimal.ZERO;
		for (DebitTransactionItem cti : debitTransactionList) {
			ctlSum = ctlSum.add(cti.getAmount());
		}

		// Felder prüfen
		if(IBAN == null || IBAN.isEmpty()){
			throw new SEPAException("IBAN Fehler");
		}
		
		if(IBAN.length() > 34){
			throw new SEPAException("IBAN Länge > 34");
		}
		
		if(BIC.length() != 8 && BIC.length() != 11){
			throw new SEPAException("BIC nicht 8 bzw. 11 stellig");
		}
		
		if(name == null || name.isEmpty()){
			throw new SEPAException("Name ist nicht belegt.");
		}
		
		if(name.length() > 70){
			throw new SEPAException("Name ist länger 70.");
		}
		
		if(creditorId == null || creditorId.isEmpty()){
			throw new SEPAException("Gläubiger-Id nicht vorhanden.");
		}
	}
	
	/**
	 * Referenz zur eindeutigen Identifizierung des Sammlers
	 * @return
	 */
	public String getPaymentInformation() {
		return paymentInformation;
	}

	/**
	 * Referenz zur eindeutigen Identifizierung des Sammlers
	 * @param paymentInformation
	 * @return
	 */
	public DebitTransaction setPaymentInformation(String paymentInformation) {
		this.paymentInformation = paymentInformation;
		return this;
	}

	/**
	 * Business  Identifier Code (SWIFT-Code) des Auftraggebers (Kreditor)
	 * @return
	 */
	public String getBIC() {
		return BIC;
	}

	/**
	 * Business  Identifier Code (SWIFT-Code) des Auftraggebers (Kreditor)
	 * @param bIC
	 * @return
	 */
	public DebitTransaction setBIC(String bIC) {
		BIC = bIC;
		return this;
	}

	/**
	 * Name des Auftraggebers  (Kreditor)
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name des Auftraggebers (Kreditor)
	 * @param name
	 * @return
	 */
	public DebitTransaction setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Kredior IBAN
	 * @return
	 */
	public String getIBAN() {
		return IBAN;
	}


	/***
	 * Kreditor IBAN
	 * @param iBAN
	 * @return
	 */
	public DebitTransaction setIBAN(String iBAN) {
		IBAN = iBAN;
		return this;
	}


	/**
	 * Ausführungsdatum 
	 * @return
	 */
	public GregorianCalendar getExecutionDate() {
		return executionDate;
	}


	/**
	 * Ausführungsdatum
	 * @param executionDate
	 * @return
	 */
	public DebitTransaction setExecutionDate(GregorianCalendar executionDate) {
		this.executionDate = executionDate;
		return this;
	}


	/**
	 * Liefert die Liste der Positionen in der Transaktion zurück
	 * @return
	 */
	public List<DebitTransactionItem> getDebitTransactionList() {
		return debitTransactionList;
	}

	/***
	 * Liefert SEPA Typ zurück CORE, COR1, B2B
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Liefert SEPA Typ zurück CORE, COR1, B2B
	 * @param type
	 * @return
	 */
	public DebitTransaction setType(Type type) {
		this.type = type;
		return this;
	}

	/**
	 * Der SequenceType gibt an, ob es sich um eine Erst-, Folge- , Einmal- oder letzt-
	 * malige Lastschrift handelt. 
	 * FRST -> erstmalige
	 * RCUR -> Folge
	 * OOFF -> einmalige 
	 * FNAL -> letztmalige 
	 * @return
	 */
	public SeqType getSeqType() {
		return seqType;
	}

	/**
	 * Der SequenceType gibt an, ob es sich um eine Erst-, Folge- , Einmal- oder letzt-
	 * malige Lastschrift handelt. 
	 * FRST -> erstmalige
	 * RCUR -> Folge
	 * OOFF -> einmalige 
	 * FNAL -> letztmalige 
	 * @param seqType
	 * @return
	 */
	public DebitTransaction setSeqType(SeqType seqType) {
		this.seqType = seqType;
		return this;
	}
	
	/**
	 * Erste Lastschrift
	 * @return
	 */
	public DebitTransaction setSeqenceFRST() {
		this.seqType = SeqType.FRST;
		return this;
	}
	
	/**
	 * Wiederholung der Lastschrift 
	 * @return
	 */
	public DebitTransaction setSeqenceRCUR() {
		this.seqType = SeqType.RCUR;
		return this;
	}
	
	/**
	 * Einmalige Lastschrift
	 * @return
	 */
	public DebitTransaction setSeqenceOOFF() {
		this.seqType = SeqType.OOFF;
		return this;
	}
	
	/**
	 * Letzte Lastschrift
	 * @return
	 */
	public DebitTransaction setSeqenceFNAL() {
		this.seqType = SeqType.FNAL;
		return this;
	}

	/**
	 * Identifikation des Zahlungsempfängers Gläubiger-Id
	 * @return
	 */
	public String getCreditorId() {
		return creditorId;
	}

	/**
	 * Identifikation des Zahlungsempfängers Gläubiger-Id
	 * @param creditorId
	 * @return
	 */
	public DebitTransaction setCreditorId(String creditorId) {
		this.creditorId = creditorId;
		return this;
	}

}
