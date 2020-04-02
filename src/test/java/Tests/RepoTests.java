package Tests;

import Domain.Student;
import Repositories.StudentRepo;
import Validator.ValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RepoTests {
    private StudentRepo repo;
    private Student st1, st2, st3, st4;

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

    }

    @Test
    public void save() {
        Iterable<Student> all = repo.findAll();
        int size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 4);
        st1 = new Student("Ciocan", "Catalin", "222", "cioc@gmail.com", "Teo");
        st1.setId(5L);
        repo.save(st1);
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 5);


    }

    @Test
    public void remove() {

        Iterable<Student> all = repo.findAll();
        int size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 4);
        repo.delete(2L);
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 3);
        try {
            repo.delete(5L);
        } catch (ValidationException e) {
            assertTrue(true);
        }
        size = 0;
        for (Student st : all)
            size++;
        assertEquals(size, 3);
    }

    @Test
    public void update() {
        Student student = repo.findOne(2L);
        assertEquals(student.getName(), "Alin");
        student = new Student("Student", "Nou", "227", "fds@gasdf.com", "Teo");
        student.setId(2L);
        repo.update(student);
        student = repo.findOne(2L);
        assertEquals(student.getName(), "Student");
        assertEquals(student.getPrenume(), "Nou");
    }

    @Test
    public void find() {
        Student st = repo.findOne(1L);
        assertEquals("Ciocan", st.getName());
        try {
            st = repo.findOne(5L);
            assertTrue(false);
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    @After
    public void clear() {
        Iterable<Student> all = repo.findAll();
        List<Student> toremove = new ArrayList<>();

        for (Student st : all) {
            toremove.add(st);
        }

        for (Student st : toremove) {
            repo.delete(st.getId());
        }

    }
}

