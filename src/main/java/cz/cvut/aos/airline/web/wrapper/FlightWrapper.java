package cz.cvut.aos.airline.web.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
public class FlightWrapper extends CreateFlightWrapper implements CommonWrappable {

    @JsonProperty("id")
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
