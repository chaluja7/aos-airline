package cz.cvut.aos.airline.dao.generics;

import cz.cvut.aos.airline.entity.AbstractEntity;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 19.03.14.
 */
public interface GenericDao<T extends AbstractEntity> {

    /**
     * persist entity
     * @param t entity to persist
     */
    void persist(T t);

    /**
     * merge entity
     * @param t entity to merge
     */
    void merge(T t);

    /**
     * find entity by id
     * @param id entity id
     * @return founded entity
     */
    T find(long id);

    /**
     * delete entity
     * @param id entity id (to delete)
     */
    void delete(long id);

    /**
     * @return all records of given type
     */
    List<T> findAll();

}
