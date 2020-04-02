package Domain;

public class TemaGrea {
    private String tema;
    private double medie;

    public TemaGrea(String tema, double medie) {
        this.tema = tema;
        this.medie = medie;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }
}
