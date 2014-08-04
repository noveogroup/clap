package com.noveogroup.clap.aj;

import android.util.Log;
import com.noveogroup.clap.aspect.dispatchkey.DispatchKeyHandler;

/**
 * @author Andrey Sokolov
 */
public aspect DispatchKeyAspect {

    pointcut ActivityOnDispatch(): execution(* Activity+.dispatchKeyEvent(..));
    before (): ActivityOnDispatch() {
        Log.e("CLAP","dispatch key in aspect");
        final Object[] args = thisJoinPoint.getArgs();
        final Object aspectThis = thisJoinPoint.getThis();
        DispatchKeyHandler.getInstance().dispatchKey(args,aspectThis);
    }

}
