package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.dao.ReservationDao;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Reservation find(long id) {
        return reservationDao.find(id);
    }

    @Override
    @Transactional
    public void persist(Reservation reservation) {
        reservation.setCreated(new Date());
        reservation.setState(StateChoices.NEW);
        reservation.setPassword(UUID.randomUUID().toString());

        reservationDao.persist(reservation);
    }

    @Override
    @Transactional
    public void merge(Reservation reservation) {
        reservationDao.merge(reservation);
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
