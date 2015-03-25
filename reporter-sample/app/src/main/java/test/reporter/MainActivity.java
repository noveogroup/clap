package test.reporter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.noveogroup.android.reporter.library.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.AndroidLibrary;
import example.JavaLibrary;

public class MainActivity extends Activity {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LOGGER.info("MainActivity::onCreate");
        LOGGER.info("AndroidLibrary.process: {}", AndroidLibrary.process(this));
        LOGGER.info("JavaLibrary.process: {}", JavaLibrary.process(this));

        findViewById(R.id.main_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("unexpected exception");
            }
        });

        findViewById(R.id.main_system_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("unexpected exception");
            }
        });

        findViewById(R.id.main_system_logcat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGGER.info(String.format("now is %1$tF %1$tT", System.currentTimeMillis()));
            }
        });

        findViewById(R.id.main_reporter_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.crash(new RuntimeException("unexpected exception"));
            }
        });

        findViewById(R.id.main_reporter_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(String.format("now is %1$tF %1$tT", System.currentTimeMillis()));
            }
        });

        findViewById(R.id.main_reporter_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                view.setDrawingCacheEnabled(true);
                Bitmap bitmap = view.getDrawingCache();
                Log.image(bitmap);
            }
        });
    }

}
