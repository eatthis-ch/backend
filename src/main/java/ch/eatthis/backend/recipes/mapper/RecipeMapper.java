package ch.eatthis.backend.recipes.mapper;

import ch.eatthis.backend.recipes.model.Recipe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeMapper implements RowMapper<Recipe> {

    @Override
    public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Recipe(rs.getString("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("image_id"),
                rs.getString("ingredients"),
                rs.getString("procedure"),
                rs.getInt("energy_cal"),
                rs.getInt("protein_g"),
                rs.getInt("fat_g"),
                rs.getInt("carbohydrate_g"));
    }
}
