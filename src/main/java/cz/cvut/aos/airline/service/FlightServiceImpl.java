package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.dao.FlightDao;
import cz.cvut.aos.airline.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Flight find(long id) {
        return flightDao.find(id);
    }

    @Override
    @Transactional
    public void persist(Flight flight) {
        flightDao.persist(flight);
    }

    @Override
    @Transactional
    public void merge(Flight flight) {
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
}
