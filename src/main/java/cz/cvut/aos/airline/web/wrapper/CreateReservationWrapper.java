package cz.cvut.aos.airline.web.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.hateoas.ResourceSupport;

/**
 * Diky JacksonXmlRootElement anotaci se budou property i do xml serializovat dle @Json anotaci
 * https://github.com/FasterXML/jackson-dataformat-xml
 *
 * @author jakubchalupa
 * @since 31.10.16
 */
@JacksonXmlRootElement(localName = "reservation")
public class CreateReservationWrapper extends ResourceSupport {

    private Long flight;

    private Integer seats;

    public Long getFlight() {
        return flight;
    }

    public void setFlight(Long flight) {
        this.flight = flight;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
