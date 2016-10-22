package cz.cvut.aos.airline.dao.generics;


import cz.cvut.aos.airline.entity.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16.
 */
public abstract class AbstractGenericHibernateDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected final Class<T> type;

    public AbstractGenericHibernateDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public void persist(T t) {
        sessionFactory.getCurrentSession().persist(t);
    }

    @Override
    public void merge(T t) {
        sessionFactory.getCurrentSession().merge(t);
    }

    @Override
    public T find(long id) {
        return sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    public void delete(long id) {
        sessionFactory.getCurrentSession().delete(find(id));
    }

    @Override
    public List<T> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + type.getName()).list();
    }
}
