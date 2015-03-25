package test.reporter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.noveogroup.android.reporter.library.Logger;
import com.noveogroup.android.reporter.library.Reporter;

public class MainActivity extends Activity {

    private static final Logger logger = Reporter.getLogger();
    private static final String TAG = "ReporterSample";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.main_system_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("unexpected exception");
            }
        });

        findViewById(R.id.main_system_logcat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, String.format("now is %1$tF %1$tT", System.currentTimeMillis()));
            }
        });

        findViewById(R.id.main_reporter_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.crash(new RuntimeException("unexpected exception"));
            }
        });

        findViewById(R.id.main_reporter_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.i(String.format("now is %1$tF %1$tT", System.currentTimeMillis()));
            }
        });

        findViewById(R.id.main_reporter_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                view.setDrawingCacheEnabled(true);
                Bitmap bitmap = view.getDrawingCache();
                logger.image(bitmap);
            }
        });
    }

}
