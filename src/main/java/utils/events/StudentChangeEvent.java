package utils.events;

import Domain.Student;

import javax.swing.event.ChangeEvent;

public class StudentChangeEvent implements Event {
    private EventType type;
    private Student data,olddata;

    public StudentChangeEvent(EventType type, Student st)
    {
            this.type=type;
            this.data=st;
    }

    public StudentChangeEvent(EventType type, Student st, Student st2)
    {
        this.type=type;
        data=st;
        olddata=st2;
    }

    public EventType getType()
    {
            return  type;
    }

    public Student getData() {
        return data;
    }

    public Student getOlddata() {
        return olddata;
    }
}
