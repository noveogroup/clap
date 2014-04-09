package com.noveogroup.clap;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.noveogroup.clap.R;

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //To enable clap screenshots
        Log.e("CLAP","dispatch key in app updated");
        return super.dispatchKeyEvent(event);
    }
}
