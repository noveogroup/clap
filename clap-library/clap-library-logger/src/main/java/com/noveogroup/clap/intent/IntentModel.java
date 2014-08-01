package com.noveogroup.clap.intent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.ProjectInfo;

/**
 * @author Andrey Sokolov
 */
public abstract class IntentModel {


    public Intent createIntent(Context context) {
        Class<?> c = null;
        try {
            c = Class.forName("com.noveogroup.clap.RevisionImpl");
            ProjectInfo revision = (ProjectInfo) c.newInstance();
            Intent intent = new Intent(context, Class.forName("com.noveogroup.clap.client.service.ClapService"));
            intent.putExtra("revision", revision.getRevisionId());
            intent.putExtra("project", revision.getProjectId());
            intent.putExtra("traceType", getModelType());
            return intent;
        }  catch (Exception e) {
            Log.e("INTENT_SENDER", "error sending crash info", e);
        }
        return null;
    }

    protected abstract String getModelType();
}
