package pl.edu.pg.eti.kask.rpg.controller.interceptor;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;
import lombok.extern.java.Log;

import java.util.UUID;
import java.util.logging.Level;

/**
 * Interceptor for logging user operations on resources.
 * Logs the username, operation name, and resource identifier.
 */
@Interceptor
@LogOperation("")
@Log
public class OperationLoggingInterceptor {

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private SecurityContext securityContext;

    @AroundInvoke
    public Object logOperation(InvocationContext context) throws Exception {
        // Get the annotation from the method
        LogOperation annotation = context.getMethod().getAnnotation(LogOperation.class);
        
        if (annotation != null) {
            String operationName = annotation.value();
            String username = getUsername();
            UUID resourceId = extractResourceId(context);
            
            // Log the operation
            log.log(Level.INFO, 
                String.format("User '%s' performed operation '%s' on resource with ID: %s", 
                    username, operationName, resourceId));
        }
        
        // Proceed with the method execution
        return context.proceed();
    }

    /**
     * Gets the current username from the security context.
     */
    private String getUsername() {
        try {
            if (securityContext != null && securityContext.getCallerPrincipal() != null) {
                return securityContext.getCallerPrincipal().getName();
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Could not retrieve username from security context", e);
        }
        return "ANONYMOUS";
    }

    /**
     * Extracts the resource ID from the method parameters.
     * Looks for the first UUID parameter which is typically the resource ID.
     */
    private UUID extractResourceId(InvocationContext context) {
        Object[] parameters = context.getParameters();
        if (parameters != null) {
            for (Object param : parameters) {
                if (param instanceof UUID) {
                    return (UUID) param;
                }
            }
        }
        return null;
    }
}
