package utils.events;

import Domain.Nota;

public class NotaChangeEvent implements Event{
    private EventType type;
    private Nota data,olddata;

    public NotaChangeEvent(EventType type, Nota t)
    {
        this.type=type;
        this.data=t;
    }

    public NotaChangeEvent(EventType type, Nota nou, Nota old)
    {
        this.type=type;
        data=nou;
        olddata=old;
    }

    public EventType getType()
    {
        return  type;
    }

    public Nota getData() {
        return data;
    }

    public Nota getOlddata() {
        return olddata;
    }
}
