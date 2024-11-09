package pl.edu.agh.logger.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import pl.edu.agh.logger.Logger;

public class LoggerModule extends AbstractModule {

    @Provides
    @Singleton
    public Logger provideLogger() {
        return Logger.getInstance();
    }
}
