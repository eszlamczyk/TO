package pl.edu.agh.iisg.to.dao;

import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        Student student = new Student(firstName,lastName,indexNumber);
        return save(student);
    }

    public List<Student> findAll() {
        // TODO - implement
        try {
            return currentSession().createQuery("select  s from Student s order by s.lastName",Student.class)
                    .getResultList();
        } catch (PersistenceException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO - implement
        try {
            return Optional.of(
                    currentSession().createQuery("select s from Student s where s.indexNumber = :index",Student.class)
                    .setParameter("index", indexNumber)
                    .getSingleResult());
        } catch (PersistenceException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
