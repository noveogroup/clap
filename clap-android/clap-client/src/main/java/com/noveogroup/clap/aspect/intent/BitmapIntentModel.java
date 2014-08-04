package com.noveogroup.clap.aspect.intent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

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
    public Intent createIntent(Context context) {
        if (bitmap != null) {
            final Intent intent = super.createIntent(context);
            if (intent != null) {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                intent.putExtra("bitmap",bs.toByteArray());
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
