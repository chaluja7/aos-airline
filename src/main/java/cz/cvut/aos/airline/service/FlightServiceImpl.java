package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.dao.FlightDao;
import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.service.exception.UnknownOrderColumnException;
import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import cz.cvut.aos.airline.service.rome2rio.R2RProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Autowired
    private R2RProvider r2RProvider;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Flight find(long id) {
        return flightDao.find(id);
    }

    @Override
    @Transactional
    public void persist(Flight flight) {
        fillDistanceAndPriceToFlight(flight);
        flightDao.persist(flight);
    }

    @Override
    @Transactional
    public void merge(Flight flight) {
        fillDistanceAndPriceToFlight(flight);
        flightDao.merge(flight);
    }

    @Override
    @Transactional
    public void delete(long id) {
        flightDao.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Flight> findAll() {
        return flightDao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Flight> find(ZonedDateTime departureFrom, ZonedDateTime departureTo, Integer start, Integer count, String orderColumn, boolean desc) throws UnknownOrderColumnException {
        return flightDao.find(departureFrom, departureTo, start, count, orderColumn, desc);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int countAll() {
        return flightDao.countAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getNumberOfReservedSeats(long id) {
        return flightDao.getNumberOfReservedSeats(id);
    }

    private void fillDistanceAndPriceToFlight(Flight flight) {
        Location origin = new Location(flight.getFrom().getLat(), flight.getFrom().getLon());
        Location destination = new Location(flight.getTo().getLat(), flight.getTo().getLon());

        double distance = r2RProvider.getDistance(origin, destination);
        double price = getPrice(distance);

        flight.setDistance(distance);
        flight.setPrice(price);
    }

    private double getPrice(double distance) {
        return distance * 10; // 100km = 1000Kc
    }
}
