package Validator;

import Domain.Nota;
import utils.StructuraSemestrului;

public class NotaValidator implements Validator<Nota> {

    public NotaValidator(){}
    @Override
    public void validate(Nota nota) throws ValidationException {
        String err="";
        if(nota.getNota()<1 || nota.getNota()>10) err+="Nota trebuie sa fie intre 1 si 10\n";
        if(nota.getProfesor().equals("")) err+="Numele nu poate fi vid\n";
        if(nota.getData().isBefore(StructuraSemestrului.startSemester)) err+="Data este inainte de inceperea semestrului\n";
        if(nota.getData().isAfter(StructuraSemestrului.endSemester)) err+="Semestrul este gata\n";
        if(nota.getData().isAfter(StructuraSemestrului.beginHolyday) &&
                nota.getData().isBefore(StructuraSemestrului.endHolyday)) err+="Data este in vacanta";
        if(!err.equals("")) throw new ValidationException(err);
    }
}
