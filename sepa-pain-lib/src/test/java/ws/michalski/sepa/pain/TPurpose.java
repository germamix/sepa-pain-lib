package ws.michalski.sepa.pain;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.junit.Test;

import ws.michalski.sepa.pain.CreditTransaction;
import ws.michalski.sepa.pain.CreditTransactionItem;
import ws.michalski.sepa.pain.SEPAAutoReader;
import ws.michalski.sepa.pain.SEPAFactory;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.Purpose.PurposeCode;
import ws.michalski.sepa.pain.interfaces.SEPAWriter;

public class TPurpose {

	@Test
	public void test() throws Exception {
		
		SEPAMessage sm = new SEPAMessage();
		
		// Debitor Daten (es können mehrere Debitoren pro SEPA Nachricht sein)
		CreditTransaction ct = new CreditTransaction()
				.setName("Markus Michalski")
				.setBIC("LENZDEM1XXX")
				.setIBAN("DE25701307000001013416")
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
		
		
		System.out.println(sm.toString());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		System.out.println("PAIN_001_002_003");
		SEPAWriter sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_001_002_03);
		baos = (ByteArrayOutputStream) sw.export(sm);
		System.out.println(baos.toString());
		
		System.out.println("PAIN_001_003_003");
		sw = SEPAFactory.getSEPAWriter(SEPAFactory.PainType.PAIN_001_003_03);
		baos = (ByteArrayOutputStream) sw.export(sm);
		
		System.out.println(baos.toString());
		
		
		
		String outStr = baos.toString();
		
		SEPAAutoReader ar = new SEPAAutoReader();

		SEPAMessage smOut = ar.parse(outStr.getBytes());
		System.out.println(smOut.getTransactionsList().get(0).getCtlSum());
		
		
		System.out.println("Fertig!");
		
		
	}

}
