package com.noveogroup.clap;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Mikhail Demidov
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        ((Button) findViewById(R.id.bbb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                throw new NullPointerException("test some new revision");
            }
        });

    }
}
