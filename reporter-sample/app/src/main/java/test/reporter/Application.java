package test.reporter;

import com.noveogroup.android.reporter.library.Reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends android.app.Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public void onCreate() {
        super.onCreate();
        Reporter.initContext(this);
        LOGGER.info("Application::onCreate");
    }

}
