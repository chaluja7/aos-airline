package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.entity.Destination;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public class DestinationServiceTest extends AbstractServiceTest {

    @Autowired
    private DestinationService destinationService;

    @Test
    public void testCRUD() {
        final Double lat = 108.5;
        final Double lat2 = 112.53;

        Destination destination = getNewDestination("testDestination", lat, 124.44);
        destinationService.persist(destination);

        Destination retrievedDestination = destinationService.find(destination.getId());
        Assert.assertNotNull(retrievedDestination);
        Assert.assertEquals(lat, retrievedDestination.getLat());

        retrievedDestination.setLat(lat2);
        destinationService.merge(retrievedDestination);

        retrievedDestination = destinationService.find(retrievedDestination.getId());
        Assert.assertEquals(lat2, retrievedDestination.getLat());

        destinationService.delete(retrievedDestination.getId());
        retrievedDestination = destinationService.find(retrievedDestination.getId());
        Assert.assertNull(retrievedDestination);
    }

    public static Destination getNewDestination(String name, Double lat, Double lon) {
        Destination destination = new Destination();
        destination.setName(name);
        destination.setLat(lat);
        destination.setLon(lon);

        return destination;
    }

}
