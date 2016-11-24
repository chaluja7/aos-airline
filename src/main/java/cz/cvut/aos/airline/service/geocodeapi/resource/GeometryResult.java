package cz.cvut.aos.airline.service.geocodeapi.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sange on 24/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GeometryResult {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
