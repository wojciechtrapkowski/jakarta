package pl.edu.pg.eti.kask.rpg.authentication.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * View bean for handling user logout.
 */
@RequestScoped
@Named
public class LogoutView {

    /**
     * Logout the current user and redirect to the home page.
     *
     * @return navigation outcome
     */
    public String logout() throws ServletException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Logout the user
        request.logout();
        
        // Invalidate the session
        context.getExternalContext().invalidateSession();
        
        // Redirect to home page
        return "/index.xhtml?faces-redirect=true";
    }
}
