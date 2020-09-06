package br.com.craftlife.eureka.database;

import br.com.craftlife.eureka.server.entity.User;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class EurekaCtrlED<T> implements EurekaED<T> {

    @Column(name = "erk_created_by", length = 16, nullable = false, updatable = false)
    private String erkCreatedBy;

    @Column(name = "erk_created_by_ip", length = 15, nullable = false, updatable = false)
    private String erkCreatedByIp;

    @Column(name = "erk_creation_date", nullable = false, updatable = false)
    private LocalDateTime erkCreationDate;

    @Column(name = "erk_updated_by", length = 16, nullable = false)
    private String erkUpdatedBy;

    @Column(name = "erk_updated_by_ip", length = 15, nullable = false)
    private String erkUpdatedByIp;

    @Column(name = "erk_update_date", nullable = false)
    private LocalDateTime erkUpdateDate;

    public final void update(@NonNull User user) {
        if (erkCreatedBy == null) {
            erkCreatedBy = user.getName();
            erkCreatedByIp = user.getIpAddress();
            erkCreationDate = LocalDateTime.now();
        }
        erkUpdatedBy = user.getName();
        erkUpdatedByIp = user.getIpAddress();
        erkUpdateDate = LocalDateTime.now();
    }

}
