package com.noveogroup.clap.dispatchkey;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public class DispatchKeyHandler {

    public static synchronized DispatchKeyHandler getInstance() {
        if (instance == null) {
            instance = new DispatchKeyHandler();
        }
        return instance;
    }

    private static final String TAG = "DISPATCH_TRACE";

    private static DispatchKeyHandler instance;

    private Map<Integer, DispatchKeyActivity> activityMap;

    public DispatchKeyHandler() {
        activityMap = new HashMap<Integer, DispatchKeyActivity>();
        activityMap.put(KeyEvent.KEYCODE_VOLUME_DOWN, new ScreenshotDispatchKeyActivity());
    }


    public void dispatchKey(final Object[] args, final Object aspectThis) {
        if (args != null && args.length > 0
                && args[0] instanceof KeyEvent
                && aspectThis instanceof Activity) {
            KeyEvent event = (KeyEvent) args[0];
            int action = event.getAction();
            int keyCode = event.getKeyCode();
            Activity activity = (Activity) aspectThis;
            if (action == KeyEvent.ACTION_DOWN) {
                final DispatchKeyActivity dispatchKeyActivity = activityMap.get(keyCode);
                Log.e(TAG, "dispatch keyCode in aspect = " + keyCode);
                if (dispatchKeyActivity != null) {
                    Log.e(TAG, "starting dispatch activity " + dispatchKeyActivity);
                    dispatchKeyActivity.onKeyPressed(activity, event);
                }
            }
        } else {
            Log.e(TAG, "something gone wrong with aspect dispatching key, args:" + args + ", this" + aspectThis);

        }
    }
}
