package com.noveogroup.clap.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.util.Date;

// todo uncomment logic when clap-api will be introduced
public class TestActivity extends Activity {

    private static void log() {
        Log.v("LOG", "v(String, String)");
        Log.v("LOG", "v(String, String, Throwable)", new Exception());
        Log.d("LOG", "d(String, String)");
        Log.d("LOG", "d(String, String, Throwable)", new Exception());
        Log.i("LOG", "i(String, String)");
        Log.i("LOG", "i(String, String, Throwable)", new Exception());
        Log.w("LOG", "w(String, String)");
        Log.w("LOG", new Exception("w(String, Throwable)"));
        Log.w("LOG", "w(String, String, Throwable)", new Exception());
        Log.e("LOG", "e(String, String)");
        Log.e("LOG", "e(String, String, Throwable)", new Exception());
        Log.wtf("LOG", "wtf(String, String)");
        Log.wtf("LOG", new Exception("wtf(String, Throwable)"));
        Log.wtf("LOG", "wtf(String, String, Throwable)", new Exception());
        Log.println(Log.DEBUG, "LOG", "println(int, String, String)");
    }

    // @Trace
    private String test(String input) {
        return String.format("[%s]", input);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        findViewById(R.id.button_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log();
                throw new RuntimeException("test crash");
            }
        });

        findViewById(R.id.button_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test(DateFormat.getDateTimeInstance().format(new Date()));
            }
        });
    }

}
