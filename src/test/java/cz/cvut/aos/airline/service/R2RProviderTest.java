package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import cz.cvut.aos.airline.service.rome2rio.R2RProvider;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sange on 24/11/2016.
 */
public class R2RProviderTest extends AbstractServiceTest {

    private static int RANGE = 20; // km

    @Autowired
    private R2RProvider r2RProvider;

    @Test
    public void testDistance() {
        String origin = "prague";
        String destination = "london";

        double actualDistance = r2RProvider.getDistance(origin, destination);
        int expectedDistance = 1085;
        Assert.assertTrue(isInAcceptableRange(expectedDistance, actualDistance));
    }

    private boolean isInAcceptableRange(int expectedDistance, double actualDistance) {
        return Math.abs(Math.round(actualDistance) - expectedDistance) <= RANGE;
    }

}
