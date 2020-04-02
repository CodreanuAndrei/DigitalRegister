package utils.events;

import Domain.Tema;

public class TemaChangeEvent implements Event {
    private EventType type;
    private Tema data,olddata;

    public TemaChangeEvent(EventType type, Tema data, Tema olddata) {
        this.type = type;
        this.data = data;
        this.olddata = olddata;
    }

    public TemaChangeEvent(EventType type, Tema data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType()
    {
        return  type;
    }

    public  Tema getData() {return data;}
}
