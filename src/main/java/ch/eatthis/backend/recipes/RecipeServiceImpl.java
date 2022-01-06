package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import ch.eatthis.backend.recipes.model.RecipeModule;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final Random random;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        this.random = new Random();
    }

    @Override
    public List<Recipe> generateRecipes(Optional<String[]> usedRecipesArray, Optional<Integer> numberOfRecipes) {
        int recipesNumber = numberOfRecipes.orElse(5);
        List<Recipe> usedRecipes = usedRecipesArray.map(this::getUsedRecipes).orElseGet(ArrayList::new);
        if (recipesNumber <= usedRecipes.size()) {
            return usedRecipes;
        }
        RecipeModule recipeModule = new RecipeModule(usedRecipes);
        List<Recipe> generatedRecipes = new ArrayList<>();


        return this.recipeRepository.getAll();
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return this.recipeRepository.getAll();
    }

    private List<Recipe> getUsedRecipes(String[] usedRecipesArray) {
        List<Recipe> usedRecipes = new ArrayList<>();
        for (String id : usedRecipesArray) {
            Recipe recipe = this.recipeRepository.getRecipe(id);
            if (recipe != null) {
                usedRecipes.add(recipe);
            }
        }
        return usedRecipes;
    }

    /**
     * Checks if the proportions are correct
     * @param recipeModule
     * @param recipe
     * @return
     */
    private boolean isInProportion(RecipeModule recipeModule, Recipe recipe) {


        return false;
    }

    /**
     * Generated all recipes except the last one
     * @param cal
     * @return
     */
    private List<Recipe> generateRecipes(int cal) {
        return new ArrayList<>();
    }

}
