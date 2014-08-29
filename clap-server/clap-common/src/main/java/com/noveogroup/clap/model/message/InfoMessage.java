package com.noveogroup.clap.model.message;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class InfoMessage extends BaseMessage implements Serializable {
    @Override
    public MessageType type() {
        return MessageType.INFO;
    }
}
