package Validator;

import Domain.Entity;

public class ValidatorContext
{
    private Validator validator;

    public ValidatorContext(Validator validator)
    {
        this.validator=validator;
    }

    public void validate(Entity entity) throws ValidationException {
        validator.validate(entity);
    }
}
