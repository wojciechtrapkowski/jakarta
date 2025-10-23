package pl.edu.pg.eti.kask.rpg.game.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;

import jakarta.inject.Inject;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;

import java.util.UUID;

@FacesConverter(value = "gameConverter", managed = true)
public class GameConverter implements Converter<Game> {

    @Inject
    private GameService gameService;

    @Override
    public Game getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return Game.builder().build();
        return gameService.find(UUID.fromString(value)).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Game game) {
        return game != null ? game.getId().toString() : "";
    }
}