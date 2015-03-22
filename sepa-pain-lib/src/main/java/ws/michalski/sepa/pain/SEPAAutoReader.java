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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import ws.michalski.sepa.pain.SEPAFactory.PainType;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.exeptions.SEPASchemaException;
import ws.michalski.sepa.pain.interfaces.SEPAReader;

public class SEPAAutoReader {

	
	private Log log;
	private Object root;
	private PainType painType;


	public SEPAAutoReader(){
		super();
		log = LogFactory.getLog(SEPAAutoReader.class);
	}
	
	
	public SEPAMessage parse(ByteArrayInputStream bai) throws SEPAException, SEPASchemaException {
		
		SEPAMessage sepaMessage = null;
		
		 String pain = getXmlnsAttribut(bai);
		 if(log.isDebugEnabled()){
			 log.debug(pain);
		 }
		
		 painType = SEPAFactory.getPainFromSting(pain);
		 SEPAReader sr = SEPAFactory.getSEPAReader(painType);
		 
		 sepaMessage = sr.parse(bai);
		 root = sr.getRootObject();
		 
		 return sepaMessage;
	}
	
	public SEPAMessage parse(InputStream is) throws SEPAException, SEPASchemaException {
		
		// InputStream in InputByteArray einlesen
		ByteArrayInputStream  bai = null;
		
		bai = getByteArrayInputStream(is, bai);
		return parse(bai);
	}

	public SEPAMessage parse(byte[] data) throws SEPAException, SEPASchemaException {
		
		ByteArrayInputStream  bai = null;
		
		bai = new ByteArrayInputStream(data);
		return parse(bai);
	}
	
	
	/**
	 * Liefert ByteArrayInputStream zurück
	 * ByteArrayInpuStream kann man mehrfach lesen, was für Ermittlung von SEPA PAIN-Type 
	 * notwendig ist. 
	 * 
	 * @param is
	 * @param bai
	 * @return
	 */
	private ByteArrayInputStream getByteArrayInputStream(InputStream is,
			ByteArrayInputStream bai) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		int bread = 0;
		byte[] buff = new byte[1024];
		
		try {
			
			while((bread = is.read(buff)) != -1){
				bao.write(buff, 0, bread);
			}
			
			bai = new ByteArrayInputStream(bao.toByteArray());
			
		} catch (IOException e) {
			log.error(e);
		}
		return bai;
	}
	
	

	/**
	 * Liefert xmlns Attribut von des XML 
	 * @param String zum Suchen als XPath Ausdruck
	 * @param Attribut
	 * @return String
	 */
	private String getXmlnsAttribut(ByteArrayInputStream is) {

		String erg = null;
		
		is.reset();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(is);
		} catch (ParserConfigurationException e) {
			log.error(e);
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		
		Element rootNode = doc.getDocumentElement();
		
		erg = rootNode.getAttribute("xmlns");
		
		is.reset();

		return erg;
	}

	/**
	 * Liefert root als Object zurück
	 * @return
	 */
	public Object getRoot() {
		return root;
	}


	public PainType getPainType() {
		return painType;
	}


}
