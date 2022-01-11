package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.mapper.RecipeMapper;
import ch.eatthis.backend.recipes.model.Recipe;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
    public List<Recipe> getRecipeBetweenCalRange(int minimumCal, int maximumCal) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("lowerValue", minimumCal);
        mapSqlParameterSource.addValue("higherValue", maximumCal);
        return this.template.query(RecipeQueries.getRecipeBetweenCalString, mapSqlParameterSource, new RecipeMapper());
    }

    @Override
    public List<Recipe> getAll(Integer skip, Integer pageSize) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("pageSize", pageSize);
        mapSqlParameterSource.addValue("offset", skip);
        return this.template.query(RecipeQueries.allRecipes, mapSqlParameterSource, new RecipeMapper());
    }

    @Override
    public Recipe getRecipe(String id) {
        return this.template.queryForObject(RecipeQueries.getRecipe, new MapSqlParameterSource("id", id), new RecipeMapper());
    }
}
