package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


public class SchoolModule extends AbstractModule {

    @Provides
    public PersistenceManager providePersistenceManager(SerializablePersistenceManager persistenceManager) {
        return persistenceManager;
    }

    @Provides
    @ClassesStorage
    public String provideClassesStorage() {return "classes2.dat";}

    @Provides
    @TeachersStorage
    public String provideTeachersStorage() {return "teachers2.dat";}
}
