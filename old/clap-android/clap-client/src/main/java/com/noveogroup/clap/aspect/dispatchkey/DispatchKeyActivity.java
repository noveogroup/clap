package com.noveogroup.clap.aspect.dispatchkey;

import android.app.Activity;
import android.view.KeyEvent;

/**
 * @author Andrey Sokolov
 */
public interface DispatchKeyActivity {
    void onKeyPressed(Activity activity, KeyEvent event);
}
