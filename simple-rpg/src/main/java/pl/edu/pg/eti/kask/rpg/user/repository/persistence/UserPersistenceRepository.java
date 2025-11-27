package pl.edu.pg.eti.kask.rpg.user.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class UserPersistenceRepository implements UserRepository {
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    public Optional<User> find(UUID id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            
            Predicate loginPredicate = cb.equal(root.get("login"), login);
            
            cq.select(root).where(loginPredicate);
            
            return Optional.of(entityManager.createQuery(cq).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }


    @Override
    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }


    @Override
    public void create(User entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(User entity) {
        entityManager.remove(entityManager.find(User.class, entity.getId()));
    }

    @Override
    public void update(User entity) {
        entityManager.merge(entity);
    }
}
