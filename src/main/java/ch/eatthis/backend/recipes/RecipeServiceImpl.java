package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import ch.eatthis.backend.recipes.model.RecipeModule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final Random random;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        this.random = new Random();
    }

    @Override
    public List<Recipe> generateRecipes(Optional<String[]> usedRecipesArray, Optional<Integer> numberOfRecipes, int calories) {
        int recipesNumber = numberOfRecipes.orElse(5);
        List<Recipe> usedRecipes = usedRecipesArray.map(this::getUsedRecipes).orElseGet(ArrayList::new);
        if (recipesNumber <= usedRecipes.size()) {
            return usedRecipes;
        }
        RecipeModule recipeModule = new RecipeModule(usedRecipes);
        List<Recipe> generatedRecipes = new ArrayList<>();


        return this.generateRecipes(calories, recipesNumber, usedRecipes);
    }

    @Override
    public List<Recipe> getAllRecipes(Integer skip) {
        int pageSize = 50;
        int offset = skip * pageSize;
        return this.recipeRepository.getAll(offset, pageSize);
    }

    @Override
    public List<Recipe> getNumberOfRecipes(Optional<Integer> numberOfRecipes) {
        List<Recipe> allRecipes = this.recipeRepository.getAll(random.nextInt(50) * 50, 50);
        List<Recipe> randomList = new ArrayList<>();
        if (numberOfRecipes.orElse(5) < allRecipes.size()) {
            Random random = new Random();
            for (int i = 0; i < numberOfRecipes.orElse(5); i++) {
                int index = random.nextInt(allRecipes.size());
                randomList.add(allRecipes.get(index));
            }
        } else {
            randomList = allRecipes;
        }
        return randomList;
    }

    @Override
    public String castToString(String[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
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
     *
     * @param recipeModule
     * @param recipe
     * @return Returns true if the proportions are correct and false if they aren't
     */
    private boolean isInProportion(RecipeModule recipeModule, RecipeModule generatedRecipeModule, Recipe recipe) {
        RecipeModule mergedRecipeModule = new RecipeModule();
        mergedRecipeModule.setFat(recipeModule.getFat() + generatedRecipeModule.getFat() + recipe.getFat_g());
        mergedRecipeModule.setCarbohydrate(recipeModule.getCarbohydrate() + generatedRecipeModule.getCarbohydrate() + recipe.getCarbohydrate_g());
        mergedRecipeModule.setProtein(recipeModule.getProtein() + generatedRecipeModule.getProtein() + recipe.getProtein_g());

        double allModules = mergedRecipeModule.getCarbohydrate() + mergedRecipeModule.getProtein() + mergedRecipeModule.getFat();
        double percentageCarbohydrate = mergedRecipeModule.getCarbohydrate() / allModules;
        double percentageFat = mergedRecipeModule.getFat() / allModules;
        double percentageProtein = mergedRecipeModule.getProtein() / allModules;

        if (!(percentageCarbohydrate >= 0.45 && percentageCarbohydrate <= 0.55)) return false;
        if (!(percentageFat >= 0.3 && percentageFat <= 0.35)) return false;
        if (!(percentageProtein >= 0.1 && percentageProtein <= 0.2)) return false;

        return true;
    }

    /**
     * Generated all recipes except the last one
     *
     * @param cal
     * @param recipesToGenerate Number of recipes to generate starting by 1
     * @return
     */
    private List<Recipe> generateRecipes(int cal, int recipesToGenerate, List<Recipe> usedRecipes) {
        List<Recipe> generatedRecipes = new ArrayList<>();
        double averageCalPerRecipe = (double) ((cal / recipesToGenerate) - 50);
        for (int i = 0; i < recipesToGenerate; i++) {
            int randomCal = random.nextInt(70);
            List<Recipe> recipesInRange = this.recipeRepository.getRecipeBetweenCalRange((int) averageCalPerRecipe + randomCal - 15, (int) averageCalPerRecipe + randomCal + 15);
            int randomIndex = random.nextInt(recipesInRange.size());
            Recipe recipe = recipesInRange.get(randomIndex);
            if (usedRecipes.contains(recipe)) {
                i--;
                continue;
            }
            generatedRecipes.add(recipe);
        }

        return generatedRecipes;
    }

}
