package cz.cvut.aos.airline.dao;

import cz.cvut.aos.airline.dao.generics.AbstractGenericHibernateDao;
import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.exception.UnknownOrderColumnException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

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

    public int countAll() {
        Long l = (Long) sessionFactory.getCurrentSession()
            .createQuery("select count(f.id) from Flight f")
            .uniqueResult();

        return l != null ? l.intValue() : 0;
    }

    public List<Flight> find(ZonedDateTime departureFrom, ZonedDateTime departureTo, Integer start, Integer count, String orderColumn, boolean desc) throws UnknownOrderColumnException {
        StringBuilder queryStringBuilder = new StringBuilder().append("select f from Flight f where 1 = 1");

        if(departureFrom != null) {
            queryStringBuilder.append(" and f.dateOfDeparture >= :departureFrom");
        }

        if(departureTo != null) {
            queryStringBuilder.append(" and f.dateOfDeparture <= :departureTo");
        }

        queryStringBuilder.append(" order by");
        FlightOrderColumn flightOrderColumn = FlightOrderColumn.ID;
        if(orderColumn != null) {
            //zadany explicitni radici sloupec
            flightOrderColumn = FlightOrderColumn.fromStringCode(orderColumn);
            if(flightOrderColumn == null) {
                //zadal neplatny radici sloupec
                throw new UnknownOrderColumnException();
            }
        }

        switch(flightOrderColumn) {
            case ID:
                queryStringBuilder.append(" f.id");
                break;
            case DATE_OF_DEPARTURE:
                queryStringBuilder.append(" f.dateOfDeparture");
                break;
            case NAME:
                queryStringBuilder.append(" f.name");
                break;
            default:
                queryStringBuilder.append(" f.id");
                break;
        }

        if(desc) {
            queryStringBuilder.append(" desc");
        }

        Query query = sessionFactory.getCurrentSession().createQuery(queryStringBuilder.toString());

        if(departureFrom != null) {
            query.setParameter("departureFrom", departureFrom);
        }

        if(departureTo != null) {
            query.setParameter("departureTo", departureTo);
        }

        if(start != null && start >= 0) {
            query.setFirstResult(start);
        }

        if(count != null && count >= 0) {
            query.setMaxResults(count);
        }

        return query.list();
    }

    private enum FlightOrderColumn {

        ID("id"),
        NAME("name"),
        DATE_OF_DEPARTURE("dateOfDeparture");

        private String name;

        FlightOrderColumn(String name) {
            this.name = name;
        }

        public static FlightOrderColumn fromStringCode(String code) {
            if(code != null) {
                for(FlightOrderColumn flightOrderColumn : FlightOrderColumn.values()) {
                    if(code.equalsIgnoreCase(flightOrderColumn.getName())) {
                        return flightOrderColumn;
                    }
                }
            }

            return null;
        }

        public String getName() {
            return name;
        }
    }
}
