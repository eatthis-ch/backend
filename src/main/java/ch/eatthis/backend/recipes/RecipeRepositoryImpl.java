package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

    NamedParameterJdbcTemplate template;

    public RecipeRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Recipe> getRecipeBetweenRange(int minimumCal, int maximumCal) {
        return null;
    }
}
