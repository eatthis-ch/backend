package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<Recipe> generateRecipes(Optional<String[]> usedRecipes, Optional<Integer> numberOfRecipes);

}
