package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Destination;
import org.springframework.stereotype.Repository;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Repository
public class DestinationDao extends AbstractGenericHibernateDao<Destination> {

    public DestinationDao() {
        super(Destination.class);
    }

}
