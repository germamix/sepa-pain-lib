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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.Verifable;

/**
 * Diese Klasse implementiert die Container-Klasse für SEPA-Nachrit pain.001
 * 
 * TODO: In addTransaction überprüfen, ob nur Überweisungen oder Lastschriften eingefügt werden.
 *       Entscheidend ist die erste Operation. 
 * 
 * @author markus
 * @version 20131110
 */
public class SEPAMessage implements Verifable {

	/**
	 * Tag MsgId
	 * 
	 * Nachrichten ID
	 */
	private String messageId;
	
	/**
	 * Tag InitgPty/Nm 
	 * 
	 * Name des Initiators oder eine Partei welche im Auftrag handelt
	 */
	private String name;
	
	/**
	 * 
	 */
	private XMLGregorianCalendar createDate;
	
	/**
	 * Tag NbOfTxs
	 * Anzahl Transaktionen im Sammler
	 */
	private int nbrOfTransaction;
	
	/**
	 * Tag CtrlSum
	 * Summe der Beträge aller Transaktionen
	 */
	private BigDecimal ctlSum;
	
	/**
	 * Liste von SEPATransactions
	 */
	private List<SEPATransaction> transactionsList;

	private Log log; 
	
	/**
	 * 
	 * @throws DatatypeConfigurationException
	 */
	public SEPAMessage() {
		super();
		
		log = LogFactory.getLog(SEPAMessage.class);
		
		transactionsList = new ArrayList<SEPATransaction>();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		createDate = SEPAUtility.convert(cal);
		ctlSum = new BigDecimal(0);
		
		if(log.isTraceEnabled()){
			log.trace("Konstruktor SEPAMessage.class");
		}
				
	}


	/**
	 * Fügt eine SEPATransaction hinzu
	 * @param transaction
	 * @throws SEPAException 
	 */
	public SEPAMessage addTransaction(SEPATransaction transaction) throws SEPAException{
		transaction.validate();
		transactionsList.add(transaction);
		return this;
	}
	
	/**
	 * Überprüfen auf Fehler und setzten von default Werten 
	 */
	@Override
	public void validate() throws SEPAException {
		
		
		// Falls messageId nicht belegt war, wird als UUID belegt
		if(messageId == null){
			messageId = UUID.randomUUID().toString();
			messageId = messageId.replaceAll("-", "");
		}
		
		// Falls Name nicht belegt ist, wird vom ersten Debitor/Kreditor genommen
		
		try {
			if(name == null){
				SEPATransaction st = transactionsList.get(0);
				if(st instanceof CreditTransaction){
					name = ((CreditTransaction) st).getName(); 
				}
				if(st instanceof DebitTransaction){
					name = ((DebitTransaction) st).getName(); 
				}
			}
		} catch (IndexOutOfBoundsException e) {
			log.error(e);
			throw new SEPAException("Keine transactionsList Postion vorhanden.");
		}
		
		// Anzahl Transaktionen
		nbrOfTransaction = transactionsList.size();
		//Summe berechnen
		ctlSum = BigDecimal.ZERO;
		for (SEPATransaction st : transactionsList) {
			ctlSum = ctlSum.add(st.getCtlSum());
		}

		// Felder testen
		if(name.length() > 70){
			throw new SEPAException("Feld Name > 70");
		}
	}

	/**
	 * Prüft, ob es sich um Überweisung (CT) handelt
	 * @return
	 */
	public boolean hasCreditTransaction(){
		boolean b = false;
		if(transactionsList.size() > 0){
			SEPATransaction st = transactionsList.get(0);
			if(st instanceof CreditTransaction){
				b = true; 
			}
		}
		return b;
	}
	
	/**
	 * Prüft, ob es sich um Lastschrift (DD) handelt
	 * @return
	 */
	public boolean hasDebitTransaction(){
		boolean b = false;
		if(transactionsList.size() > 0){
			SEPATransaction st = transactionsList.get(0);
			if(st instanceof DebitTransaction){
				b = true; 
			}
		}
		return b;
	}
	
	
	/**
	 * Punkt-zu-Punkt-Referenz der anweisenden Partei für die folgende Partei in der Nachrichten-Kette, 
	 * um die Nachricht (Datei) eindeutig zu identifizieren. 
	 * 
	 * @return
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Punkt-zu-Punkt-Referenz der anweisenden Partei für die folgende Partei in der Nachrichten-Kette, 
	 * um die Nachricht (Datei) eindeutig zu identifizieren. 
	 * 
	 * Wird das Feld nicht gesetzt, wird automatisch eine Id generiert 
	 * 
	 * @param messageId
	 * @return
	 */
	public SEPAMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}


	/**
	 * Name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name
	 * 
	 * Falls der Name nicht gesetzt ist, wird automatisch von ersten Transaction ermittelt. 
	 * 
	 * @param name
	 * @return
	 */
	public SEPAMessage setName(String name) {
		this.name = name;
		return this;
	}


	/**
	 *  Datum und Zeit, wann die ZV-Nachricht durch die anweisende Partei erzeugt wurde. 
	 * @return
	 */
	public XMLGregorianCalendar getCreateDate() {
		return createDate;
	}

	/**
	 *  Datum und Zeit, wann die ZV-Nachricht durch die anweisende Partei erzeugt wurde. 
	 *  
	 *  Das Feld wird ggf. automatisch gefüllt.
	 *  
	 * @return
	 */
	public SEPAMessage setCreateDate(XMLGregorianCalendar createDate) {
		this.createDate = createDate;
		return this;
	}

	/**
	 *  Anzahl der einzelnen Transaktionen innerhalb der gesamten Nachricht
	 * @return
	 */
	public int getNbrOfTransaction() {
		return nbrOfTransaction;
	}

	/**
	 * Diese Methode ist nur für interne Zwecke gedacht. Wert wird automatisch berechnet.
	 * Anzahl der einzelnen Transaktionen innerhalb der gesamten Nachricht
	 * @param nbrOfTransaction
	 */
	public void setNbrOfTransaction(int nbrOfTransaction) {
		this.nbrOfTransaction = nbrOfTransaction;
	}

	/**
	 * Summe der Beträge aller Einzeltransaktionen in der gesamten Nachricht 
	 * @return
	 */
	public BigDecimal getCtlSum() {
		return ctlSum;
	}

	/**
	 * Diese Methode ist nur für interne Zwecke gedacht. Wert wird automatisch berechnet.
	 * Summe der Beträge aller Einzeltransaktionen in der gesamten Nachricht 
	 * 
	 * @param ctlSum
	 */
	public void setCtlSum(BigDecimal ctlSum) {
		this.ctlSum = ctlSum;
	}


	/**
	 * Liefert eine Liste der Transaktionen zurück
	 * @return
	 */
	public List<SEPATransaction> getTransactionsList() {
		return transactionsList;
	}

	/**
	 * Fügt eine Liste von Transaktionen 
	 * @param transactionsList
	 */
	public void setTransactionsList(List<SEPATransaction> transactionsList) {
		this.transactionsList = transactionsList;
	}
	
	
	
}
