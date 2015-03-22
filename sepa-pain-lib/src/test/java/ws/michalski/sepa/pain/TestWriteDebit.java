package ws.michalski.sepa.pain;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.junit.Test;

import ws.michalski.sepa.pain.DebitTransaction;
import ws.michalski.sepa.pain.DebitTransactionItem;
import ws.michalski.sepa.pain.Mandate;
import ws.michalski.sepa.pain.SEPAAutoReader;
import ws.michalski.sepa.pain.SEPAFactory;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.DebitTransaction.Type;
import ws.michalski.sepa.pain.Purpose.PurposeCode;
import ws.michalski.sepa.pain.interfaces.SEPAWriter;

public class TestWriteDebit {

	@Test
	public void test2() throws Exception {
		

			SEPAMessage sm = new SEPAMessage();
			
			// Kreditor Daten (es können mehrere Debitoren pro SEPA Nachricht sein)
			DebitTransaction dt = new DebitTransaction()
					.setName("Max Mustermann")
					.setBIC("LENZDEM1XXX")
					.setIBAN("DE25701307000001013416")
					.setPaymentInformation("29123749876")
					.setCreditorId("DE98ZZZ09999999999")
					.setType(Type.CORE)
					.setSeqenceOOFF();
			
			// Debitor Daten (es können mehrere Positionen pro Kreditor sein)
			DebitTransactionItem dti1 = new DebitTransactionItem()
					.setBIC("DEUTDEDDXXX")
					.setIBAN("DE63300700100270704005")
					.setAmount(new BigDecimal("58.69"))
					.setEndToEndId("9123649128364")
					.setName("Vodafone")
					.setRemitanceInformation("Lastschrift 4234234")
					.setPurposeCode(PurposeCode.OTHR)
					.setMandate(new Mandate("123654"));
					
			DebitTransactionItem dti2 = new DebitTransactionItem()
			.setBIC("DEUTDEDDXXX")
			.setIBAN("DE63300700100270704005")
			.setAmount(new BigDecimal("120.01"))
			.setEndToEndId("9123649128364")
			.setName("Vodafone")
			.setRemitanceInformation("Lastschrift 4234234")
			.setMandate(new Mandate("123654"));

			
			dt.addDebitTransationItem(dti1);
			dt.addDebitTransationItem(dti2);
			
			
			sm.addTransaction(dt);
			sm.validate();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			System.out.println("PAIN_008_002_002");
			SEPAWriter sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_008_002_02);
			baos = (ByteArrayOutputStream) sw.export(sm);
			assertEquals("","");
			
			System.out.println(baos.toString());
			
			sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_008_001_02);
			baos = (ByteArrayOutputStream) sw.export(sm);
			System.out.println(baos.toString());
			
			sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_008_003_02);
			baos = (ByteArrayOutputStream) sw.export(sm);
			System.out.println(baos.toString());
			
			String outStr = baos.toString();
			SEPAAutoReader ar = new SEPAAutoReader();
			
			SEPAMessage smOut = ar.parse(outStr.getBytes());
			System.out.println(smOut.getTransactionsList().get(0).getCtlSum());
			
			System.out.println("Fertig!");
			

		}


}
