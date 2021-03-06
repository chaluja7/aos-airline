package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.service.exception.UnknownLocationNameException;
import cz.cvut.aos.airline.service.exception.UnknownOrderColumnException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public void testCRUD() throws UnknownLocationNameException {
        final String name1 = "Flight1";
        final String name2 = "Flight111";

        Flight flight = getNewFlight(name1, 50.5, 50, 89.6, ZonedDateTime.now());
        destinationService.persist(flight.getFrom());
        destinationService.persist(flight.getTo());
        flightService.persist(flight);

        Flight retrievedFlight = flightService.find(flight.getId());
        Assert.assertNotNull(retrievedFlight);
        Assert.assertNotNull(retrievedFlight.getFrom());
        Assert.assertNotNull(retrievedFlight.getTo());
        Assert.assertEquals(name1, retrievedFlight.getName());


        Destination newDestination = DestinationServiceTest.getNewDestination("New York", 89.6, 50.6);
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

    @Test
    public void testCountAll() {
        int i = flightService.countAll();
        Assert.assertTrue(i >= 0);
    }

    @Test
    public void testFind() throws UnknownOrderColumnException {
        ZonedDateTime departureFrom = ZonedDateTime.parse("2012-10-24T11:15:41+02:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime departureTo = ZonedDateTime.parse("2013-10-24T11:15:41+02:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        List<Flight> flights = flightService.find(departureFrom, departureTo, 0, 4, "dateOfDeparture", false);
        Assert.assertNotNull(flights);
    }

    public static Flight getNewFlight(String name, Double price, Integer seats, Double distance, ZonedDateTime dateOfDeparture) {
        Flight flight = new Flight();

        flight.setName(name);
        flight.setPrice(price);
        flight.setSeats(seats);
        flight.setDistance(distance);
        flight.setDateOfDeparture(dateOfDeparture);

        Destination destinationFrom = DestinationServiceTest.getNewDestination(UUID.randomUUID().toString(), 50.36, 84.44);
        Destination destinationTo = DestinationServiceTest.getNewDestination(UUID.randomUUID().toString(), 45.36, 89.44);

        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);

        return flight;
    }

}
