/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ravi Kumar Hazare
 */
public abstract class BaseService<T> {

    @PersistenceContext(unitName = "itmd4515PU")
    private EntityManager entityManager;

    private final Class<T> entityClass;

    protected BaseService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {

        entityManager.persist(entity);
    }

    public void update(T entity) {

        entityManager.merge(entity);
    }

    public void remove(T entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    public T find(Long id) {
        return entityManager.find(entityClass, id);
    }

    public abstract List<T> findAll();

    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
