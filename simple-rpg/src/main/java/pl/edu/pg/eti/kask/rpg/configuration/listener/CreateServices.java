package pl.edu.pg.eti.kask.rpg.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pl.edu.pg.eti.kask.rpg.crypto.component.Pbkdf2PasswordHash;
import pl.edu.pg.eti.kask.rpg.datastore.component.DataStore;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;
import pl.edu.pg.eti.kask.rpg.user.repository.memory.UserInMemoryRepository;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of services (business layer) and
 * puts them in the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateServices implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataStore dataSource = (DataStore) event.getServletContext().getAttribute("datasource");

        UserRepository userRepository = new UserInMemoryRepository(dataSource);

        event.getServletContext().setAttribute("userService", new UserService(userRepository, new Pbkdf2PasswordHash()));
    }
}
