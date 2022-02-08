package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.mapper.RecipeMapper;
import ch.eatthis.backend.recipes.model.Recipe;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeRepository {

    NamedParameterJdbcTemplate template;

    public RecipeRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Recipe> getRecipeBetweenCalRange(int minimumCal, int maximumCal) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("lowerValue", minimumCal);
        mapSqlParameterSource.addValue("higherValue", maximumCal);
        return this.template.query(RecipeQueries.getRecipeBetweenCalString, mapSqlParameterSource, new RecipeMapper());
    }

    public List<Recipe> getAll(int skip, int pageSize) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("pageSize", pageSize);
        mapSqlParameterSource.addValue("offset", skip);
        return this.template.query(RecipeQueries.allRecipes, mapSqlParameterSource, new RecipeMapper());
    }

    public Recipe getRecipe(String id) {
        return this.template.queryForObject(RecipeQueries.getRecipe, new MapSqlParameterSource("id", id), new RecipeMapper());
    }

    public List<Recipe> getLowerThanRecipes(int cal) {
        return this.template.query(RecipeQueries.getLowerThanRecipes, new MapSqlParameterSource("cal_value", cal), new RecipeMapper());
    }
}
