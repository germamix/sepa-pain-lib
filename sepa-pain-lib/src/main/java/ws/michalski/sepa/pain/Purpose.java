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

public class Purpose {

	public static enum PurposeCode {
		/** Account Management 	
		 *  Kontenmanagement */
		ACCT,
		/** Advance Payment 
		 *  Kreditzahlung */
		ADVA, 
		/** Agricultural Payment 
		 *  Landwirtschaftszahlung */
		AGRT, 
		/** Air 
		 *  Luftfracht */
		AIRB,
		/** Alimony Payment 
		 *  Unterhaltszahlung */
		ALMY, 
		/** Annuity 
		 *  Rente */
		ANNI, 
		/** Anesthesia Services 
		 *  Anästhesie Dienstleistung */
		ANTS, 
		/** Accounts Receivables Entry
		 *  Forderungseingang 	*/
		AREN, 
		/** Benefit Child 
		 *  Kindergeld (Kinderbeihilfe) */ 	
		BECH, 
		/** Benefit 
		 *  Unterstützungsleistungen bei  Erwerbslosigkeit, insbesondere ALG I. 	56 */
		BENE, 
		/** Business Expenses 
		 * 	Geschäftskosten */
		BEXP, 
		/** Back Office Conversion Entry 
		 * 	Buchung zu erfolgtem Umtausch */
		BOCE, 
		/** Bonus Payment 
		 *  Bonusleistungen 	Textschlüssel alt 53 */	
		BONU, 
		/** Bus 	Bus */
		BUSB, 
		/** Cash Management Transfer Transaction 
		 *  Dispositionszahlung */
		CASH, 
		/** Capital Building Fringe Fortune 
		 *  Vermögenswirksame Leistung 	Textschlüssel alt 54 */ 	
		CBFF, 
		/** Cable TV Bill 
		 * 	Kabel-TV Rechnung */
		CBTV, 
		/** Credit Card Payment 
		 *  Kreditkartenzahlung */
		CCRD, 
		/** Credit Card Bill Kreditkartenabrechnung 
		 *  Lastschrift aus Kreditkartenumsätzen Textschlüssel alt 05 */
		CDBL, 
		/** Card Payment with Cashback 
		 * 	POS-Zahlung mit Cashbac 	*/
		CDCB, 
		/** Cash disbursement 	
		 *  Barauszahlung 	*/
		CDCD, 
		/** Original Credit 
		 * 	Gutschrift aus Glückspiel 	*/
		CDOC, 
		/** Quasi Cash 
		 * 	Gutschrift für Bargeldersatz */
		CDQC,
		/** Cancellation Fee 
		 * 	Stornogebühr */
		CFEE, 
		/** Charity 
		 * 	Spende Textschlüssel alt 69 */	
		CHAR, 
		/** Car Loan Repayment  
		 * 	Ablösung Autodarlehen */
		CLPR, 
		/** Commodities 
		 * 	Wirtschaftsgüter */
		CMDT, 
		/** Collection Payment 
		 * 	Einzug 	*/
		COLL, 
		/** Commercial Payment 
		 * 	Warenkredit */
		COMC,
		/** Commission 
		 * 	Provision */
		COMM, 
		/** Consumer Third  Party Consol Pay 
		 *  Zahlung zu Gunsten Dritter */
		COMT, 
		/** Trade Settlement Payment Courtage Transaction 
		 *  SEPA Überweisung wegen Handelszahlungen  */
		CORT, 
		/** Costs 	Kosten 	 */
		COST, 
		/** Copyright 
		 * 	Kopierrechte 	*/
		CPYR, 
		/**Cash Disbursement 
		 * Bareinzahlung, Geldausgabeautomat Entgeld-Verrechnung */
		CSDB, 
		/** Company Social Loan Payment To Bank 
		 *  Firmensozialkredit-Zahlung an die Bank */
		CSLP, 
		/** Convalescent Care Facility 
		 * 	Genesung Vorsorge 	*/
		CVCF, 
		/** Debit Collection Payment 
		 * 	Lastschriftzahlung 	 */
		DBTC, 
		/**Debit Card Payment 
		 *  Debitkartenzahlung */
		DCRD, 
		/** Deposit 
		 * 	Einzahlung 	 */
		DEPT, 
		/** Derivatives 
		 * 	Derivate 	*/
		DERI, 
		/** Dividend Dividendenzahlungen */
		DIVI, 
		/** Medical Equipment 
		 * 	Medizinische Anlagen */
		DMEQ, 
		/** DentalServices 	
		 *  Zahnservices  */
		DNTS,
		/** Electricity Bill 
		 * 	Stromrechnung 	*/
		ELEC, 
		/** Energies 
		 * 	Energie */
		ENRG, 
		/** E- Payment 
		 *  Online Banking */
		EPAY, 
		/** Estate Tax 
		 * 	Vermögenssteuer */
		ESTX, 
		/** Ferry 
		 * 	Fähre 	*/
		FERB, 
		/** Foreign Exchange 
		 * 	Devisenverkehr */
		FREX, 
		/** GasBill 
		 * 	Gasrechnung 	*/
		GASB, 
		/** Purchase Sale Of Goods 
		 * 	An- und Verkauf von Waren 	*/
		GDDS, 
		/** PurchaseSaleOfGoodsAndServices 
		 * 	Kauf von Waren und Service mit Rückvergütung */
		GDSV, 
		/** Government Insurance 
		 * 	Versicherung von Behörden 	*/
		GOVI, 
		/** Government Payment 
		 *  Überweisung (Einzelbuchung) wegen staatlicher öffentlicher Kassen. Textschlüssel alt 56 */
		GOVT, 
		/** Pur Sale Of Good And Service With Cash Back 
		 *  Rückvergütung aus Ankauf von Waren und Service  */
		GSCB, 
		/** Austrian Government Employee CategoryA 
		 *  Österreichische Regierungsangestellter Kategorie A 	*/
		GVEA, 
		/** Austrian Government Employee Category B 
		 *  Österreichische Regierungsangestellter Kategorie B 	*/
		GVEB, 
		/** Austrian Government	Employee Category C 
		 *  Österreichische Regierungsangestellte Kategorie C 	*/
		GVEC, 
		/** Austrian Government Employee Category D 
		 *  Österreichische Regierungsangestellter Kategorie D 	*/
		GVED, 
		/** Hedging Transaction is related to the payment of a hedging operation 
		 *  Deckungsgeschäft	*/
		HEDG, 
		/** Housing Loan Repayment 
		 * 	Ablösung Hausdarlehen */
		HLRP, 
		/** Home Health Care 
		 * 	Gesundheitsvorsorge 	*/
		HLTC, 
		/** Health Insurance 
		 * 	Krankenversicherung */
		HLTI, 
		/** Hospital Care 
		 * 	Krankenhausvorsorge 	*/
		HSPC, 
		/** Housing Tax 
		 * 	Haus- und Grundsteuer */
		HSTX, 
		/** Irrevocable Credit Card Payment 
		 *  Garantierte Kartenzahlung */
		ICCP, 
		/** Intermediate Care Facility 
		 * 	Kurzzeitbetreuung */
		ICRF, 
		/** Irrevocable Debit Card Payment
		 *  Garantierte Debitkartenzahlung	*/
		IDCP, 
		/** Instalment Hire Purchase Agreement
		 * 	Ratenkauf 	*/
		IHRP, 
		/** nsurancePremiumCar 
		 * 	Fahrzeugversicherung */
		INPC, 
		/** Installment 
		 * 	Abzahlung/Rate 	 */
		INSM, 
		/** Insurance Premium 
		 * 	Premium-Versicherung 	 */
		INSU, 
		/** Intra Company Payment
		 *  SEPA Überweisung wegen Konzernzahlungen 	  */
		INTC, 
		/** Interest 
		 *  Zinszahlungen */
		INTE, 
		/** Income Tax 	
		 *  Einkommensteuer */
		INTX, 
		/** Labor Insurance 
		 * 	Arbeitslosenversicherung 	*/
		LBRI, 
		/** License Fee 
		 * 	Lizenzgebühr */
		LICF, 
		/** Life Insurance 
		 * 	Lebensversicherung 	*/
		LIFI, 
		/**Liquidity Management 
		 * Liquiditätsmanagement 	*/
		LIMA, 
		/**Loan
		 * Kreditzahlungen */
		LOAN, 
		/** Loan Repayment 
		 *  Ablösung Kredit */
		LOAR, 
		/** Long Term Care Facility
		 *  Langzeitbetreuung 	*/
		LTCF, 
		/** Medical Services
		 *  Medizinischer Service 	*/
		MDCS, 
		/** Multiple Service Types 
		 * 	Unterschiedliche Dienstleistungen */
		MSVC, 
		/** Netting 
		 * 	Spitzenausgleich 	*/
		NETT, 
		/** NetIncomeTax 
		 * 	Einkommensteuer */
		NITX, 
		/** Not Otherwise Specified 
		 *  nicht näher definiert 	*/
		NOWS, 
		/** Network Charge 
		 * 	Netzwerkentgelt/Netzwerk-Abbuchungen */
		NWCH, 
		/** Network Communication 
		 * 	Netzwerkkommunikation */
		NWCM, 
		/** Opening Fee 
		 *  Eröffnungsgebühren */
		OFEE, 
		/** Other payment purpose. 
		 * 	Sonstiges, andere */
		OTHR, 
		/** Other Telecom Related Bill 	
		 *  Andere Telecom-Rechnungen 	*/
		OTLC, 
		/** Preauthorized debit 
		 * 	Authorisierte Verfügung */
		PADD, 
		/** Payroll 
		 *  Gehaltszahlung, analog SALA	Textschlüssel alt 53	*/
		PAYR, 
		/** Pension Payment 
		 *  Gutschrift Renten und Sonstige Altersbezüge (Pensionen) "	53	*/
		PENS, 
		/** Telephone Bill 	
		 *  Telefonrechnung 	*/
		PHON, 
		/** Point of Purchase Entry
		 *  Zahlung am Point of Sale 	 */
		POPE, 
		/** Property Insurance
		 *  Hausratsversicherung 	*/
		PPTI, 
		/** Price Payment
		 *  Preiszahlung 	*/
		PRCP, 
		/** Precious Metal
		 *  Edelmetall 	 */
		PRME, 
		/** Payment Terms 
		 *  Zahlungsbedingungen/ Zahlungskonditionen 	*/
		PTSP, 
		/** Re-presented Check Entry
		 *  Wiedervorgelegte Scheckzahlung 	 */
		RCKE, 
		/** Receipt Payment
		 *  akzeptierte Zahlung 	 */
		RCPT, 
		/** Refund 	
		 *  Gutschrift, Rücküberweisung 	*/
		REFU, 
		/** Rent 	Mieten 	*/
		RENT, 
		/** Salary Payment
		 *  Dauerauftrag (Ratenzahlung) Textschlüssel alt 52	*/
		RINP, 
		/** Railway
		 *  Eisenbahn 	 */
		RLWY, 
		/** Royalties
		 *  Tantiemen 	 */
		ROYA, 
		/** Salary Payment 
		 *  Gutschrift Gehaltszahlung Textschlüssel alt 53 und 56*/
		SALA,
		/** Savings
		 *  Spareinlagen, Renten 	*/
		SAVG, 
		/** Purchase Sale Of Services
		 *  Kauf und Verkauf von Dienstleistungen 	*/
		SCVE, 
		/** Securities 
		 *  Wertpapierzahlungen 	  */
		SECU, 
		/** Social Security Benefit 
		 *  Sozialversicherungsbeihilfe Textschlüssel alt 56*/
		SSBE, 
		/** Salary Payment  
		 *  BAföG (Studium) */
		STDY, 
		/** Subscription
		 *  Abonnement/Beitrag 	*/
		SUBS, 
		/** Supplier Payment 
		 *  Lieferantenzahlung  */
		SUPP, 
		/** Tax Payment 
		 *  Steuerzahlungen */ 	
		TAXS, 
		/** Telephone-Initiated Transaction
		 *  Telefonisch veranlasste Transaktion */
		TELI, 
		/** TradeServices
		 *  Handelsgeschäfte */
		TRAD, 
		/** Treasury Payment 
		 *  Finanzzahlungen */
		TREA, 
		/** Trust Fund
		 *  Treuhandfonds */
		TRFD, 
		/** Value Added Tax Payment
		 *  Umsatzsteuerzahlung */
		VATX,
		/** Vision Care
		 *  Augenvorsorge */
		VIEW,
		/** Internet-Initiated Transaction
		 *  Internet basierte Transaktion */
		WEBI, 
		/** With Holding 
		 *  Kapitalertrags- Quellensteuer */
		WHLD, 
		/** Water Bill
		 *  Wasserrechnung */
		WTER 
	}
		
		
	public static PurposeCode getPurposeCode(String code) {

		if(code == null){
			return null;
		}
		
		if (code.equals("ACCT")) {
			return PurposeCode.ACCT;
		}
		if (code.equals("ADVA")) {
			return PurposeCode.ADVA;
		}
		if (code.equals("AGRT")) {
			return PurposeCode.AGRT;
		}
		if (code.equals("AIRB")) {
			return PurposeCode.AIRB;
		}
		if (code.equals("ALMY")) {
			return PurposeCode.ALMY;
		}
		if (code.equals("ANNI")) {
			return PurposeCode.ANNI;
		}
		if (code.equals("ANTS")) {
			return PurposeCode.ANTS;
		}
		if (code.equals("AREN")) {
			return PurposeCode.AREN;
		}
		if (code.equals("BECH")) {
			return PurposeCode.BECH;
		}
		if (code.equals("BENE")) {
			return PurposeCode.BENE;
		}
		if (code.equals("BEXP")) {
			return PurposeCode.BEXP;
		}
		if (code.equals("BOCE")) {
			return PurposeCode.BOCE;
		}
		if (code.equals("BONU")) {
			return PurposeCode.BONU;
		}
		if (code.equals("BUSB")) {
			return PurposeCode.BUSB;
		}
		if (code.equals("CASH")) {
			return PurposeCode.CASH;
		}
		if (code.equals("CBFF")) {
			return PurposeCode.CBFF;
		}
		if (code.equals("CBTV")) {
			return PurposeCode.CBTV;
		}
		if (code.equals("CCRD")) {
			return PurposeCode.CCRD;
		}
		if (code.equals("CDBL")) {
			return PurposeCode.CDBL;
		}
		if (code.equals("CDCB")) {
			return PurposeCode.CDCB;
		}
		if (code.equals("CDCD")) {
			return PurposeCode.CDCD;
		}
		if (code.equals("CDOC")) {
			return PurposeCode.CDOC;
		}
		if (code.equals("CDQC")) {
			return PurposeCode.CDQC;
		}
		if (code.equals("CFEE")) {
			return PurposeCode.CFEE;
		}
		if (code.equals("CHAR")) {
			return PurposeCode.CHAR;
		}
		if (code.equals("CLPR")) {
			return PurposeCode.CLPR;
		}
		if (code.equals("CMDT")) {
			return PurposeCode.CMDT;
		}
		if (code.equals("COLL")) {
			return PurposeCode.COLL;
		}
		if (code.equals("COMC")) {
			return PurposeCode.COMC;
		}
		if (code.equals("COMM")) {
			return PurposeCode.COMM;
		}
		if (code.equals("COMT")) {
			return PurposeCode.COMT;
		}
		if (code.equals("CORT")) {
			return PurposeCode.CORT;
		}
		if (code.equals("COST")) {
			return PurposeCode.COST;
		}
		if (code.equals("CPYR")) {
			return PurposeCode.CPYR;
		}
		if (code.equals("CSDB")) {
			return PurposeCode.CSDB;
		}
		if (code.equals("CSLP")) {
			return PurposeCode.CSLP;
		}
		if (code.equals("CVCF")) {
			return PurposeCode.CVCF;
		}
		if (code.equals("DBTC")) {
			return PurposeCode.DBTC;
		}
		if (code.equals("DCRD")) {
			return PurposeCode.DCRD;
		}
		if (code.equals("DEPT")) {
			return PurposeCode.DEPT;
		}
		if (code.equals("DERI")) {
			return PurposeCode.DERI;
		}
		if (code.equals("DIVI")) {
			return PurposeCode.DIVI;
		}
		if (code.equals("DMEQ")) {
			return PurposeCode.DMEQ;
		}
		if (code.equals("DNTS")) {
			return PurposeCode.DNTS;
		}
		if (code.equals("ELEC")) {
			return PurposeCode.ELEC;
		}
		if (code.equals("ENRG")) {
			return PurposeCode.ENRG;
		}
		if (code.equals("EPAY")) {
			return PurposeCode.EPAY;
		}
		if (code.equals("ESTX")) {
			return PurposeCode.ESTX;
		}
		if (code.equals("FERB")) {
			return PurposeCode.FERB;
		}
		if (code.equals("FREX")) {
			return PurposeCode.FREX;
		}
		if (code.equals("GASB")) {
			return PurposeCode.GASB;
		}
		if (code.equals("GDDS")) {
			return PurposeCode.GDDS;
		}
		if (code.equals("GDSV")) {
			return PurposeCode.GDSV;
		}
		if (code.equals("GOVI")) {
			return PurposeCode.GOVI;
		}
		if (code.equals("GOVT")) {
			return PurposeCode.GOVT;
		}
		if (code.equals("GSCB")) {
			return PurposeCode.GSCB;
		}
		if (code.equals("GVEA")) {
			return PurposeCode.GVEA;
		}
		if (code.equals("GVEB")) {
			return PurposeCode.GVEB;
		}
		if (code.equals("GVEC")) {
			return PurposeCode.GVEC;
		}
		if (code.equals("GVED")) {
			return PurposeCode.GVED;
		}
		if (code.equals("HEDG")) {
			return PurposeCode.HEDG;
		}
		if (code.equals("HLRP")) {
			return PurposeCode.HLRP;
		}
		if (code.equals("HLTC")) {
			return PurposeCode.HLTC;
		}
		if (code.equals("HLTI")) {
			return PurposeCode.HLTI;
		}
		if (code.equals("HSPC")) {
			return PurposeCode.HSPC;
		}
		if (code.equals("HSTX")) {
			return PurposeCode.HSTX;
		}
		if (code.equals("ICCP")) {
			return PurposeCode.ICCP;
		}
		if (code.equals("ICRF")) {
			return PurposeCode.ICRF;
		}
		if (code.equals("IDCP")) {
			return PurposeCode.IDCP;
		}
		if (code.equals("IHRP")) {
			return PurposeCode.IHRP;
		}
		if (code.equals("INPC")) {
			return PurposeCode.INPC;
		}
		if (code.equals("INSM")) {
			return PurposeCode.INSM;
		}
		if (code.equals("INSU")) {
			return PurposeCode.INSU;
		}
		if (code.equals("INTC")) {
			return PurposeCode.INTC;
		}
		if (code.equals("INTE")) {
			return PurposeCode.INTE;
		}
		if (code.equals("INTX")) {
			return PurposeCode.INTX;
		}
		if (code.equals("LBRI")) {
			return PurposeCode.LBRI;
		}
		if (code.equals("LICF")) {
			return PurposeCode.LICF;
		}
		if (code.equals("LIFI")) {
			return PurposeCode.LIFI;
		}
		if (code.equals("LIMA")) {
			return PurposeCode.LIMA;
		}
		if (code.equals("LOAN")) {
			return PurposeCode.LOAN;
		}
		if (code.equals("LOAR")) {
			return PurposeCode.LOAR;
		}
		if (code.equals("LTCF")) {
			return PurposeCode.LTCF;
		}
		if (code.equals("MDCS")) {
			return PurposeCode.MDCS;
		}
		if (code.equals("MSVC")) {
			return PurposeCode.MSVC;
		}
		if (code.equals("NETT")) {
			return PurposeCode.NETT;
		}
		if (code.equals("NITX")) {
			return PurposeCode.NITX;
		}
		if (code.equals("NOWS")) {
			return PurposeCode.NOWS;
		}
		if (code.equals("NWCH")) {
			return PurposeCode.NWCH;
		}
		if (code.equals("NWCM")) {
			return PurposeCode.NWCM;
		}
		if (code.equals("OFEE")) {
			return PurposeCode.OFEE;
		}
		if (code.equals("OTHR")) {
			return PurposeCode.OTHR;
		}
		if (code.equals("OTLC")) {
			return PurposeCode.OTLC;
		}
		if (code.equals("PADD")) {
			return PurposeCode.PADD;
		}
		if (code.equals("PAYR")) {
			return PurposeCode.PAYR;
		}
		if (code.equals("PENS")) {
			return PurposeCode.PENS;
		}
		if (code.equals("PHON")) {
			return PurposeCode.PHON;
		}
		if (code.equals("POPE")) {
			return PurposeCode.POPE;
		}
		if (code.equals("PPTI")) {
			return PurposeCode.PPTI;
		}
		if (code.equals("PRCP")) {
			return PurposeCode.PRCP;
		}
		if (code.equals("PRME")) {
			return PurposeCode.PRME;
		}
		if (code.equals("PTSP")) {
			return PurposeCode.PTSP;
		}
		if (code.equals("RCKE")) {
			return PurposeCode.RCKE;
		}
		if (code.equals("RCPT")) {
			return PurposeCode.RCPT;
		}
		if (code.equals("REFU")) {
			return PurposeCode.REFU;
		}
		if (code.equals("RENT")) {
			return PurposeCode.RENT;
		}
		if (code.equals("RINP")) {
			return PurposeCode.RINP;
		}
		if (code.equals("RLWY")) {
			return PurposeCode.RLWY;
		}
		if (code.equals("ROYA")) {
			return PurposeCode.ROYA;
		}
		if (code.equals("SALA")) {
			return PurposeCode.SALA;
		}
		if (code.equals("SAVG")) {
			return PurposeCode.SAVG;
		}
		if (code.equals("SCVE")) {
			return PurposeCode.SCVE;
		}
		if (code.equals("SECU")) {
			return PurposeCode.SECU;
		}
		if (code.equals("SSBE")) {
			return PurposeCode.SSBE;
		}
		if (code.equals("STDY")) {
			return PurposeCode.STDY;
		}
		if (code.equals("SUBS")) {
			return PurposeCode.SUBS;
		}
		if (code.equals("SUPP")) {
			return PurposeCode.SUPP;
		}
		if (code.equals("TAXS")) {
			return PurposeCode.TAXS;
		}
		if (code.equals("TELI")) {
			return PurposeCode.TELI;
		}
		if (code.equals("TRAD")) {
			return PurposeCode.TRAD;
		}
		if (code.equals("TREA")) {
			return PurposeCode.TREA;
		}
		if (code.equals("TRFD")) {
			return PurposeCode.TRFD;
		}
		if (code.equals("VATX")) {
			return PurposeCode.VATX;
		}
		if (code.equals("VIEW")) {
			return PurposeCode.VIEW;
		}
		if (code.equals("WEBI")) {
			return PurposeCode.WEBI;
		}
		if (code.equals("WHLD")) {
			return PurposeCode.WHLD;
		}
		if (code.equals("WTER")) {
			return PurposeCode.WTER;
		}

		return PurposeCode.OTHR;

	}
	
	
}
