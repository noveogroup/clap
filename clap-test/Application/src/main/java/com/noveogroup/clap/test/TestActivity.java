package com.noveogroup.clap.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.noveogroup.clap.api.Trace;

import java.text.DateFormat;
import java.util.Date;

public class TestActivity extends Activity {

    @Trace
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
