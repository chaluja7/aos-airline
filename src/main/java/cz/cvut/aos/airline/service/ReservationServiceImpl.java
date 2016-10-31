package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.dao.ReservationDao;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private FlightService flightService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Reservation find(long id) {
        return reservationDao.find(id);
    }

    @Override
    @Transactional
    public void persist(Reservation reservation) throws NotEnoughSeatsException {
        //musim zjistit, zda mam dost volnych mist
        if(reservation.getFlight() == null || reservation.getSeats() == null || reservation.getSeats() <= 0) throw new IllegalStateException();

        final int totalNumberOfSeats = reservation.getFlight().getSeats() != null ? reservation.getFlight().getSeats() : 0;
        final int currentNumberOfSeats = reservation.getSeats() + flightService.getNumberOfReservedSeats(reservation.getFlight().getId());
        if(currentNumberOfSeats > totalNumberOfSeats) {
            //pri soubezne vicero rezeravci se zde muze stat, ze by volna mista pretekla, protoze probehla jina rezervace,
            // v ramci teto app nereseno ale vime o tom. Vyresil by napr. dodatecny constraint na databazi nebo lock/synchronized block.
            throw new NotEnoughSeatsException();
        }

        reservation.setCreated(ZonedDateTime.now());
        reservation.setState(StateChoices.NEW);
        reservation.setPassword(UUID.randomUUID().toString());

        reservationDao.persist(reservation);
    }

    @Override
    @Transactional
    public void updateState(long id, StateChoices newState) throws InvalidStateChangeException {
        Reservation reservation = find(id);
        if(reservation == null || newState == null) {
            throw new IllegalArgumentException();
        }

        reservationDao.updateState(id, newState);
    }

    @Override
    @Transactional
    public void delete(long id) {
        reservationDao.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}
