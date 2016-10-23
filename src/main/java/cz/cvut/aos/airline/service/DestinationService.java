package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Destination;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface DestinationService {

    Destination find(long id);

    void persist(Destination destination);

    void merge(Destination destination);

    void delete(long id);

    List<Destination> findAll();

}
