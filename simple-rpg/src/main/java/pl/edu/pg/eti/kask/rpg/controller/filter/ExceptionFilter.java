package pl.edu.pg.eti.kask.rpg.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.edu.pg.eti.kask.rpg.controller.servlet.ApiServlet;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.HttpRequestException;

import java.io.IOException;

/**
 * Web filter with mechanism for catching exceptions and rewriting them to appropriate HTTP response statutes.
 */
@WebFilter(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
public class ExceptionFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (HttpRequestException ex) {
            response.sendError(ex.getResponseCode());
        }
    }

}
