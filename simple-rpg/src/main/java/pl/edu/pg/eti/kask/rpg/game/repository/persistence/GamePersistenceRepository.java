package pl.edu.pg.eti.kask.rpg.game.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;

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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Game> cq = cb.createQuery(Game.class);
        Root<Game> root = cq.from(Game.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
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
