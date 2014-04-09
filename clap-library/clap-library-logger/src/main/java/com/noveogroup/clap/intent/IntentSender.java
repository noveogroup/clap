package com.noveogroup.clap.intent;

import android.content.Context;
import android.content.Intent;
import com.noveogroup.clap.ProjectInfo;

public final class IntentSender {

    private Context context;

    private IntentModel intentModel;

    public IntentModel getIntentModel() {
        return intentModel;
    }

    public void setIntentModel(final IntentModel intentModel) {
        this.intentModel = intentModel;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public void send() {
        if(intentModel != null && context != null){
            final Intent intent = intentModel.createIntent();
            if(intent != null){
                context.startService(intent);
            }
        }
    }
}
