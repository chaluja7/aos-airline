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
@JacksonXmlRootElement(localName = "flight")
public class CreateFlightWrapper extends ResourceSupport {

    private String name;

    private String dateOfDeparture;

    private Double distance;

    private Integer seats;

    private Double price;

    private Long from;

    private Long to;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(String dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
