package Services;

import Domain.Nota;
import Domain.NotaDto;
import Domain.Student;
import Domain.Tema;
import Repositories.CRUDRepository;
import utils.StructuraSemestrului;
import Validator.ValidatorContext;
import utils.events.EventType;
import utils.events.NotaChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NotaService implements Observable<NotaChangeEvent> {
    private CRUDRepository<String, Nota> noteRepo;
    private StudentService stserv;
    private TemaService temaserv;
    private ValidatorContext validator;

    public NotaService(CRUDRepository<String, Nota> noteRepo, ValidatorContext validator,
                       StudentService stserv, TemaService temaserv) {
        this.noteRepo = noteRepo;
        this.stserv=stserv;
        this.temaserv=temaserv;
        this.validator = validator;
    }

    public int calculeazanota(int nota, LocalDate predare, int weekdeadline, ArrayList<LocalDate> motivari) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int startSem = StructuraSemestrului.startSemester.get(weekFields.ISO.weekOfWeekBasedYear()) - 1;
        int weekpredare = predare.get(weekFields.ISO.weekOfWeekBasedYear()) - startSem;
        int pred=0;
        if(weekpredare>=weekdeadline+3)
            return 1;
        int penalizare = weekpredare - weekdeadline;
        if (penalizare > 0) {
            {
                for (LocalDate motivare : motivari) {
                    int weekmotivare = motivare.get(weekFields.ISO.weekOfWeekBasedYear()) - startSem;
                    if (weekmotivare == weekdeadline  || weekmotivare == weekdeadline + 1 || weekmotivare== weekdeadline+2)
                        if(weekmotivare<weekpredare && weekmotivare!=pred) {
                            penalizare--;
                            pred=weekmotivare;
                        }
                }
            }
        }
        int res=nota - penalizare;
        if(res>=10)
            return 10;
        else
            return res;
    }

    private void writeFeedback(String id, int nota, LocalDate data, String feedback) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int startSem = StructuraSemestrului.startSemester.get(weekFields.ISO.weekOfWeekBasedYear()) - 1;
        int weekpredare = data.get(weekFields.ISO.weekOfWeekBasedYear()) - startSem;
        Student st = stserv.findStudent((long) (id.charAt(0) - '0'));
        Tema tema = temaserv.findTema((long) (id.charAt(2) - '0'));
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("NoteStudenti/" + st.getName() + " " + st.getPrenume() + ".txt", true));
            out.write("Tema:" + id.charAt(2) + "\n");
            out.write("Nota:" + Integer.toString(nota) + "\n");
            out.write("Predata in saptamana:" + Integer.toString(weekpredare) + "\n");
            out.write("Deadline:" + tema.getDeadlineWeek() + "\n");
            out.write("Feedback:" + feedback + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Nota addNota(String id, int nota, LocalDate data, String profesor, String feedback, ArrayList<LocalDate> motivari) {
        Long idtema = (long) (id.charAt(2) - '0');
        int NotaNoua = calculeazanota(nota, data, temaserv.findTema(idtema).getDeadlineWeek(), motivari);
        Nota asig = new Nota(NotaNoua, data, profesor,feedback);
        validator.validate(asig);
        Long stid = (long) (id.charAt(0) - '0');
        Long temaid = (long) (id.charAt(2) - '0');
        stserv.findStudent(stid);
        asig.setId(id);
        Nota old=noteRepo.save(asig);
        notifyObservers(new NotaChangeEvent(EventType.ADD,old,asig));
        return old;
    }

    public Nota removeNota(String id) {
        Nota n=noteRepo.delete(id);
        notifyObservers(new NotaChangeEvent(EventType.DELETE,n));
        return n;
    }

    public Nota updateNota(String id, int nota, LocalDate data, String profesor,String feedback) {
        Nota asig = new Nota(nota, data, profesor,feedback);
        validator.validate(asig);
        asig.setId(id);

        Nota old=noteRepo.update(asig);
        notifyObservers(new NotaChangeEvent(EventType.UPDATE,old,asig));
        return old;
    }

    public Nota findNota(String id) {
        return noteRepo.findOne(id);
    }

    public List<NotaDto> getAll() {
        List<Nota> note = new ArrayList<>();
        noteRepo.findAll().forEach(note::add);
        return  note.stream()
                .map(x->
                {
                    Student st=stserv.findStudent(Long.parseLong(x.getId().split("_")[0]));
                    Tema tema=temaserv.findTema(Long.parseLong(x.getId().split("_")[1]));
                    return new NotaDto(x.getId(),st.getName()+" "+st.getPrenume(),tema.getDescriere(),x.getNota(),st.getGrupa(),x.getProfesor(),x.getFeedback());
                })
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudents()
    {
        List<Student> studenti= new ArrayList<>();
        stserv.getAll().forEach(studenti::add);
        return new ArrayList<>(studenti);
    }

    public List<Tema> getAllTeme()
    {
        List<Tema> teme=new ArrayList<>();
        temaserv.getAll().forEach(teme::add);
        return teme;
    }

    public Tema getTema(Long id)
    {
        return temaserv.findTema(id);
    }

    public Student getStudent(Long id) {return stserv.findStudent(id);}

    private List<Observer<NotaChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<NotaChangeEvent> e) {
            observers.add(e);
    }

    @Override
    public void removeObserver(Observer<NotaChangeEvent> e) {
    }

    @Override
    public void notifyObservers(NotaChangeEvent t) {
            observers.forEach(x->x.update(t));
    }
}
