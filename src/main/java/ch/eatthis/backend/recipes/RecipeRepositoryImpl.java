package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.mapper.RecipeMapper;
import ch.eatthis.backend.recipes.model.Recipe;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Recipe> getAll() {
        return this.template.query(RecipeQueries.getAll, new RecipeMapper());
    }

    @Override
    public Recipe getRecipe(String id) {
        return this.template.queryForObject(RecipeQueries.getRecipe, new MapSqlParameterSource("id", id), new RecipeMapper());
    }
}
