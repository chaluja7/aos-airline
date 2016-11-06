package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.service.exception.UnknownOrderColumnException;

import java.time.ZonedDateTime;
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

    List<Flight> find(ZonedDateTime departureFrom, ZonedDateTime departureTo, Integer start, Integer count, String orderColumn, boolean desc) throws UnknownOrderColumnException;

    /**
     * @return pocet vsech zaznamu flight v databazi
     */
    int countAll();

    int getNumberOfReservedSeats(long id);

}
