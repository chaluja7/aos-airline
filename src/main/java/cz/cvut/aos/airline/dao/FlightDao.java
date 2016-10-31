package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.entity.StateChoices;
import org.springframework.stereotype.Repository;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Repository
@SuppressWarnings("JpaQlInspection")
public class FlightDao extends AbstractGenericHibernateDao<Flight> {

    public FlightDao() {
        super(Flight.class);
    }

    public int getNumberOfReservedSeats(long id) {
        Long l = (Long) sessionFactory.getCurrentSession()
                                .createQuery("select sum(r.seats) from Reservation r where r.flight.id = :id and r.state <> :excludeState")
                                .setParameter("id", id)
                                .setParameter("excludeState", StateChoices.CANCELLED)
                                .uniqueResult();

        return l != null ? l.intValue() : 0;
    }

}
