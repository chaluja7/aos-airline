package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.InvalidReservationDeleteException;
import cz.cvut.aos.airline.service.InvalidStateChangeException;
import org.springframework.stereotype.Repository;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Repository
@SuppressWarnings("JpaQlInspection")
public class ReservationDao extends AbstractGenericHibernateDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    public void updateState(long id, StateChoices oldState, StateChoices newState) throws InvalidStateChangeException {
        int numberOfUpdated = sessionFactory.getCurrentSession()
            .createQuery("update Reservation set state = :newState where id = :id and state = :oldState")
            .setParameter("newState", newState)
            .setParameter("id", id)
            .setParameter("oldState", oldState)
            .executeUpdate();

        if(numberOfUpdated != 1) {
            //osetruje napriklad dvojnasobne uhrazeni apod.
            throw new InvalidStateChangeException();
        }
    }

    public void deleteWithStateControl(long id, StateChoices currentState) throws InvalidReservationDeleteException {
        int numberOfDeleted = sessionFactory.getCurrentSession()
            .createQuery("delete from Reservation where id = :id and state = :currentState")
            .setParameter("id", id)
            .setParameter("currentState", currentState)
            .executeUpdate();

        if(numberOfDeleted != 1) {
            //snazim se smazat rezervaci, ale mezi tim se ji zmenil stav
            throw new InvalidReservationDeleteException();
        }
    }
}
