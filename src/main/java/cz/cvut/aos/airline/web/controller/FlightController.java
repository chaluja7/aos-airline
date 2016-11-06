package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.service.DestinationService;
import cz.cvut.aos.airline.service.FlightService;
import cz.cvut.aos.airline.service.exception.UnknownOrderColumnException;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.CreateFlightWrapper;
import cz.cvut.aos.airline.web.wrapper.FlightWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<FlightWrapper>> getFlights(@RequestHeader(value = X_BASE_HEADER, required = false) Integer xBase,
                                                          @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                                          @RequestHeader(value = X_ORDER, required = false) String xOrder,
                                                          @RequestHeader(value = X_FILTER, required = false) String xFilter) {

        final int totalNumberOfFlights = flightService.countAll();

        //limit
        final int start = getListStartValueFromHeader(xBase);
        final int tmpCount = getListCountValueFromHeader(xOffset);
        final int count = tmpCount >= 0 ? tmpCount : totalNumberOfFlights;

        //razeni
        final OrderHeaderValue listOrderValueFromHeader = getListOrderValueFromHeader(xOrder);

        //filtrovani
        final FilterHeaderValue listFilterValueFromHeader = getListFilterValueFromHeader(xFilter);

        List<FlightWrapper> list = new ArrayList<>();
        if(count > 0) {
            try {
                List<Flight> flights = flightService.find(listFilterValueFromHeader.getDepartureFrom(), listFilterValueFromHeader.getDepartureTo(),
                                                          start, count, listOrderValueFromHeader.getColumn(), listOrderValueFromHeader.isDesc());
                for(Flight flight : flights) {
                    list.add(getFlightWrapper(flight));
                }
            } catch(UnknownOrderColumnException unknownOrderColumnException) {
                throw new BadRequestException();
            }
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_COUNT_HEADER, totalNumberOfFlights + "");
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
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

    /**
     * @param xBase xBase hlavicka
     * @return rozhodujici hodnotu z hlavicky xBase
     */
    private int getListStartValueFromHeader(Integer xBase) {
        if(xBase != null && xBase > 0) {
            return xBase - 1;
        }

        return 0;
    }

    /**
     * @param xOffset xOffset hlavicka
     * @return rozhodujici hodnotu z hlavicky xOffset
     */
    private int getListCountValueFromHeader(Integer xOffset) {
        if(xOffset != null && xOffset >= 0) {
            return xOffset;
        }

        return -1;
    }

    /**
     * @param xOrder xOrder hlavicka
     * @return rozhodujici hodnoty z hlavicky xOrder
     */
    private OrderHeaderValue getListOrderValueFromHeader(String xOrder) {
        String orderColumn = null;
        boolean desc = false;

        if(xOrder != null) {
            //oddeleni "sloupec":desc
            if(xOrder.contains(":")) {
                String[] split = xOrder.split(":");
                if(split.length == 2) {
                    orderColumn = split[0];
                    desc = "desc".equalsIgnoreCase(split[1]);
                } else {
                    throw new BadRequestException();
                }
            } else {
                orderColumn = xOrder;
                desc = false;
            }
        }

        return new OrderHeaderValue(orderColumn, desc);
    }

    /**
     * @param xFilter xFilter hlavicka
     * @return rozhodujici hodnoty z hlavicky xFilter
     */
    private FilterHeaderValue getListFilterValueFromHeader(String xFilter) {
        ZonedDateTime departureFrom = null;
        ZonedDateTime departureTo = null;

        if(xFilter != null) {
            List<String> simpleValues = new ArrayList<>();
            if(xFilter.contains(",")) {
                //jsou tam oba datumy
                String[] split = xFilter.split(",");
                if(split.length == 2) {
                    simpleValues.add(split[0]);
                    simpleValues.add(split[1]);
                } else {
                    throw new BadRequestException();
                }
            } else {
                //je tam jen jeden datum
                simpleValues.add(xFilter);
            }

            //v simpleValues mam nyni klic=hodnota hodnoty
            for(String simpleValue : simpleValues) {
                if(!simpleValue.contains("=")) {
                    throw new BadRequestException();
                }

                String[] split = simpleValue.split("=");
                if(split.length != 2) {
                    throw new BadRequestException();
                }

                switch(split[0]) {
                    case "dateOfDepartureFrom":
                        departureFrom = ZonedDateTime.parse(split[1], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        break;
                    case "dateOfDepartureTo":
                        departureTo = ZonedDateTime.parse(split[1], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        break;
                    default:
                        throw new BadRequestException();
                }
            }
        }

        return new FilterHeaderValue(departureFrom, departureTo);
    }

    /**
     * pomocna trida pro hodnoty hlavicky X-Order
     */
    private class OrderHeaderValue {

        private final String column;

        private final boolean desc;

        public OrderHeaderValue(String column, boolean desc) {
            this.column = column;
            this.desc = desc;
        }

        public String getColumn() {
            return column;
        }

        public boolean isDesc() {
            return desc;
        }
    }

    /**
     * pomocna trid pro hodnoty hlavicky X-Filter
     */
    private class FilterHeaderValue {

        private final ZonedDateTime departureFrom;

        private final ZonedDateTime departureTo;

        public FilterHeaderValue(ZonedDateTime departureFrom, ZonedDateTime departureTo) {
            this.departureFrom = departureFrom;
            this.departureTo = departureTo;
        }

        public ZonedDateTime getDepartureFrom() {
            return departureFrom;
        }

        public ZonedDateTime getDepartureTo() {
            return departureTo;
        }
    }
}
