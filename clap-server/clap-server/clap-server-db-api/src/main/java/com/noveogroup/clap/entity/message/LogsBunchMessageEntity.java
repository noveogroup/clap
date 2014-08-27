package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.message.log.LogEntryEntity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "LogsBunchMessageEntity")
@DiscriminatorValue("logsBunch")
public class LogsBunchMessageEntity extends BaseMessageEntity {

    @OneToMany(cascade = {CascadeType.ALL})
    private List<LogEntryEntity> logEntries;

    @ElementCollection
    private List<String> logCat;

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return LogsBunchMessageEntity.class;
    }

    public List<LogEntryEntity> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(final List<LogEntryEntity> logEntries) {
        this.logEntries = logEntries;
    }

    public List<String> getLogCat() {
        return logCat;
    }

    public void setLogCat(final List<String> logCat) {
        this.logCat = logCat;
    }
}
