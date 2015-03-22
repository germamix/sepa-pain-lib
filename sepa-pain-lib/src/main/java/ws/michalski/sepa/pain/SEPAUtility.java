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

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Klasse mit statischen Hilfsmethoden 
 * 
 * @author markus
 * @version 20131110
 *
 */
public class SEPAUtility {

	private static final Log log = LogFactory.getLog(SEPAUtility.class);
	
	/**
	 * Konvertiert zwischen GregorianCalender und XMLGregorianCalender Datum und Zeit
	 * 
	 * @param calendar
	 * @return
	 */
	public static XMLGregorianCalendar convert(GregorianCalendar calendar){
		
		XMLGregorianCalendar xmlCal = null;
		try {
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			log.error(e);
		}
		
		return xmlCal;
	}
	
	/**
	 * Konvertiert zwischen GregorianCalender und XMLGregorianCalender Datum und Zeit
	 * 
	 * @param calendar
	 * @return
	 */
	public static XMLGregorianCalendar convertYearOnly(GregorianCalendar calendar){
		
		XMLGregorianCalendar xmlCal = null;
		try {
				String str = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(str);
		} catch (DatatypeConfigurationException e) {
			log.error(e);
		}
		
		return xmlCal;
	}
	
	/**
	 * Konvertiert Datum als ISO int in GregorianCalendar
	 * @param isoDate
	 * @return
	 */
	public static GregorianCalendar convertYearOnly(int isoDate){
		
		if(isoDate < 19990101){
			isoDate = 19990101;
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		
		String strDate = String.valueOf(isoDate);
		int yyyy = Integer.parseInt(strDate.substring(0, 4));
		int mm   = Integer.parseInt(strDate.substring(4, 6));
		int dd   = Integer.parseInt(strDate.substring(6, 8));
		
		cal.set(yyyy, mm - 1, dd);
		
		return cal;
	}
	
	/**
	 * Konvertiert  GregorianCalender zu int
	 * 
	 * @param calendar
	 * @return
	 */
	public static int convertToEfdisDate(GregorianCalendar calendar){
		
		int efdisDate = 0;
		
		try {
				String str = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
				efdisDate = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			log.error(e);
		}
		return efdisDate;
	}
	
}
