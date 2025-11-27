package pl.edu.pg.eti.kask.rpg.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure the mark is within valid range (1.0 - 10.0).
 */
@Documented
@Constraint(validatedBy = ValidMarkValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMark {
    String message() default "{validation.mark.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
