package Domain;

public class NotaDto {
    private  String notaid;
    private String studentName;
    private String temaId;
    private double nota;
    private String grupa;
    private String profesor;
    private String feedback;

    public String getGrupa() {
        return grupa;
    }

    public String getNotaid() {
        return notaid;
    }

    public String getFeedback() {
        return feedback;
    }

    public NotaDto(String notaid, String studentName, String temaId, double nota, String grupa, String profesor, String feedback) {
        this.notaid=notaid;
        this.studentName = studentName;
        this.temaId = temaId;
        this.nota = nota;
        this.grupa=grupa;
        this.profesor = profesor;
        this.feedback=feedback;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTemaId() {
        return temaId;
    }

    public void setTemaId(String temaId) {
        this.temaId = temaId;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getProfesor() {
        return profesor;
    }

    @Override
    public String toString() {
        return "Nume student:" + this.getStudentName() +" |"+ " Lab:"+ this.getTemaId() +" |" + " nota:" +this.getNota()+ " |"
                +" profesor:" +this.getProfesor();
    }
}
