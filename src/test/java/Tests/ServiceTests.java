package Tests;

import Domain.Student;
import Repositories.StudentRepo;
import Services.StudentService;
import Validator.StudentValidator;
import Validator.ValidationException;
import Validator.ValidatorContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

public class ServiceTests {
    private StudentRepo repo;
    private Student st1, st2, st3, st4;
    private StudentService serv;

    @Before
    public void initialize() {
        st1 = new Student("Ciocan", "Catalin", "222", "cioc@gmail.com", "Teo");
        st1.setId(1L);
        st2 = new Student("Alin", "Cuciurean", "222", "alin@gmail.com", "Teo");
        st2.setId(2L);
        st3 = new Student("Alberto", "Grasu", "228", "alberto@yahoo.com", "Ionel");
        st3.setId(3L);
        st4 = new Student("Andrei", "Ionut", "225", "alberto@gmail.com", "Ionel");
        st4.setId(4L);

        repo = new StudentRepo("src/test/resources/testdata/teststudenti.xml", "student");
        repo.save(st1);
        repo.save(st2);
        repo.save(st3);
        repo.save(st4);
        serv = new StudentService(repo, new ValidatorContext(new StudentValidator()));
    }

    @Test
    public void addStudent() {
        Iterable<Student> all = serv.getAll();
        int size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 4);
        serv.addStudent((long) 5, "Mirel", "Ion", "224", "fas@g.com", "Vkad");
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 5);
        try {
            serv.addStudent(4L, "Mihai", "Fanel", "227", "fdsa", "fdas");
            fail();
        } catch (ValidationException e) {
            assertFalse(false);
        }
    }

    @Test
    public void deleteStudent() {
        Iterable<Student> all = serv.getAll();
        int size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 4);
        serv.removeStudent(2L);
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 3);
        try {
            serv.removeStudent(5L);
        } catch (ValidationException e) {
            assertTrue(true);
        }
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 3);
    }

    @Test
    public void updateStudent()
    {
        Student student = serv.findStudent(2L);
        assertEquals(student.getName(), "Alin");
        serv.updateStudent(2L,"Student","Nou","227","fdas@fds.com","Mihai");
        student = serv.findStudent(2L);
        assertEquals(student.getName(), "Student");
        assertEquals(student.getPrenume(), "Nou");
    }

    @After
    public void clear() {
        Iterable<Student> all = serv.getAll();
        List<Student> toremove = new ArrayList<>();

        for (Student st : all) {
            toremove.add(st);
        }

        for (Student st : toremove) {
            serv.removeStudent(st.getId());
        }

    }
}
