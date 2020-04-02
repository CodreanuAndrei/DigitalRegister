package Services;

import Domain.Student;
import Repositories.CRUDRepository;
import Validator.ValidatorContext;
import utils.events.Event;
import utils.events.EventType;
import utils.events.StudentChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class StudentService implements Observable<StudentChangeEvent> {
    private CRUDRepository<Long, Student> stRepo;
    private ValidatorContext validator;


    private Long autoID()
    {
        Long aidi=null;
        Iterable<Student> students=stRepo.findAll();
        for(Student st: students)
        {
             aidi=st.getId();
        }
        if(aidi==null)return 1L;
        else return aidi;

    }

    public StudentService(CRUDRepository<Long, Student> stRepo, ValidatorContext validator) {
        this.stRepo = stRepo;
        this.validator = validator;
    }

    public Student addStudent(String nume, String prenume, String grupa, String email, String indrumator) {
        Student st = new Student(nume, prenume, grupa, email, indrumator);
        validator.validate(st);
        st.setId(autoID()+1);
        Student ex = stRepo.save(st);
        notifyObservers(new StudentChangeEvent(EventType.ADD, ex));
        return ex;
    }

    public Student removeStudent(Long id) {
        Student deleted = stRepo.delete(id);
        notifyObservers(new StudentChangeEvent(EventType.DELETE, deleted));
        return deleted;
    }

    public Student updateStudent(Long id, String nume, String prenume, String grupa, String email, String indrumator) {
        Student st = new Student(nume, prenume, grupa, email, indrumator);
        validator.validate(st);
        st.setId(id);
        Student old = stRepo.update(st);
        notifyObservers(new StudentChangeEvent(EventType.UPDATE, st, old));
        return old;
    }

    public Student findStudent(Long id) {
        return stRepo.findOne(id);
    }

    public Iterable<Student> getAll() {
        return stRepo.findAll();
    }


    private List<Observer<StudentChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<StudentChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentChangeEvent> e) {

    }

    @Override
    public void notifyObservers(StudentChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }
}

