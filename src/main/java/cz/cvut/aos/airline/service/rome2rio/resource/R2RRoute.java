package cz.cvut.aos.airline.service.rome2rio.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by sange on 10/12/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class R2RRoute {

    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
