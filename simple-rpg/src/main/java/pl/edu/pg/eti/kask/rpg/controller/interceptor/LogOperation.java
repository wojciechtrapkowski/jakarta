package pl.edu.pg.eti.kask.rpg.controller.interceptor;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for logging user operations on resources.
 * When applied to a method, it will log the username, operation name, and resource identifier.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface LogOperation {
    /**
     * The name of the operation being performed (e.g., "CREATE", "UPDATE", "DELETE").
     */
    String value();
}
