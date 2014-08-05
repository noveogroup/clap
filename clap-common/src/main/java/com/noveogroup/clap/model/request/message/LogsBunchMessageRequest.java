package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.LogsBunchMessage;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessageRequest extends BaseMessageRequest {
    private LogsBunchMessage message;

    public LogsBunchMessage getMessage() {
        return message;
    }

    public void setMessage(final LogsBunchMessage message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("message", message)
                .toString();
    }
}
