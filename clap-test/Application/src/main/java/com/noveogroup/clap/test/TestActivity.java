package com.noveogroup.clap.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

// todo uncomment logic when clap-api will be introduced
public class TestActivity extends Activity {

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
                throw new RuntimeException("test crash");
            }
        });

        findViewById(R.id.button_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test(DateFormat.getDateTimeInstance().format(new Date()));
            }
        });

        TextView infoView = (TextView) findViewById(R.id.info);
//        infoView.setText(String.format("projectId: %s\n"
//                        + "serverUrl: %s\n"
//                        + "username: %s\n"
//                        + "password: %s\n"
//                        + "source hash: %s\n",
//                BuildConfigHelper.get(getPackageName(), BuildConfigHelper.FIELD_CLAP_PROJECT_ID),
//                BuildConfigHelper.get(getPackageName(), BuildConfigHelper.FIELD_CLAP_SERVER_URL),
//                BuildConfigHelper.get(getPackageName(), BuildConfigHelper.FIELD_CLAP_USERNAME),
//                BuildConfigHelper.get(getPackageName(), BuildConfigHelper.FIELD_CLAP_PASSWORD),
//                BuildConfigHelper.get(getPackageName(), BuildConfigHelper.FIELD_CLAP_SOURCE_HASH)
//        ));
    }

}
