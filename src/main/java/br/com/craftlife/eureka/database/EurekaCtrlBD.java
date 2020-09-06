package br.com.craftlife.eureka.database;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public abstract class EurekaCtrlBD<ED extends EurekaCtrlED<PK>, PK> extends EurekaBD<ED, PK> {

    private CriteriaQuery<ED> queryByProp(String prop, String val) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<ED> criteria = getCriteria();
        Root<ED> root = criteria.from(getEntityClass());
        criteria
                .select(root)
                .where(builder.equal(root.get(prop), val));
        return criteria;
    }

    public List<ED> getCreatedBy(String createdBy) {
        return findAll(queryByProp("erkCreatedBy", createdBy));
    }

    public List<ED> getCreatedByIp(String createdByIp) {
        return findAll(queryByProp("erkCreatedByIp", createdByIp));
    }

    public List<ED> getUpdatedBy(String updatedBy) {
        return findAll(queryByProp("erkUpdatedBy", updatedBy));
    }

    public List<ED> getUpdatedByIp(String updatedByIp) {
        return findAll(queryByProp("erkUpdatedByIp", updatedByIp));
    }

    public List<ED> getCreatedBetween(LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<ED> criteria = getCriteria();
        Root<ED> root = criteria.from(getEntityClass());
        criteria
                .select(root)
                .where(
                        builder.greaterThanOrEqualTo(root.get("erkCreationDate"), start),
                        builder.lessThanOrEqualTo(root.get("erkCreationDate"), end)
                );
        return findAll(criteria);
    }

    public List<ED> getUpdatedBetween(LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<ED> criteria = getCriteria();
        Root<ED> root = criteria.from(getEntityClass());
        criteria
                .select(root)
                .where(
                        builder.greaterThanOrEqualTo(root.get("erkUpdateDate"), start),
                        builder.lessThanOrEqualTo(root.get("erkUpdateDate"), end)
                );
        return findAll(criteria);
    }

}
