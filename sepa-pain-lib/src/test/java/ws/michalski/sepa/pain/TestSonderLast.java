package ws.michalski.sepa.pain;

import ws.michalski.sepa.pain.SEPAAutoReader;
import ws.michalski.sepa.pain.SEPAMessage;
import ws.michalski.sepa.pain.exeptions.SEPAException;
import ws.michalski.sepa.pain.exeptions.SEPASchemaException;

public class TestSonderLast {

	public static void main(String[] args) {

	   String xml =	"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.008.003.02\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:iso:std:iso:20022:tech:xsd:pain.008.003.02 pain.008.003.02.xsd\"><CstmrDrctDbtInitn><GrpHdr><MsgId>1401281340</MsgId><CreDtTm>2014-01-30T07:38:05.000Z</CreDtTm><NbOfTxs>1</NbOfTxs><CtrlSum>0.01</CtrlSum><InitgPty><Nm>Wahl GmbH</Nm></InitgPty></GrpHdr><PmtInf><PmtInfId>1401281340</PmtInfId><PmtMtd>DD</PmtMtd><NbOfTxs>1</NbOfTxs><CtrlSum>0.01</CtrlSum><PmtTpInf><SvcLvl><Cd>SEPA</Cd></SvcLvl><LclInstrm><Cd>CORE</Cd></LclInstrm><SeqTp>FRST</SeqTp></PmtTpInf><ReqdColltnDt>2014-02-07</ReqdColltnDt><Cdtr><Nm>Muster GmbH</Nm></Cdtr><CdtrAcct><Id><IBAN>DE09733317000000019551</IBAN></Id></CdtrAcct><CdtrAgt><FinInstnId><BIC>GABLDE71XXX</BIC></FinInstnId></CdtrAgt><ChrgBr>SLEV</ChrgBr><DrctDbtTxInf><PmtId><EndToEndId>1401281340-0000001</EndToEndId></PmtId><InstdAmt Ccy=\"EUR\">0.01</InstdAmt><DrctDbtTx><MndtRltdInf><MndtId>121896000001</MndtId><DtOfSgntr>2013-12-04</DtOfSgntr><AmdmntInd>false</AmdmntInd></MndtRltdInf><CdtrSchmeId><Id><PrvtId><Othr><Id>DE60ZZZ00000071348</Id><SchmeNm><Prtry>SEPA</Prtry></SchmeNm></Othr></PrvtId></Id></CdtrSchmeId></DrctDbtTx><DbtrAgt><FinInstnId><BIC>BYLADEM1ALG</BIC></FinInstnId></DbtrAgt><Dbtr><Nm>Wahl Andreas</Nm></Dbtr><DbtrAcct><Id><IBAN>DE28733500000000707877</IBAN></Id></DbtrAcct><RmtInf><Ustrd>KD 121896</Ustrd></RmtInf></DrctDbtTxInf></PmtInf></CstmrDrctDbtInitn></Document>";
	   
	   SEPAAutoReader ar = new SEPAAutoReader();

		try {
			
			SEPAMessage sm = ar.parse(xml.getBytes());
			
			
			System.out.println(sm.getCtlSum());
			
		} catch (SEPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SEPASchemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// PAIN-Typ der Umwandlung
		System.out.println(ar.getPainType());
		
		
	}

}
