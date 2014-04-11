package com.noveogroup.clap.client.service.message;

import android.content.Intent;

/**
 * @author Andrey Sokolov
 */
public interface MessageProcessor {
    void processIntent(Intent intent);
}
