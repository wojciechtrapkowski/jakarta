package pl.edu.pg.eti.kask.rpg.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

@ApplicationScoped
public class ConfigurationProducer {

    @Inject
    private ServletContext servletContext;

    private String avatarDir;

    @PostConstruct
    public void init() {
        this.avatarDir = servletContext.getInitParameter("avatar.dir");
    }

    @Produces
    @Named("avatarDir")
    public String getAvatarDir() {
        return avatarDir;
    }
}