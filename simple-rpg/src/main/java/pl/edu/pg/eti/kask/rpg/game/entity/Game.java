package pl.edu.pg.eti.kask.rpg.game.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "games")
public class Game implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private LocalDate dateOfRelease;
    @Enumerated(EnumType.STRING)
    private GameType type;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
