package Validator;

import Domain.Tema;

public class TemaValidator implements Validator<Tema> {

    public TemaValidator(){}

    @Override
    public void validate(Tema tema) throws ValidationException
    {
        String err = "";
        if(tema.getDeadlineWeek()<1 || tema.getDeadlineWeek()>14) err +="Deadline-ul nu este valid\n";
        if(tema.getStartWeek()<1 || tema.getStartWeek()>14) err +="StartWeek-ul nu este valid\n";
        if(tema.getStartWeek()>=tema.getDeadlineWeek()) err +="Deadline-ul nu poate fi inaintea primirii temei\n";
        if(!err.equals(""))
            throw new ValidationException(err);
    }

}
