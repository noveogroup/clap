package com.noveogroup.clap.intent;

import android.content.Intent;
import com.noveogroup.clap.ProjectInfo;

/**
 * @author Andrey Sokolov
 */
public abstract class IntentModel {


    public Intent createIntent() {
        Class<?> c = null;
        try {
            c = Class.forName("com.noveogroup.clap.RevisionImpl");
            ProjectInfo revision = (ProjectInfo) c.newInstance();
            Intent intent = new Intent("com.noveogroup.clap.SEND_MESSAGE");
            intent.putExtra("revision", revision.getRevisionId());
            intent.putExtra("project", revision.getProjectId());
            intent.putExtra("traceType", getModelType());
            return intent;
        } catch (ClassNotFoundException e) {
            // TODO fix this
        } catch (InstantiationException e) {
            // TODO fix this
        } catch (IllegalAccessException e) {
            // TODO fix this
        } catch (Exception e) {
            // TODO fix this
        }
        return null;
    }

    protected abstract String getModelType();
}
