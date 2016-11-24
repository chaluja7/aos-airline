package cz.cvut.aos.airline.service.geocodeapi.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sange on 24/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleResult {

    GeometryResult geometry;
    String formatted_address;

    public GeometryResult getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryResult geometry) {
        this.geometry = geometry;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
