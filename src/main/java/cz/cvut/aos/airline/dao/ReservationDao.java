package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
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

    public void updateState(long id, StateChoices newState) {
        sessionFactory.getCurrentSession()
            .createQuery("update Reservation set state = :newState where id = :id")
            .setParameter("newState", newState)
            .setParameter("id", id)
            .executeUpdate();
    }

}
