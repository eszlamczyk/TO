package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;


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
