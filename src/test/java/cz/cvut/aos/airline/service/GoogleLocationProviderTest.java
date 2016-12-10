package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.service.geocodeapi.GoogleLocationProvider;
import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sange on 24/11/2016.
 */
public class GoogleLocationProviderTest extends AbstractServiceTest {

    @Autowired
    private GoogleLocationProvider googleLocationProvider;

    @Test
    public void testOneLocation() {
        String testAddress = "prague";
        Location expectedLocation = new Location(50.0755381, 14.4378005);
        Location actualLocation = googleLocationProvider.getLocationFromAddress(testAddress);
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testNoLocation() {
        String testAddress = "sdgsdfdfgfd";
        Location actualLocation = googleLocationProvider.getLocationFromAddress(testAddress);
        Assert.assertNull(actualLocation);
    }



}
