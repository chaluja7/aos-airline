package cz.cvut.aos.airline.service.rome2rio;

import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sange on 24/11/2016.
 */
@Service
public class R2RProvider {

    @Autowired
    private R2RManager r2RManager;

    public double getDistance(Location origin, Location destination) {
//        return r2RManager.getR2RDistance(origin, destination);
        return (r2RManager.getR2RDistanceTest(origin, destination));
    }
}
