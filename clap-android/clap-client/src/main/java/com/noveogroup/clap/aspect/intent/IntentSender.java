package com.noveogroup.clap.aspect.intent;

import android.content.Context;
import android.content.Intent;

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

    public boolean send() {
        if(intentModel != null && context != null){
            final Intent intent = intentModel.createIntent(context);
            if(intent != null){
                context.startService(intent);
                return true;
            }
        }
        return false;
    }
}
