package pl.edu.pg.eti.kask.rpg.controller.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

//Can be overwritten in web.xml using servlet configuration.
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
}
