package pl.edu.pg.eti.kask.rpg.review.model.function;

import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewEditModel;
import pl.edu.pg.eti.kask.rpg.review.view.ReviewEditView;

import java.io.Serializable;
import java.util.function.Function;

public class ReviewToEditModelFunction implements Function<Review, ReviewEditModel>, Serializable {

    @Override
    public ReviewEditModel apply(Review review) {
        return ReviewEditModel.builder()
                .description(review.getDescription())
                .mark(review.getMark())
                .game(review.getGame())
                .user(review.getUser())
                .build();
    }

}
