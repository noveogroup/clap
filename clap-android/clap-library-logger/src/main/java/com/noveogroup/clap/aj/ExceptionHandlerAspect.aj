package com.noveogroup.clap.aj;

import android.app.Activity;
import com.noveogroup.clap.aspect.ActivityTraceLogger;
import com.noveogroup.clap.aspect.ExceptionHandler;

public aspect ExceptionHandlerAspect {

    // exception handler

    pointcut EachClassLoad(): staticinitialization(Activity+);
    before(): EachClassLoad() {
//        Activity activity = (Activity) thisJoinPoint.getThis();
//        if (activity == null) {
//            Log.d("Empty activity", "Empty activity");
//        }   else {
//            Log.d("Non Empty activity", "Non Empty activity");
//        }
        ExceptionHandler.replaceHandler();
    }

    // activity logger

    pointcut ActivityOnCreate(): execution(* Activity+.onCreate(..));
    pointcut ActivityOnStart(): execution(* Activity+.onStart(..));
    pointcut ActivityOnResume(): execution(* Activity+.onResume(..));
    pointcut ActivityBeforeMethods(): ActivityOnCreate() || ActivityOnStart() || ActivityOnResume();

    before(): ActivityBeforeMethods() {
        Activity activity = (Activity) thisJoinPoint.getThis();
        String methodName = thisJoinPoint.getSignature().getName();
        ActivityTraceLogger.getInstance().logMessage(activity, methodName);
    }

    pointcut ActivityOnPause(): execution(* Activity+.onPause(..));
    pointcut ActivityOnStop(): execution(* Activity+.onStop(..));
    pointcut ActivityOnDestroy(): execution(* Activity+.onDestroy(..));
    pointcut ActivityAfterMethods(): ActivityOnPause() || ActivityOnStop() || ActivityOnDestroy();

    after(): ActivityAfterMethods() {
        Activity activity = (Activity) thisJoinPoint.getThis();
        String methodName = thisJoinPoint.getSignature().getName();
        ActivityTraceLogger.getInstance().logMessage(activity, methodName);
    }

}
