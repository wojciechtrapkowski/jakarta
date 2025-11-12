package pl.edu.pg.eti.kask.rpg.review.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"game", "user"})
@EqualsAndHashCode(exclude = {"game", "user"})
@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    private String description;
    private LocalDate dateOfCreation;
    private double mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
