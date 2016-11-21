package cz.cvut.aos.airline.web.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author jakubchalupa
 * @since 21.11.16
 */
@JacksonXmlRootElement(localName = "reservation")
public class DiscoverResourcesWrapper extends ResourceSupport {

    //empty

}
