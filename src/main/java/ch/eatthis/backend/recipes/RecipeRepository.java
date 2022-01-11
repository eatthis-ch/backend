package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;

import java.util.List;

public interface RecipeRepository {

    List<Recipe> getRecipeBetweenCalRange(int minimumCal, int maximumCal);

    List<Recipe> getAll(Integer skip, Integer pageSize);

    Recipe getRecipe(String id);


}
