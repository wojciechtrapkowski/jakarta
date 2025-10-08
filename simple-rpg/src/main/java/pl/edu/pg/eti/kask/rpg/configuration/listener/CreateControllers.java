package pl.edu.pg.eti.kask.rpg.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pl.edu.pg.eti.kask.rpg.character.controller.simple.CharacterSimpleController;
import pl.edu.pg.eti.kask.rpg.character.controller.simple.ProfessionSimpleController;
import pl.edu.pg.eti.kask.rpg.character.service.CharacterService;
import pl.edu.pg.eti.kask.rpg.character.service.ProfessionService;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.user.controller.simple.SimpleUserController;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of controllers and puts them in
 * the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateControllers implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        UserService userService = (UserService) event.getServletContext().getAttribute("userService");

        event.getServletContext().setAttribute("userController", new SimpleUserController(
                userService,
                new DtoFunctionFactory()
        ));
    }
}
