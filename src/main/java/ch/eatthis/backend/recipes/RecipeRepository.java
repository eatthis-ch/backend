package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;

import java.util.List;

public interface RecipeRepository {

    List<Recipe> getRecipeBetweenRange(int minimumCal, int maximumCal);


}
