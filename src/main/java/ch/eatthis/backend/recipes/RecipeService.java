package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<Recipe> generateRecipes(Optional<String[]> usedRecipes, Optional<Integer> numberOfRecipes, int calories);

    List<Recipe> getAllRecipes(Integer skip);

    Recipe getById(String id);

    String castToString(String[] a);

}
