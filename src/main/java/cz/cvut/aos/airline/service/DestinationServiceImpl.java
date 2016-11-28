package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.dao.DestinationDao;
import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.service.exception.UnknownLocationNameException;
import cz.cvut.aos.airline.service.geocodeapi.GoogleLocationProvider;
import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Service
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationDao destinationDao;

    @Autowired
    private GoogleLocationProvider googleLocationProvider;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Destination find(long id) {
        return destinationDao.find(id);
    }

    @Override
    @Transactional
    public void persist(Destination destination) throws UnknownLocationNameException {
        fillLocationToDestination(destination);
        destinationDao.persist(destination);
    }

    @Override
    @Transactional
    public void merge(Destination destination) throws UnknownLocationNameException {
        fillLocationToDestination(destination);
        destinationDao.merge(destination);
    }

    @Override
    @Transactional
    public void delete(long id) {
        destinationDao.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Destination> findAll() {
        return destinationDao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Destination> findAll(boolean desc) {
        return destinationDao.findAll(desc);
    }

    private void fillLocationToDestination(Destination destination) throws UnknownLocationNameException {
        Location location = googleLocationProvider.getLocationFromAddress(destination.getName());
        //pokud je location null tak se nic nevyplni

        if(location != null) {
            destination.setLat(location.getLat());
            destination.setLon(location.getLng());
        }
    }
}
