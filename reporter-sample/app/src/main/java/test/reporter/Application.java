package test.reporter;

import com.noveogroup.android.reporter.library.Reporter;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Reporter.initContext(this);
    }

}
