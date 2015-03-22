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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ws.michalski.sepa.pain.exeptions.SEPASchemaException;
import ws.michalski.sepa.pain.interfaces.SEPAReader;
import ws.michalski.sepa.pain.interfaces.SEPAWriter;
import ws.michalski.sepa.pain.readers.SEPAReaderPain_001_002_03;
import ws.michalski.sepa.pain.readers.SEPAReaderPain_001_003_03;
import ws.michalski.sepa.pain.readers.SEPAReaderPain_008_001_02;
import ws.michalski.sepa.pain.readers.SEPAReaderPain_008_002_02;
import ws.michalski.sepa.pain.readers.SEPAReaderPain_008_003_02;
import ws.michalski.sepa.pain.writers.SEPAWriterPain_001_002_03;
import ws.michalski.sepa.pain.writers.SEPAWriterPain_001_003_03;
import ws.michalski.sepa.pain.writers.SEPAWriterPain_008_001_02;
import ws.michalski.sepa.pain.writers.SEPAWriterPain_008_002_02;
import ws.michalski.sepa.pain.writers.SEPAWriterPain_008_003_02;

/**
 * Liefert die passende pain Klasse zurück
 * 
 * @author markus
 * @version 20131110
 */
public class SEPAFactory {

	public static enum PainType {PAIN_001_002_03, PAIN_001_003_03, PAIN_008_002_02, PAIN_008_001_02, PAIN_008_003_02}
	
	/**
	 * TRF = Überweisung
	 * DD  = Lastschrift
	 */
	public static enum CurrentPainType{TRF, DD}
	
	private static final Log log = LogFactory.getLog(SEPAFactory.class);
	
	
	/**
	 * Liefert die aktuelle Klasse des SEPAWriters zurück 
	 * TRF = Überweisung
	 * DD  = Lastschrift
	 * 
	 * @param cpType
	 * @return
	 * @throws SEPASchemaException
	 */
	public static SEPAWriter getSEPAWriter(CurrentPainType cpType) throws SEPASchemaException{
		
		// Achtung: Diese Klasse muss immer bei aktualisieren der Formate angepasst werden !!!
		
		if(cpType == CurrentPainType.TRF){
			return new SEPAWriterPain_001_003_03();
		}
		
		if(cpType == CurrentPainType.DD){
			return new SEPAWriterPain_008_003_02();
		} 
		
		return null;
	}
	
	
	
	public static SEPAWriter getSEPAWriter(PainType type) throws SEPASchemaException{
		
		if(type == PainType.PAIN_001_002_03){
			return new SEPAWriterPain_001_002_03();
		} 
		
		if(type == PainType.PAIN_001_003_03){
			return new SEPAWriterPain_001_003_03();
		} 
		
		if(type == PainType.PAIN_008_002_02){
			return new SEPAWriterPain_008_002_02();
		} 
		
		if(type == PainType.PAIN_008_001_02){
			return new SEPAWriterPain_008_001_02();
		} 
		
		if(type == PainType.PAIN_008_003_02){
			return new SEPAWriterPain_008_003_02();
		} 
		
		
		log.error("Schema " + type + " nicht gefunden!");
		throw new SEPASchemaException("Schema " + type + " nicht gefunden!");
	}
	
	
	public static PainType getPainFromSting(String pain) throws SEPASchemaException{
		
		String pain_a = null;
		int pos = pain.indexOf("pain.");
		
		pain_a = pain.substring(pos);
		
		if(pain_a.startsWith("pain.001.002.03")){
			return PainType.PAIN_001_002_03;
		}
		
		
		if(pain_a.startsWith("pain.001.003.03")){
			return PainType.PAIN_001_003_03;
		}
		
		if(pain_a.startsWith("pain.008.001.02")){
			return PainType.PAIN_008_001_02;
		}
		
		if(pain_a.startsWith("pain.008.002.02")){
			return PainType.PAIN_008_002_02;
		}
		
		if(pain_a.startsWith("pain.008.003.02")){
			return PainType.PAIN_008_003_02;
		}
		
		log.error("Schema fuer" + pain + " nicht gefunden!");
		throw new SEPASchemaException("Schema " + pain + " nicht gefunden!");
	}
	
	
	public static SEPAReader getSEPAReader(PainType type) throws SEPASchemaException{
		
		if(type == PainType.PAIN_001_002_03){
			return new SEPAReaderPain_001_002_03();
		} 
		
		if(type == PainType.PAIN_001_003_03){
			return new SEPAReaderPain_001_003_03();
		}
		
		if(type == PainType.PAIN_008_001_02){
			return new SEPAReaderPain_008_001_02();
		}
		
		if(type == PainType.PAIN_008_002_02){
			return new SEPAReaderPain_008_002_02();
		}
		
		if(type == PainType.PAIN_008_003_02){
			return new SEPAReaderPain_008_003_02();
		}
		
		log.error("Schema " + type + " nicht gefunden!");
		throw new SEPASchemaException("Schema " + type + " nicht gefunden!");
	}
}
