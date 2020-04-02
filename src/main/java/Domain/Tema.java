package Domain;

import java.util.Objects;

public class Tema extends Entity<Long> implements Comparable<Tema>{
    private String descriere;
    private int startWeek;
    private int deadlineWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tema tema = (Tema) o;
        return startWeek == tema.startWeek &&
                deadlineWeek == tema.deadlineWeek &&
                Objects.equals(descriere, tema.descriere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriere, startWeek, deadlineWeek);
    }

    public Tema(String descriere, int startWeek, int deadlineWek) {
        this.startWeek=startWeek;
        this.descriere = descriere;
        this.deadlineWeek = deadlineWek;
    }

    public String getDescriere() { return descriere; }

    public void setDescriere(String descriere) { this.descriere = descriere; }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek){ this.startWeek=startWeek;}

    public int getDeadlineWeek() { return deadlineWeek; }

    public void setDeadlineWeek(int deadlineWeek) { this.deadlineWeek = deadlineWeek;}

    @Override
    public int compareTo(Tema o) {
        return 0;
    }

    @Override
    public String toString() {
        return  this.getDescriere();
    }

}