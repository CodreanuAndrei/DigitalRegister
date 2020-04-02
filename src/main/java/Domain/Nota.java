package Domain;

import java.time.LocalDate;
import java.util.Objects;

public class Nota extends Entity<String>{
    private int nota;
    private LocalDate data;
    private String profesor;
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public Nota(int nota, LocalDate data, String profesor, String feedback)
    {
        this.nota=nota;
        this.data=data;
        this.profesor=profesor;
        this.feedback=feedback;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getProfesor() {
        return profesor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota1 = (Nota) o;
        return nota == nota1.nota &&
                Objects.equals(data, nota1.data) &&
                Objects.equals(profesor, nota1.profesor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nota, data, profesor);
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        return "IdStudent:" + this.getId().charAt(0) +" |"+ " IdTema:"+ this.getId().charAt(2) +" |" + " nota:" +this.getNota()+ " |" + " data:"+ this.getData()+
                " |" +" profesor:" +this.getProfesor();
    }
}

