package Domain;

import java.util.Objects;

public class Student extends Entity<Long> implements Comparable<Student>{

    private String name;
    private String prenume;
    private String grupa;
    private String email;
    private String indrumator;

    public Student(String name, String prenume, String grupa, String email, String indrumator) {
        this.name=name; this.prenume=prenume; this.grupa=grupa; this.email=email; this.indrumator=indrumator;
    }

    public String getName() { return name; }

    public String getPrenume() { return prenume; }

    public String getGrupa() { return grupa; }

    public String getEmail() { return email; }

    public String getIndrumator() { return indrumator; }

    public void setName(String name) { this.name = name; }

    public void setPrenume(String prenume) { this.prenume = prenume; }

    public void setGrupa(String grupa) { this.grupa = grupa; }

    public void setEmail(String email) { this.email = email; }

    public void setIndrumator(String indrumator) { this.indrumator = indrumator; }

    @Override
    public String toString() {
        return this.getName()+" " + this.getPrenume()+ " | grupa:"+ this.getGrupa()
                + " | email:" + this.getEmail() + " | Indrumator de laborator:" + getIndrumator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return grupa.equals(student.grupa) &&
                Objects.equals(name, student.name) &&
                Objects.equals(prenume, student.prenume) &&
                Objects.equals(email, student.email) &&
                Objects.equals(indrumator, student.indrumator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, prenume, grupa, email, indrumator);
    }

    @Override
    public int compareTo(Student o) {
        return 0;
    }


}
