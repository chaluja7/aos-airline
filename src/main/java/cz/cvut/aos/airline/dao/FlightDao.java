package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Flight;
import org.springframework.stereotype.Repository;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Repository
public class FlightDao extends AbstractGenericHibernateDao<Flight> {

    public FlightDao() {
        super(Flight.class);
    }

}
