package pl.edu.pg.eti.kask.rpg.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the @ValidMark annotation.
 * Ensures the mark is between 1.0 and 10.0 inclusive.
 */
public class ValidMarkValidator implements ConstraintValidator<ValidMark, Double> {

    @Override
    public void initialize(ValidMark constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value >= 1.0 && value <= 10.0;
    }
}
