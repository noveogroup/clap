package com.noveogroup.clap.library.logs.dispatchkey;

import android.app.Activity;
import android.view.KeyEvent;

public interface DispatchKeyActivity {

    void onKeyPressed(Activity activity, KeyEvent event);

}
