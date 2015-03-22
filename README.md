# sepa-pain-lib
SEPA pain Library
Mit der Bibliothek kann man die SEPA PAIN Formate für Überweisung und Lastschrift lesen und erstellen.
 
Lesen:
```java
InputStream is = new FileInputStream("x.xml");
// Gelesen wird mit dem AutoReader	
SEPAAutoReader ar = new SEPAAutoReader();

SEPAMessage sm = ar.parse(is);

// Die Sepa-Nachricht steht jetzt zur Verfügung
if (sm.hasCreditTransaction()) {
	System.out.println("Es handelt sich um Überweisung!");
}
if (sm.hasDebitTransaction()) {
	System.out.println("Es handelt sich um Lastschrift!");
}
```
Schreiben:
```java
SEPAMessage sm = new SEPAMessage();
		
// Debitor Daten (es können mehrere Debitoren pro SEPA Nachricht sein)
		CreditTransaction ct = new CreditTransaction()
				.setName("Max Mustermann")
				.setBIC("ABCDEM1XXX")
				.setIBAN("DEXX778877000001013416")
				.setUrgendServLev(true)
				.setPaymentInformation("29123749876");
		
		// Kreditor Daten (es können mehrere Positionen pro Debitor sein)
		CreditTransactionItem cti1 = new CreditTransactionItem()
				.setBIC("DEUTDEDDXXX")
				.setIBAN("DE63300700100270704005")
				.setAmount(new BigDecimal("25.69"))
				.setEndToEndId("9123649128364")
				.setName("Vodafone")
				.setRemitanceInformation("Rechnung 4234234")
				.setPurposeCode(PurposeCode.ADVA);
				
		// Kreditor Daten (es können mehrere Positionen pro Debitor sein)
		CreditTransactionItem cti2 = new CreditTransactionItem()
				.setBIC("DEUTDEDDXXX")
				.setIBAN("DE63300700100270704005")
				.setAmount(new BigDecimal("15.09"))
				.setEndToEndId("233459128364")
				.setName("Vodafone")
				.setRemitanceInformation("Rechnung 53454333");
		
		ct.addCreditTransationItem(cti1);
		ct.addCreditTransationItem(cti2);
		
		
		sm.addTransaction(ct);
		sm.validate();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_001_003_03);
		baos = (ByteArrayOutputStream) sw.export(sm);
		
		System.out.println(baos.toString());
```
