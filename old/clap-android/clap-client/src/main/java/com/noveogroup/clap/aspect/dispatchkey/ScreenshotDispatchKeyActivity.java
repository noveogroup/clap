package com.noveogroup.clap.aspect.dispatchkey;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.noveogroup.clap.aspect.intent.BitmapIntentModel;
import com.noveogroup.clap.aspect.intent.IntentSender;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotDispatchKeyActivity implements DispatchKeyActivity {
    private static final String TAG = "SCREENSHOT_DISPATCH_TRACE";

    @Override
    public void onKeyPressed(final Activity activity, final KeyEvent event) {
        Log.e(TAG, "screenshot activity 1");
        final View v1 = activity.getWindow().getDecorView();
        Log.e(TAG, "screenshot activity 2");
        v1.setDrawingCacheEnabled(true);
        Log.e(TAG, "screenshot activity 3");
        Bitmap bm = v1.getDrawingCache();
        Log.e(TAG, "bitmap width " + bm.getWidth());
        Log.e(TAG, "bitmap height " + bm.getHeight());
        Log.e(TAG, "screenshot activity 4");
        sendIntent(activity, bm);
        Log.e(TAG, "screenshot activity 5");
    }

    private void sendIntent(final Activity activity, final Bitmap bm) {
        IntentSender sender = new IntentSender();
        sender.setContext(activity);
        final BitmapIntentModel bitmapIntentModel = new BitmapIntentModel();
        bitmapIntentModel.setBitmap(bm);
        bitmapIntentModel.setCausedByUser(true);
        sender.setIntentModel(bitmapIntentModel);
        sender.send();
    }
}
