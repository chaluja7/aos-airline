package cz.cvut.aos.airline.service.rome2rio.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by sange on 24/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class R2RResource {

    ArrayList<R2RRoute> routes;

    public ArrayList<R2RRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<R2RRoute> routes) {
        this.routes = routes;
    }
}
