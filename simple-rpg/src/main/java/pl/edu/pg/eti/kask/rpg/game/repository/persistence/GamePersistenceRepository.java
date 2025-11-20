package pl.edu.pg.eti.kask.rpg.game.repository.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class GamePersistenceRepository implements GameRepository {
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Optional<Game> find(UUID id) {
        return Optional.ofNullable(entityManager.find(Game.class, id));
    }

    @Override
    public List<Game> findAll() {
        return entityManager.createQuery("select g from Game g", Game.class).getResultList();
    }


    @Override
    public void create(Game entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Game entity) {
        entityManager.remove(entityManager.find(Game.class, entity.getId()));
    }

    @Override
    public void update(Game entity) {
        entityManager.merge(entity);
    }
}
