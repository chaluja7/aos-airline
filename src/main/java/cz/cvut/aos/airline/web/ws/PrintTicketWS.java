package cz.cvut.aos.airline.web.ws;


import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.ReservationService;
import org.apache.cxf.attachment.ByteDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.MTOM;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Bottom up ws - nemam zadne wsdl - to se naopak vygeneruje (z anotaci  zde)
 *
 * vygenerovane WSDL je na adrese /ws/downloadTicket?wsdl
 *
 * @author jakubchalupa
 * @since 08.12.16
 */
@MTOM
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PrintTicketWS {

    @Autowired
    private ReservationService reservationService;

    private static final String TICKET_CONTENT_TYPE = "text/plain";

    private static final String TICKET_FILE_APPENDIX = ".txt";

    @WebMethod(operationName = "downloadTicket")
    public DataHandler downloadTicket(@WebParam(name = "reservationId") long reservationId) throws IOException {
        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null || !StateChoices.PAID.equals(reservation.getState())) {
            //vytisknout muzu jen zaplacenou letenku
            throw new IllegalArgumentException("invalid reservationId");
        }

        ByteDataSource byteDataSource = new ByteDataSource(reservation.toString().getBytes(StandardCharsets.UTF_8), TICKET_CONTENT_TYPE);
        byteDataSource.setName(new StringBuilder("reservation_").append(reservation.getId()).append(TICKET_FILE_APPENDIX).toString());
        return new DataHandler(byteDataSource);
    }

}
