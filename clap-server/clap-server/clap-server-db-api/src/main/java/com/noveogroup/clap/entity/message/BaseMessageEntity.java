package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType= DiscriminatorType.STRING,length=20)
public abstract class BaseMessageEntity extends BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date timestamp;
    @ManyToOne(optional = false)
    private RevisionVariantEntity revisionVariant;

    @Column(name = "deviceId")
    private String deviceId;

    @ElementCollection
    @MapKeyColumn(name = "deviceInfoKey")
    @Column(name = "deviceInfoValue")
    @CollectionTable(name = "device_info", joinColumns = @JoinColumn(name = "device_info_id"))
    private Map<String,String> deviceInfo = new HashMap<>();

    public abstract Class<? extends BaseMessageEntity> getEntityType();

    public void setRevisionVariant(final RevisionVariantEntity revisionVariant) {
        this.revisionVariant = revisionVariant;
    }

    public RevisionVariantEntity getRevisionVariant() {
        return revisionVariant;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getDeviceInfo() {
        return deviceInfo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }
}
