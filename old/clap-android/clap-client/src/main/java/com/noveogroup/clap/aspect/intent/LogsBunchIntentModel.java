package com.noveogroup.clap.aspect.intent;

import android.content.Context;
import android.content.Intent;
import com.noveogroup.clap.model.message.log.LogEntry;

import java.util.ArrayList;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchIntentModel extends IntentModel {

    private final ArrayList<LogEntry> logEntries;

    public LogsBunchIntentModel(final ArrayList<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    @Override
    public Intent createIntent(final Context context) {
        final Intent intent = super.createIntent(context);
        intent.putExtra("logs",logEntries);
        return intent;
    }

    @Override
    protected String getModelType() {
        return "logsBunch";
    }
}
