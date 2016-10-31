package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Reservation;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface ReservationService {

    Reservation find(long id);

    void persist(Reservation reservation);

    void merge(Reservation reservation);

    void delete(long id);

    List<Reservation> findAll();

}
