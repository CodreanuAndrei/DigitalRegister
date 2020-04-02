package Services;

import Domain.Tema;
import Repositories.CRUDRepository;
import Validator.ValidatorContext;
import utils.StructuraSemestrului;
import utils.events.EventType;
import utils.events.StudentChangeEvent;
import utils.events.TemaChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TemaService implements Observable<TemaChangeEvent> {
    private CRUDRepository<Long, Tema> temaRepo;
    private ValidatorContext validator;


    public Long autoID()
    {
        Long aidi=null;
        Iterable<Tema> teme=temaRepo.findAll();
        for(Tema t:teme)
        {
            aidi=t.getId();
        }
        if(aidi==null)return 1L;
        else return aidi;
    }

    private int autoStartWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int week=date.get(weekFields.ISO.weekOfWeekBasedYear());
        System.out.println(week);
        weekFields = WeekFields.of(Locale.getDefault());
        int beginweek = StructuraSemestrului.startSemester.get(weekFields.ISO.weekOfWeekBasedYear());
        return week - beginweek + 1;
    }

    public TemaService(CRUDRepository<Long, Tema> temaRepo, ValidatorContext validator) {
        this.temaRepo = temaRepo;
        this.validator = validator;
    }

    public Tema addTema(String desc, int deadline) {
        int startweek = autoStartWeek();
        Tema tema = new Tema(desc, startweek, deadline);
        validator.validate(tema);
        tema.setId(autoID()+1);
        temaRepo.save(tema);
        notifyObservers(new TemaChangeEvent(EventType.ADD,tema));
        return tema;
    }

    public Tema deleteTema(Long id) {
        Tema tema=temaRepo.delete(id);
        notifyObservers(new TemaChangeEvent(EventType.DELETE,tema));
        return tema;
    }

    public Tema updateTema(Long id, String desc, int deadline) {
        int startweek = autoStartWeek();
        Tema tema = new Tema(desc, startweek, deadline);
        validator.validate(tema);
        tema.setId(id);
        temaRepo.update(tema);
        notifyObservers(new TemaChangeEvent(EventType.UPDATE,tema));
        return tema;
    }

    public Tema findTema(Long id) {
        return temaRepo.findOne(id);
    }

    public Iterable<Tema> getAll() {
        return temaRepo.findAll();
    }


    private List<Observer<TemaChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<TemaChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<TemaChangeEvent> e) {

    }

    @Override
    public void notifyObservers(TemaChangeEvent t) {
            observers.forEach(e->e.update(t));
    }
}

