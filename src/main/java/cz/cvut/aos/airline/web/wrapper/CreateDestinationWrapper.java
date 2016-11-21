package cz.cvut.aos.airline.web.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.hateoas.ResourceSupport;

/**
 * Diky JacksonXmlRootElement anotaci se budou property i do xml serializovat dle @Json anotaci
 * https://github.com/FasterXML/jackson-dataformat-xml
 *
 * @author jakubchalupa
 * @since 22.10.16
 */
@JacksonXmlRootElement(localName = "destination")
public class CreateDestinationWrapper extends ResourceSupport {

    private String name;

    //takto nyni prejmenuji atribut pro json i xml
    //@JsonProperty("latitude")
    private Double lat;

    //@JsonProperty("longitude")
    private Double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
