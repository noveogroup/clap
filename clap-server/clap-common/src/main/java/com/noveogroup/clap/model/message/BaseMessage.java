package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.BaseModel;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseMessage extends BaseModel implements Serializable {
    private String deviceId;
    private long timestamp;
    private Map<String,String> deviceInfo;

    public abstract MessageType type();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(final Map<String, String> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
