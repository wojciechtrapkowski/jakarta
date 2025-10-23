package pl.edu.pg.eti.kask.rpg.game.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pl.edu.pg.eti.kask.rpg.component.ModelFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.model.GamesModel;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;

@RequestScoped
@Named
public class GamesList {

    private final GameService service;

    private GamesModel games;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;

    @Inject
    public GamesList(GameService service, ModelFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all characters
     */
    public GamesModel getGames() {
        if (games == null) {
            games = factory.gamesToModel().apply(service.findAll());
        }
        return games;
    }

    public String deleteAction(GamesModel.Game character) {
        service.delete(character.getId());
        return "games_list?faces-redirect=true";
    }
}
