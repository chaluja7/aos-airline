package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface ReservationService {

    Reservation find(long id);

    void persist(Reservation reservation) throws NotEnoughSeatsException;

    void updateState(long id, StateChoices newState) throws InvalidStateChangeException;

    void delete(long id);

    List<Reservation> findAll();

}
