package cz.cvut.aos.airline.web.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Diky JacksonXmlRootElement anotaci se budou property i do xml serializovat dle @Json anotaci
 * https://github.com/FasterXML/jackson-dataformat-xml
 *
 * @author jakubchalupa
 * @since 22.10.16
 */
@JacksonXmlRootElement(localName = "destination")
public class DestinationWrapper extends CreateDestinationWrapper implements CommonWrappable {

    private Long entityId;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
