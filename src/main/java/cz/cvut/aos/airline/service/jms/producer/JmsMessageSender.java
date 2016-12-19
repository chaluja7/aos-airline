package cz.cvut.aos.airline.service.jms.producer;

import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.ReservationService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.BytesMessage;

/**
 * JMS producer - posle do fronty pozadavek na zaslani mailu na adresu.
 *
 * @author jakubchalupa
 * @since 19.12.16
 */
@Service
public class JmsMessageSender {

    @Autowired
    protected JmsTemplate jmsTemplate;

    @Autowired
    protected ReservationService reservationService;

    private static final String EMAIL_ADDRESS_ATTRIBUTE = "emailAddress";

    /**
     * send request to send reservation via email
     * @param reservationId id of reservation to send via email
     * @param emailAddress email address to send reservation
     * @throws IllegalArgumentException if reservation or email is invalid
     */
    public void sendReservationToEmailQueue(final long reservationId, final String emailAddress) throws IllegalArgumentException {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(emailAddress)) {
            throw new IllegalArgumentException("invalid email");
        }

        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null || !StateChoices.PAID.equals(reservation.getState())) {
            //poslat mailem muzu jen zaplacenou letenku
            throw new IllegalArgumentException("invalid reservationId");
        }

        MessageCreator messageCreator = session -> {
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.setStringProperty(EMAIL_ADDRESS_ATTRIBUTE, emailAddress);

            //zde zdanlive prebytecne pisu string jako byte[] (mohl bych poslat rovnou String)
            //timto ale simuluji, ze umim pripadne poslat jakykoliv vygenerovany soubor (napr pdf)
            bytesMessage.writeBytes(reservation.toString().getBytes());
            return bytesMessage;
        };

        this.jmsTemplate.send(messageCreator);
    }

}
