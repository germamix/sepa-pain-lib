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
 * Diese Klasse implementiert CreditTransfer
 * Hier sind nur die Felder von Zahlungspflichtigen vorhanden
 * Die Klasse beinhaltet List von einzelnen CreditTransction Positionen
 *  
 * @author markus
 * @version 20131110
 */

public class CreditTransaction extends SEPATransaction{

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
	 * Liste von Transaktionen
	 */
	private List<CreditTransactionItem> creditTransactionList;

	
	/**
	 * Taggleiche Eilüberweisung (falls Möglich, da erst ab pain.001.003.03 definiert)
	 */
	private boolean urgendServLev;
	
	private Log log;
	
	public CreditTransaction(){
		super();
		log = LogFactory.getLog(CreditTransaction.class);
		
		urgendServLev = false;
		creditTransactionList = new ArrayList<CreditTransactionItem>();

		ctlSum = new BigDecimal(0);
		ctlSum.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		if(log.isTraceEnabled()){
			log.trace("Konstruktor CreditTransaction.class");
		}
	}

	/**
	 * Neue Position wird eingefügt
	 * @param item
	 * @throws SEPAException 
	 */
	public CreditTransaction addCreditTransationItem(CreditTransactionItem item) throws SEPAException{
		item.validate();
		creditTransactionList.add(item);
		return this;
	}

	
	@Override
	public void validate() throws SEPAException {
		
		// Falls BIC nicht belegt, mit NOTPROVIDED belegen
		if(BIC == null || BIC.isEmpty()){
			BIC = "NOTPROVIDED";
		}
		
		// Falls Ausführungsdatum nicht belegt
		if(executionDate == null){
			calendar.set(1999, Calendar.JANUARY, 1);
			executionDate = (GregorianCalendar) calendar;
		}
		
		// Anzahl Positionen setzen 
		nbrOfTransaction = creditTransactionList.size();
		// Kontrollsumme berechnen
		ctlSum = BigDecimal.ZERO;
		for (CreditTransactionItem cti : creditTransactionList) {
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
	 * 
	 * @param paymentInformation
	 * @return
	 */
	public CreditTransaction setPaymentInformation(String paymentInformation) {
		this.paymentInformation = paymentInformation;
		return this;
	}

	/**
	 * Business  Identifier Code (SWIFT-Code) (Debitor)
	 * @return
	 */
	public String getBIC() {
		return BIC;
	}

	/**
	 * Business  Identifier Code (SWIFT-Code) (Debitor)
	 * 
	 * @param bIC
	 * @return
	 */
	public CreditTransaction setBIC(String bIC) {
		BIC = bIC;
		return this;
	}

	/**
	 * Debitor Name 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Debitor Name  
	 * @param name
	 * @return
	 */
	public CreditTransaction setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * International Bank Account Number (IBAN) (Debitor)
	 * @return
	 */
	public String getIBAN() {
		return IBAN;
	}

	/**
	 * International Bank Account Number (IBAN) (Debitor)
	 * 
	 * @param iBAN
	 * @return
	 */
	public CreditTransaction setIBAN(String iBAN) {
		IBAN = iBAN;
		return this;
	}

	/**
	 *  Ausführungstermin 
	 * @return
	 */
	public GregorianCalendar getExecutionDate() {
		return executionDate;
	}

	/**
	 *  Ausführungstermin 
	 * @param executionDate
	 * @return
	 */
	public CreditTransaction setExecutionDate(GregorianCalendar executionDate) {
		this.executionDate = executionDate;
		return this;
	}

	/**
	 * Liefert die Liste mit Transaktionen zurück.
	 * @return
	 */
	public List<CreditTransactionItem> getCreditTransactionList() {
		return creditTransactionList;
	}

	/**
	 * Prüft, ob taggleiche Eilüberweisung (falls Möglich, da erst ab pain.001.003.03 definiert)
	 * @return
	 */
	public boolean isUrgendServLev() {
		return urgendServLev;
	}

	/**
	 * Setzt Status für taggleiche Eilüberweisung (falls Möglich, da erst ab pain.001.003.03 definiert)
	 * @param urgendServLev
	 * @return
	 */
	public CreditTransaction setUrgendServLev(boolean urgendServLev) {
		this.urgendServLev = urgendServLev;
		return this;
	}

}
