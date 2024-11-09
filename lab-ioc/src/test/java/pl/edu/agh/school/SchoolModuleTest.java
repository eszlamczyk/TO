package pl.edu.agh.school;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.logger.guice.LoggerModule;
import pl.edu.agh.school.guice.SchoolModule;
import pl.edu.agh.logger.Logger;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SchoolModuleTest {

    private Injector injector;

    @BeforeEach
    public void setUp() {
        // Set up the Guice injector with the SchoolModule
        injector = Guice.createInjector(new SchoolModule(), new LoggerModule());
    }

    @Test
    public void testLoggerSingleton(){
        SchoolClass schoolClass1 = injector.getInstance(SchoolClass.class);
        SchoolClass schoolClass2 = injector.getInstance(SchoolClass.class);

        Logger logger1 = schoolClass1.getLogger();
        Logger logger2 = schoolClass2.getLogger();

        assertSame(logger1, logger2);
    }
}
