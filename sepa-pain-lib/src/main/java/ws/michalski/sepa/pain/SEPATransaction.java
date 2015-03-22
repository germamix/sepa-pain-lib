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

import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.interfaces.Verifable;

public abstract class SEPATransaction implements Verifable {

	/**
	 * Tag NbOfTxs
	 * Anzahl Transaktionen im Sammler
	 */
	protected int nbrOfTransaction;
	/**
	 * Tag CtrlSum
	 * Kontrollsumme
	 */
	
	
	protected BigDecimal ctlSum;
	
	
	

	@Override
	public abstract void validate() throws SEPAException;
	
	/**
	 * Liefert die Anzahl der Positionen in der Transaktion
	 * @return
	 */
	public int getNbrOfTransaction() {
		return nbrOfTransaction;
	}

	/**
	 * Liefert die Summe aller Positionen in der Transaktion
	 * @return
	 */
	public BigDecimal getCtlSum() {
		return ctlSum;
	}

	/**
	 * Methode nur für interne Nutzung.
	 * Setzt die Anzahl der Positionen in der Transaktion
	 * 
	 * @param nbrOfTransaction
	 */
	public void setNbrOfTransaction(int nbrOfTransaction) {
		this.nbrOfTransaction = nbrOfTransaction;
	}

	/**
	 * Methode nur für interne Nutzung.
	 * Setzt die Summe aller Positionen in der Transaction
	 * @param ctlSum
	 */
	public void setCtlSum(BigDecimal ctlSum) {
		this.ctlSum = ctlSum;
	}

	

}
