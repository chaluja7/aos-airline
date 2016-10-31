package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Reservation;
import org.springframework.stereotype.Repository;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Repository
public class ReservationDao extends AbstractGenericHibernateDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

}
