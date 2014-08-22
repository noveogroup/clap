package test;

import android.app.Activity;
import android.os.Bundle;

import com.noveogroup.clap.library.common.AndroidContext;
import com.noveogroup.clap.module.crash_spy.CrashSpyModule;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CrashSpyModule().initContext(new AndroidContext(this, "crash-spy"));
        throw new RuntimeException("unknown error");
    }

}
