package Validator;

import Domain.Student;

import java.util.regex.Pattern;


public class StudentValidator implements  Validator<Student>{

    public StudentValidator()
    { }
    @Override
    public void validate(Student student) throws ValidationException {
        String err = "";
        if(student.getName().equals("")) err +="Numele nu poate fi vid\n";
        if(student.getPrenume().equals("")) err +="Prenumele nu poate fi vid\n";
        if(!Pattern.matches(".+@.+.com",student.getEmail())) err +="Emailul nu este valid\n";

        if(!student.getGrupa().matches("[0-9]+")) err+="Grupa nu poate contine litere sau caractere speciale\n";
        if((Integer.parseInt(student.getGrupa()) < 221 || Integer.parseInt(student.getGrupa())>227)) err+="Nu exista grupa!\n";
        if(student.getIndrumator().equals("")) err +="Numele indrumatorului nu poate fi vid";
        if(err.equals("")) {
        }
        else
            throw new ValidationException(err);
    }
}
