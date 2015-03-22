package ws.michalski.sepa.pain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import ws.michalski.sepa.pain.SEPAAutoReader;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.exeptions.SEPASchemaException;

public class TestAutoReader {

	//@Test
	public void autoReader() throws FileNotFoundException, SEPAException,
			SEPASchemaException {

		InputStream is = new FileInputStream("x.xml");

		// Gelesen wird mit dem AutoReader. Möglich wäre auch zu Fuß....
		SEPAAutoReader ar = new SEPAAutoReader();

		SEPAMessage sm = ar.parse(is);

		// PAIN-Typ der Umwandlung
		System.out.println(ar.getPainType());

		// Für fortgeschrittene
		iso.std.iso._20022.tech.xsd.pain_001_002_03.Document doc = (iso.std.iso._20022.tech.xsd.pain_001_002_03.Document) ar
				.getRoot();
		System.out.println(doc.getCstmrCdtTrfInitn().getGrpHdr().getCtrlSum());

		System.out.println(sm.getMessageId());
		if (sm.hasCreditTransaction()) {
			System.out.println("Es handelt sich um Überweisung!");
		}
		if (sm.hasDebitTransaction()) {
			System.out.println("Es handelt sich um Lastschrift!");
		}

		System.out.println("Paintype vom root ist " + ar.getPainType());
	}

}
