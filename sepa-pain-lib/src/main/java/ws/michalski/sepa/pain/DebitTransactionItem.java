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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.Purpose.PurposeCode;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.Verifable;

/**
 * Diese Klasse implementiert einzelne Position von DebitTransfer
 * Hier sind nur die Felder des Zahlungspflichtiger vorhanden
 * Alle Informationen über den Empfänger sind in der Klasse DebitTransction vorhanden
 * 
 * @author markus
 * @version 20131110
 */
public class DebitTransactionItem implements Verifable{

	/**
	 * Tag EndToEndId
	 * 
	 * Eindeutige Referenz des Auftraggebers
	 * Diese Referenz wird unverändert durch die gesamte Kette bis zum Zahlungspflichtigen geleitet 
	 * (Ende-zu-Ende-Referenz). 
	 */
	private String endToEndId;
	
	/**
	 * Attribut Ccy in IstdAmt
	 * 
	 * Währungskennzeichen
	 */
	private Currency currency;
	
	/**
	 * Tag InstdAmt
	 * 
	 * Betrag
	 */
	private BigDecimal amount;
	
	/**
	 * Tag BIC
	 * 
	 * BIC von Kreditinstitut des Zahlungspflichtigen 
	 * 
	 */
	private String BIC;
	
	/**
	 * Tag Dbtr/Nm 
	 * 
	 * Name Zahlungspflichtigen
	 */
	private String name;
	
	/**
	 * Tag Id/IBAN
	 * 
	 * IBAN Zahlungspflichtigen
	 */
	private String IBAN;
	
	/**
	 * Tag RmtInf/Ustrd
	 * 
	 * Verwendungszweck 
	 */
	private String remitanceInformation;
	
	/**
	 * Tag MndtRltdInf
	 * 
	 * Mandat
	 */
	private Mandate mandate;
	
	/**
	 * Tag Purp/Cd
	 * 
	 * Zweck / Textschlüssel
	 */
	private PurposeCode purposeCode;

	private Log log;
	
	
	public DebitTransactionItem(){
		super();
		
		log = LogFactory.getLog(DebitTransactionItem.class);
		
		currency = Currency.EUR;
		
		
		if(log.isTraceEnabled()){
			log.trace("Konstruktor DebitTransactionItem.class");
		}
	}

	
	@Override
	public void validate() throws SEPAException {
		
		
		
		// Falls BIC nicht belegt, mit NOTPROVIDED belegen
		if(BIC == null || BIC.isEmpty()){
			BIC = "NOTPROVIDED";
		}
		
		// Falls EndToEndId nicht gesetzt, wird auf NOTPROVIDED gesetzt
		if (endToEndId == null || endToEndId.isEmpty()) {
			endToEndId = "NOTPROVIDED";
		}
		
	}
	
	/**
	 * Eindeutige Referenz des Zahlers (Auftraggebers) Diese Referenz wird unverändert durch 
	 * die gesamte Kette bis zum Zahlungsempfänger geleitet (Ende-zu-Ende-Referenz).
	 * @return
	 */
	public String getEndToEndId() {
		return endToEndId;
	}

	/**
	 * Eindeutige Referenz des Zahlers (Auftraggebers) Diese Referenz wird unverändert durch 
	 * die gesamte Kette bis zum Zahlungsempfänger geleitet (Ende-zu-Ende-Referenz).
	 * @param endToEndId
	 * @return
	 */
	public DebitTransactionItem setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
		return this;
	}

	/**
	 * Währung (kann nur EUR sein)
	 * @return
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Währung (kann nur EUR sein)
	 * @param currency
	 * @return
	 */
	public DebitTransactionItem setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	/**
	 * Betrag
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Betrag
	 * @param amount
	 * @return
	 */
	public DebitTransactionItem setAmount(BigDecimal amount) {
		amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.amount = amount;
		return this;
	}

	/**
	 * BIC des Zahlungspflichtigen
	 * @return
	 */
	public String getBIC() {
		return BIC;
	}

	/**
	 * BIC des Zahlungspflichtigen (Debitor)
	 * @param bIC
	 * @return
	 */
	public DebitTransactionItem setBIC(String bIC) {
		BIC = bIC;
		return this;
	}

	/**
	 * Name des Zahlungspflichtigen (Debitor)
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name des Zahlungspflichtigen (Debitor)
	 * @param name
	 * @return
	 */
	public DebitTransactionItem setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * IBAN des Zahlungspflichtigen (Debitor)
	 * @return
	 */
	public String getIBAN() {
		return IBAN;
	}

	/**
	 * IBAN des Zahlungspflichtigen (Debitor)
	 * @param iBAN
	 * @return
	 */
	public DebitTransactionItem setIBAN(String iBAN) {
		IBAN = iBAN;
		return this;
	}

	/**
	 * Verwendungszweck
	 * @return
	 */
	public String getRemitanceInformation() {
		return remitanceInformation;
	}

	/**
	 * Verwendungszweck
	 * @param remitanceInformation
	 * @return
	 */
	public DebitTransactionItem setRemitanceInformation(String remitanceInformation) {
		this.remitanceInformation = remitanceInformation;
		return this;
	}

	/**
	 * Liefert Mandat zurück
	 * @return
	 */
	public Mandate getMandate() {
		return mandate;
	}

	/**
	 * Setzt Mandat
	 * @param mandate
	 * @return
	 */
	public DebitTransactionItem setMandate(Mandate mandate) {
		this.mandate = mandate;
		return this;
	}

	/**
	 * Zweck, Buchungstext
	 * @return
	 */
	public PurposeCode getPurposeCode() {
		return purposeCode;
	}

	/**
	 * Zweck, Buchungstext
	 * @param purposeCode
	 * @return
	 */
	public DebitTransactionItem setPurposeCode(PurposeCode purposeCode) {
		this.purposeCode = purposeCode;
		return this;
	}

}
