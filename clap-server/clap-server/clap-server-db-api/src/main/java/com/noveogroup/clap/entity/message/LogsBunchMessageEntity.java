package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.message.log.LogEntryEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "LogsBunchMessageEntity")
@DiscriminatorValue("logsBunch")
public class LogsBunchMessageEntity extends BaseMessageEntity {

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "logsBunch_logs",
            joinColumns = @JoinColumn(name = "logsBunch_id"),
            inverseJoinColumns = @JoinColumn(name = "log_id"))
    private List<LogEntryEntity> logs;

    @ElementCollection
    @Column(length = COLUMN_LENGTH)
    private List<String> logCat;

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return LogsBunchMessageEntity.class;
    }

    public List<LogEntryEntity> getLogs() {
        return logs;
    }

    public void setLogs(final List<LogEntryEntity> logs) {
        this.logs = logs;
    }

    public List<String> getLogCat() {
        return logCat;
    }

    public void setLogCat(final List<String> logCat) {
        this.logCat = logCat;
    }

}
