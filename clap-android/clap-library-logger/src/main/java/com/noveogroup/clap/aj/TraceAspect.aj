package com.noveogroup.clap.aj;

import android.util.Log;
import org.aspectj.lang.Signature;

public aspect TraceAspect {

  private static final String TAG = "TRACE";

  pointcut eachExecution(): execution(* *(..));

  before(): eachExecution() {
    Signature signature = thisJoinPoint.getSignature();
    Log.d(TAG, String.format("before %s::%s",
            signature.getDeclaringType(), signature.getName()));
  }

  after(): eachExecution() {
    Signature signature = thisJoinPoint.getSignature();
    Log.d(TAG, String.format("after %s::%s",
            signature.getDeclaringType(), signature.getName()));
  }

}
