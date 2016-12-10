package cz.cvut.aos.airline.service.rome2rio;

import cz.cvut.aos.airline.service.rome2rio.resource.R2RResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sange on 24/11/2016.
 */
@Service
public class R2RProvider {

    public static double FALSE_DISTANCE = -1;

    @Autowired
    private R2RManager r2RManager;

    public double getDistance(String origin, String destination) {

        R2RResource resource = r2RManager.getR2RFlightDistance(origin, destination);
        if (resource != null && resource.getRoutes() != null && !resource.getRoutes().isEmpty()) {
            return resource.getRoutes().get(0).getDistance();
        }

        return FALSE_DISTANCE;
    }
}
