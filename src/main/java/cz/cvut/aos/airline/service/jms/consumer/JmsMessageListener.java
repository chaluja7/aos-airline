package cz.cvut.aos.airline.service.jms.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

/**
 * JMS consumer - stahne zpravu z fronty a posle email na danou adresu.
 *
 * @author jakubchalupa
 * @since 19.12.16
 */
@Service
public class JmsMessageListener {

    private static final String EMAIL_ADDRESS_ATTRIBUTE = "emailAddress";

    /**
     * @param message zprava z fronty aos-mail, v tele obsahuje soubor pro poslani (byte[]) a propertu emailAddress
     * pokud bych chtel odpovede do jine fronty staci vratit odpoved a pridat anotaci @SendTo("queue name")
     */
    @JmsListener(destination = "aos-mail")
    public void sendEmailToAddress(BytesMessage message) throws JMSException {
        final String emailAddress = message.getStringProperty(EMAIL_ADDRESS_ATTRIBUTE);

        //content je ve skutecnosti pouze String, nicmene pripadne je v nem samozrejme mozne posilat jakykoliv obsah (napr pdf soubor)
        byte[] content = new byte[(int) message.getBodyLength()];
        message.readBytes(content);
        final String messageBody = new String(content);

        if(!StringUtils.isEmpty(emailAddress) && !StringUtils.isEmpty(messageBody)) {
            //pred vypisem chvili pockam
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //jen vypis, spravne by se mel posilat mail (a napriklad i zkontrolovat validnost mailove adresy) :)
            System.out.println("-----------------");
            System.out.println("SENDING EMAIL TO: " + emailAddress);
            System.out.println("CONTENT: " + messageBody);
            System.out.println("-----------------");
        } else {
            System.out.println("-----------------");
            System.out.println("EMAIL COULD NOT BE SENT: empty email address or message content");
            System.out.println("-----------------");
        }
    }

}
