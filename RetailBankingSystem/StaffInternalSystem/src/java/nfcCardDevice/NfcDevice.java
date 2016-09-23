/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfcCardDevice;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author wang
 */
public class NfcDevice {

    public static void main(String[] args) {
        System.out.println("*** Start nfc device ***");
        try {
            CardChannel channel = initializeDevice();
            writeCard(channel, "00010203040506070809000102030405"); //32 digit
            readCard(channel);
            System.out.println();
        } catch (Exception ex) {
            System.out.println("error" + ex);
        }
    }

    public static String writeCard(CardChannel channel, String inputCreditCardNumber) { //16 digit
        ResponseAPDU answer = null;
        try {
            //write value
            String apdu = "FFD6000510" + inputCreditCardNumber;
            answer = channel.transmit(new CommandAPDU(toByteArray(apdu)));
            System.out.println("Write Result: " + answer.toString());

        } catch (Exception ex) {
            System.out.println("NfcDevice.writeCard() write Card Error");
        }
        return answer.toString();
    }

    public static String readCard(CardChannel channel) {
        String result = "";
        try {
            String apdu = "FFB0000510";
            ResponseAPDU answer = channel.transmit(new CommandAPDU(toByteArray(apdu)));
            System.out.println("Read Result: " + answer.toString());
            byte r[] = answer.getData();
            for (int i = 0; i < 16; i++) {
                
                System.out.print(r[i]);
                result += r[i];
            }
        } catch (Exception ex) {
            System.out.println("NfcDevice.readCard() Read Card Error");
        }
        return result;
    }

    public static CardChannel initializeDevice() {
        CardChannel channel = null;
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals: " + terminals);

            // Use the first terminal
            CardTerminal terminal = terminals.get(0);

            // Connect with the card
            Card card = terminal.connect("*");
            System.out.println("card: " + card);
            channel = card.getBasicChannel();
//
//            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xFF, 0xCA, 0x00, 0x00, 0x05));
//            System.out.println("answer: " + answer.toString());

            String apdu = "FF82000006FFFFFFFFFFFF"; //also u can append strings into apdu
            ResponseAPDU answer = channel.transmit(new CommandAPDU(toByteArray(apdu)));
            System.out.println("Load Authentication Key Result: " + answer.toString());

            apdu = "FF860000050100046000";
            answer = channel.transmit(new CommandAPDU(toByteArray(apdu)));
            System.out.println("Authenticate Result: " + answer.toString());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return channel;

    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
}
