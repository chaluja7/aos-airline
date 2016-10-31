package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Flight;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface FlightService {

    Flight find(long id);

    void persist(Flight flight);

    void merge(Flight flight);

    void delete(long id);

    List<Flight> findAll();

}
