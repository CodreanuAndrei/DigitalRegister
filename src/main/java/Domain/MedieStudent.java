package Domain;

public class MedieStudent {
    private String numeSt;
    private double medie;

    public MedieStudent(String numeSt, double medie) {
        this.numeSt = numeSt;
        this.medie = medie;
    }

    @Override
    public String toString() {
        return "Nume: " + numeSt +
                " | medie:" + medie ;
    }

    public String getNumeSt() {
        return numeSt;
    }

    public void setNumeSt(String numeSt) {
        this.numeSt = numeSt;
    }

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }
}
