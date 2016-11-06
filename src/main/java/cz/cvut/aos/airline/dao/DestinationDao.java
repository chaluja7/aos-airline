package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Destination;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@SuppressWarnings("unchecked")
@Repository
public class DestinationDao extends AbstractGenericHibernateDao<Destination> {

    public DestinationDao() {
        super(Destination.class);
    }

    public List<Destination> findAll(boolean desc) {
        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder.append("select d from Destination d order by d.name");

        if(desc) {
            queryStringBuilder.append(" desc");
        }

        return sessionFactory.getCurrentSession().createQuery(queryStringBuilder.toString()).list();
    }
}
