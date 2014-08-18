package com.noveogroup.clap.library.logs.dispatchkey;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.noveogroup.clap.library.api.IntentSender;

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
        new IntentSender(activity).sendTestScreenShot(bm);
    }

}
