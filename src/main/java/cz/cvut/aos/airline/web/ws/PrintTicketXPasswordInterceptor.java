package cz.cvut.aos.airline.web.ws;

import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.service.ReservationService;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author jakubchalupa
 * @since 08.12.16
 */
public class PrintTicketXPasswordInterceptor extends AbstractSoapInterceptor {

    @Autowired
    private ReservationService reservationService;

    private static final String X_PASSWORD_HEADER = "x-password";

    public PrintTicketXPasswordInterceptor() {
        //interceptor chci spustit jeste pred vlastnim telem metody, ktera tiskne rezervaci
        super(Phase.PRE_LOGICAL);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        //ziskam ID rezervace, kterou chceme vytisknout
        List attributes = soapMessage.getContent(List.class); // message parts
        Long reservationId = (Long) attributes.get(0);

        //a ziskam x-password hlavicku
        Map<String, List<String>> headers = CastUtils.cast((Map) soapMessage.get(Message.PROTOCOL_HEADERS));
        if(headers.containsKey(X_PASSWORD_HEADER)) {
            List<String> list = headers.get(X_PASSWORD_HEADER);
            if(!list.isEmpty()) {
                String authHeader = list.get(0);

                Reservation reservation = reservationService.findByIdAndPassword(reservationId, authHeader);
                if(reservation != null) {
                    //ok spravna hlavicka
                    return;
                }
            }
        }

        throw new RuntimeException("invalid reservation or invalid x-password header");
    }

}
