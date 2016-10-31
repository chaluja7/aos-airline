package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.entity.Flight;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public class FlightServiceTest extends AbstractServiceTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private DestinationService destinationService;

    @Test
    public void testCRUD() {
        final String name1 = "Flight1";
        final String name2 = "Flight111";

        Flight flight = getNewFlight(name1, 50.5, 50, 100.6, new Date());
        destinationService.persist(flight.getFrom());
        destinationService.persist(flight.getTo());
        flightService.persist(flight);

        Flight retrievedFlight = flightService.find(flight.getId());
        Assert.assertNotNull(retrievedFlight);
        Assert.assertNotNull(retrievedFlight.getFrom());
        Assert.assertNotNull(retrievedFlight.getTo());
        Assert.assertEquals(name1, retrievedFlight.getName());


        Destination newDestination = DestinationServiceTest.getNewDestination("novaDestinace", 100.6, 50.6);
        destinationService.persist(newDestination);

        retrievedFlight.setName(name2);
        retrievedFlight.setTo(newDestination);
        flightService.merge(retrievedFlight);

        retrievedFlight = flightService.find(retrievedFlight.getId());
        Assert.assertNotNull(retrievedFlight);
        Assert.assertNotNull(retrievedFlight.getTo());
        Assert.assertEquals(name2, retrievedFlight.getName());

        flightService.delete(retrievedFlight.getId());
        retrievedFlight = flightService.find(retrievedFlight.getId());
        Assert.assertNull(retrievedFlight);
    }

    public static Flight getNewFlight(String name, Double price, Integer seats, Double distance, Date dateOfDeparture) {
        Flight flight = new Flight();

        flight.setName(name);
        flight.setPrice(price);
        flight.setSeats(seats);
        flight.setDistance(distance);
        flight.setDateOfDeparture(dateOfDeparture);

        Destination destinationFrom = DestinationServiceTest.getNewDestination(UUID.randomUUID().toString(), 50.36, 124.44);
        Destination destinationTo = DestinationServiceTest.getNewDestination(UUID.randomUUID().toString(), 45.36, 90.44);

        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);

        return flight;
    }

}
