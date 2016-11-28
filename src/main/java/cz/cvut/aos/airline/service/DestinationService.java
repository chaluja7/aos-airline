package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.service.exception.UnknownLocationNameException;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface DestinationService {

    Destination find(long id);

    void persist(Destination destination) throws UnknownLocationNameException;

    void merge(Destination destination) throws UnknownLocationNameException;

    void delete(long id);

    List<Destination> findAll();

    List<Destination> findAll(boolean desc);

}
