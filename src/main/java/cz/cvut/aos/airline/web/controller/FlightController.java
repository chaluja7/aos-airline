package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.service.DestinationService;
import cz.cvut.aos.airline.service.FlightService;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.CreateFlightWrapper;
import cz.cvut.aos.airline.web.wrapper.FlightWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@RestController
public class FlightController extends AbstractController {

    private static final String PATH = "/flight";

    @Autowired
    private FlightService flightService;

    @Autowired
    private DestinationService destinationService;

    @RequestMapping(value = PATH + "/{flightId}", method = RequestMethod.GET)
    public FlightWrapper getFlight(@PathVariable Long flightId) {
        FlightWrapper flight = getFlightWrapper(flightService.find(flightId));
        if(flight == null) {
            throw new ResourceNotFoundException();
        }

        return flight;
    }

    @RequestMapping(value = PATH, method = RequestMethod.GET)
    public List<FlightWrapper> getFlights() {
        List<FlightWrapper> list = new ArrayList<>();
        for(Flight flight : flightService.findAll()) {
            list.add(getFlightWrapper(flight));
        }

        return list;
    }

    @RequestMapping(value = PATH, method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createFlight(@RequestBody CreateFlightWrapper wrapper) {
        if(wrapper == null) {
            throw new BadRequestException();
        }

        Flight flight = getFlightFromWrapper(wrapper);
        try {
            flightService.persist(flight);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }

        return getResponseCreated(getFlightWrapper(flightService.find(flight.getId())), getResourceDestination(flight.getId()));
    }

    @RequestMapping(value = PATH + "/{flightId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateFlight(@PathVariable Long flightId, @RequestBody CreateFlightWrapper wrapper) {
        if(flightService.find(flightId) == null) {
            throw new ResourceNotFoundException();
        }

        if(wrapper == null) {
            throw new BadRequestException();
        }

        Flight flight = getFlightFromWrapper(wrapper);
        flight.setId(flightId);
        try {
            flightService.merge(flight);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = PATH + "/{flightId}", method = RequestMethod.DELETE)
    public void deleteFlight(@PathVariable Long flightId) {
        Flight flight = flightService.find(flightId);
        if(flight == null) {
            //OK takova destinace neni v DB
            return;
        }

        flightService.delete(flight.getId());
    }

    private FlightWrapper getFlightWrapper(Flight flight) {
        if(flight == null) return null;

        FlightWrapper wrapper = new FlightWrapper();
        wrapper.setId(flight.getId());
        wrapper.setName(flight.getName());
        wrapper.setDistance(flight.getDistance());
        wrapper.setSeats(flight.getSeats());
        wrapper.setPrice(flight.getPrice());

        if(flight.getDateOfDeparture() != null) {
            wrapper.setDateOfDeparture(flight.getDateOfDeparture().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        if(flight.getFrom() != null) {
            wrapper.setFrom(flight.getFrom().getId());
            wrapper.setFromUrl(DestinationController.getResourceDestination(flight.getFrom().getId()));
        }

        if(flight.getTo() != null) {
            wrapper.setTo(flight.getTo().getId());
            wrapper.setToUrl(DestinationController.getResourceDestination(flight.getTo().getId()));
        }

        if(flight.getId() != null) {
            wrapper.setUrl(getResourceDestination(flight.getId()));
        }

        return wrapper;
    }

    private Flight getFlightFromWrapper(CreateFlightWrapper wrapper) {
        if(wrapper == null) return null;

        Flight flight = new Flight();
        flight.setName(wrapper.getName());
        flight.setDistance(wrapper.getDistance());
        flight.setSeats(wrapper.getSeats());
        flight.setPrice(wrapper.getPrice());

        if(wrapper.getDateOfDeparture() != null) {
            flight.setDateOfDeparture(ZonedDateTime.parse(wrapper.getDateOfDeparture(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        if(wrapper.getFrom() != null) {
            flight.setFrom(destinationService.find(wrapper.getFrom()));
            if(flight.getFrom() == null) {
                throw new BadRequestException();
            }
        }

        if(wrapper.getTo() != null) {
            flight.setTo(destinationService.find(wrapper.getTo()));
            if(flight.getTo() == null) {
                throw new BadRequestException();
            }
        }

        return flight;
    }

    public static String getResourceDestination(Long id) {
        return PATH + "/" + id;
    }
}
