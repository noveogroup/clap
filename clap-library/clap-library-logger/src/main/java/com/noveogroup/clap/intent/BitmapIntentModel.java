package com.noveogroup.clap.intent;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * @author Andrey Sokolov
 */
public class BitmapIntentModel extends IntentModel {

    private boolean causedByUser;

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isCausedByUser() {
        return causedByUser;
    }

    public void setCausedByUser(final boolean causedByUser) {
        this.causedByUser = causedByUser;
    }

    @Override
    public Intent createIntent() {
        if (bitmap != null) {
            final Intent intent = super.createIntent();
            if (intent != null) {
                //TODO compress bitmap
                intent.putExtra("bitmap",bitmap);
            }
            return intent;
        } else {
            return null;
        }
    }

    @Override
    protected String getModelType() {
        return causedByUser ? "testScreenshot" : "traceScreenshot";
    }
}
