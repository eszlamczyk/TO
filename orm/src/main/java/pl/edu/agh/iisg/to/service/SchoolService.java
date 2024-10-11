package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SchoolService {

    private final TransactionService transactionService;

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;

    private final StudentRepository studentRepository;

    public SchoolService(TransactionService transactionService, StudentDao studentDao, CourseDao courseDao, GradeDao gradeDao) {
        this.transactionService = transactionService;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.studentRepository = new StudentRepository(this.studentDao,this.courseDao); //just to remove warnings :)
    }

    public boolean enrollStudent(final Course course, final Student student) {

        return transactionService.doAsTransaction(() ->{
            if (course.studentSet().contains(student)){
                return false;
            }
            course.studentSet().add(student);
            student.courseSet().add(course);

            return true;
        }).orElse(false);
    }

    public void removeStudent(int indexNumber) {
        studentRepository.removeByIndexNumber(indexNumber);
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        return transactionService.doAsTransaction(() -> {
            var grade = new Grade(student,course,gradeValue);
            course.gradeSet().add(grade);
            student.gradeSet().add(grade);
            gradeDao.save(grade);
            return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        List<Student> studentList = studentRepository.findAllByCourseName(courseName);

        Map<String, List<Float>> grades = new HashMap<>();

        studentList.forEach(student -> {
            Set<Grade> gradeSet = student.gradeSet();

            List<Float> gradeValues = gradeSet.stream()
                    .filter(grade -> grade.course().name().equals(courseName))
                    .map(Grade::grade)
                    .collect(Collectors.toList());

            grades.put(student.firstName() + " " + student.lastName(), gradeValues);
        });

        return grades;
    }
}
