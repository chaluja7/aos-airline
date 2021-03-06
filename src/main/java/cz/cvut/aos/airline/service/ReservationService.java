package cz.cvut.aos.airline.service;


import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.exception.InvalidReservationDeleteException;
import cz.cvut.aos.airline.service.exception.InvalidStateChangeException;
import cz.cvut.aos.airline.service.exception.NotEnoughSeatsException;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public interface ReservationService {

    Reservation find(long id);

    void persist(Reservation reservation) throws NotEnoughSeatsException;

    /**
     * zupdatuje stav reservace. Tim, ze je vynuceno i zadani stareho stavu se osetri to, pokud by ke zmene stavu doslo
     * pri soubehu vice pozadavku. Napr tedy neni mozne 2x zaplatit rezervaci apod.
     * @param id id rezervace
     * @param oldState stary stav rezervace
     * @param newState pozadovany novy stav rezervace
     * @throws InvalidStateChangeException
     */
    void updateState(long id, StateChoices oldState, StateChoices newState) throws InvalidStateChangeException;

    void delete(long id);

    void deleteWithStateControl(long id, StateChoices currentState) throws InvalidReservationDeleteException;

    Reservation findByIdAndPassword(long id, String password);

    List<Reservation> findAll();

}
