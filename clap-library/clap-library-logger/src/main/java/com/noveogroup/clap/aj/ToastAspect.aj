package com.noveogroup.clap.aj;

import android.content.Context;
import android.widget.Toast;

public aspect ToastAspect {

    private boolean isToastShown = false;
    pointcut ActivityOnCreate(): execution(* Activity+.onCreate(..));
    after(): ActivityOnCreate() {
        if (!isToastShown) {
            Context context = (Context) thisJoinPoint.getThis();
            Toast.makeText(context, "DEBUG VERSION", Toast.LENGTH_LONG).show();
            isToastShown = true;
        }
    }

}
