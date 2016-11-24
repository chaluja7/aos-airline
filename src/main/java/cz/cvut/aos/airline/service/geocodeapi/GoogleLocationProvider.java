package cz.cvut.aos.airline.service.geocodeapi;

import cz.cvut.aos.airline.service.geocodeapi.resource.GoogleLocationResource;
import cz.cvut.aos.airline.service.geocodeapi.resource.GoogleResult;
import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by sange on 24/11/2016.
 */


@Service
public class GoogleLocationProvider {

    @Autowired
    protected GoogleLocationManager googleLocationManager;


    /**
     * @param addresss
     * @return  lokace | null (= nenalezena nebo vice lokaci)
     */
    public Location getLocationFromAddress(String addresss) {

        GoogleLocationResource resource = googleLocationManager.getGoogleLocation(addresss);
        if (resource != null && resource.getResults() != null && !resource.getResults().isEmpty()) {
            return resource.getResults().get(0).getGeometry().getLocation();
        }

        return null;
    }

}
