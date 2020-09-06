package br.com.craftlife.eureka.database;

import br.com.craftlife.eureka.util.ClassUtils;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

public abstract class EurekaBD<ED extends EurekaED<PK>, PK> {

    @Inject
    private DatabaseManager databaseManager;

    @Getter(AccessLevel.PACKAGE)
    private Class<ED> entityClass;

    public EurekaBD() {
        entityClass = ClassUtils.getGenericType(this.getClass()).orElse(null);
    }

    private EntityManager getEntityManager() {
        return databaseManager.getEntityManager();
    }

    @Transactional
    public void inclui(ED ed) {
        getEntityManager().persist(ed);
    }

    public ED buscaPorId(PK id) {
        return getEntityManager().find(entityClass, id);
    }

    @Transactional
    public ED altera(ED ed) {
        return getEntityManager().merge(ed);
    }

    @Transactional
    public void remove(ED ed) {
        getEntityManager().remove(ed);
    }

    protected <T> T find(CriteriaQuery<T> query) {
        return createQuery(query).getSingleResult();
    }

    protected <T> List<T> findAll(CriteriaQuery<T> query) {
        return createQuery(query).getResultList();
    }

    protected  <T> TypedQuery<T> createQuery(CriteriaQuery<T> query) {
        return getEntityManager().createQuery(query);
    }

    protected CriteriaQuery<ED> getCriteria() {
        return getCriteria(entityClass);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    protected <T> CriteriaQuery<T> getCriteria(Class<T> resultType) {
        return getEntityManager().getCriteriaBuilder().createQuery(resultType);
    }

}
