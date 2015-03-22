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

import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Diese Klasse implementiert SEPA Mandat für DebitTransaction
 * 
 * 
 * 
 * 
 * @author markus
 * @version 20131110
 *
 *
 */
public class Mandate {
	
	public String referenz;
	
	public GregorianCalendar signaturDate;
	
	public boolean amendmentIndicator;

	private Log log;
	
	public Mandate(String referenz){
		super();
		
		log = LogFactory.getLog(Mandate.class);
		
		this.referenz = referenz;
		this.signaturDate = new GregorianCalendar();
		this.amendmentIndicator = false;
		
		if(log.isTraceEnabled()){
			log.trace("Konstruktor Mandate.class");
		}
		
		
	}

	public Mandate(String referenz, GregorianCalendar signaturDate) {
		super();
		this.referenz = referenz;
		this.signaturDate = signaturDate;
		this.amendmentIndicator = false;
	}

	public String getReferenz() {
		return referenz;
	}

	public void setReferenz(String referenz) {
		this.referenz = referenz;
	}

	public GregorianCalendar getSignaturDate() {
		return signaturDate;
	}

	public void setSignaturDate(GregorianCalendar signaturDate) {
		this.signaturDate = signaturDate;
	}

	public boolean isAmendmentIndicator() {
		return amendmentIndicator;
	}

	public void setAmendmentIndicator(boolean amendmentIndicator) {
		this.amendmentIndicator = amendmentIndicator;
	}
	
	
	
	

}
