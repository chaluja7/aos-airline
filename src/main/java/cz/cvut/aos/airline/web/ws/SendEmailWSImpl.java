package cz.cvut.aos.airline.web.ws;


import cz.cvut.aos.airline.generated.ws.SendEmailWS;
import cz.cvut.aos.airline.service.jms.producer.JmsMessageSender;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Top down ws - mam wsdl (sendTicketToEmail.wsdl) - z toho vygeneruji pomoci jaxws-maven-plugin java tridy
 * zde pak pouze napimplementuji logiku.
 *
 * WSDL je take k dispozici na adrese /ws/sendTicketToEmail?wsdl
 *
 * @author jakubchalupa
 * @since 08.12.16
 */
public class SendEmailWSImpl implements SendEmailWS {

    @Autowired
    protected JmsMessageSender jmsMessageSender;

    @Override
    public void sendTicketToEmail(long reservationId, String emailAddress) {
        jmsMessageSender.sendReservationToEmailQueue(reservationId, emailAddress);
    }

}
