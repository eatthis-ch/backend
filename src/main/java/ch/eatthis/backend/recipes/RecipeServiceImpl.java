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
        System.out.println();
        if (recipesNumber <= usedRecipes.size()) {
            return new ArrayList<>();
        }
        List<Recipe> generatedRecipes = this.generateRecipes(calories, recipesNumber, usedRecipes);
        List<Recipe> mergedRecipes = generatedRecipes;
        mergedRecipes.addAll(usedRecipes);
        RecipeModule recipeModule = new RecipeModule(mergedRecipes);
        while (!isInProportion(recipeModule) && generatedRecipes.size() + usedRecipes.size() != recipesNumber) {

        }

        return generatedRecipes;
    }

    @Override
    public List<Recipe> getAllRecipes(Integer skip) {
        int pageSize = 50;
        int offset = skip * pageSize;
        return this.recipeRepository.getAll(offset, pageSize);
    }

    @Override
    public Recipe getById(String id) {
        return this.recipeRepository.getRecipe(id);
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
    private boolean isInProportion(RecipeModule recipeModule, Recipe recipe) {
        RecipeModule mergedRecipeModule = new RecipeModule();
        mergedRecipeModule.setFat(recipeModule.getFat() + recipe.getFat_g());
        mergedRecipeModule.setCarbohydrate(recipeModule.getCarbohydrate() + recipe.getCarbohydrate_g());
        mergedRecipeModule.setProtein(recipeModule.getProtein() + recipe.getProtein_g());
        return isInProportion(mergedRecipeModule);
    }

    /**
     * Checks if the proportions are correct
     *
     * @param recipeModule
     * @return Returns true if the proportions are correct and false if they aren't
     */
    private boolean isInProportion(RecipeModule recipeModule) {

        double allModules = recipeModule.getCarbohydrate() + recipeModule.getProtein() + recipeModule.getFat();
        double percentageCarbohydrate = recipeModule.getCarbohydrate() / allModules;
        double percentageFat = recipeModule.getFat() / allModules;
        double percentageProtein = recipeModule.getProtein() / allModules;

        if (!(percentageCarbohydrate >= 0.45 && percentageCarbohydrate <= 0.55)) return false;
        if (!(percentageFat >= 0.3 && percentageFat <= 0.35)) return false;
        if (!(percentageProtein >= 0.1 && percentageProtein <= 0.2)) return false;

        return true;
    }

    /**
     * Checks if the proportions are correct
     *
     * @param recipes
     * @return Returns true if the proportions are correct and false if they aren't
     */
    private boolean isInProportion(List<Recipe> recipes) {
        RecipeModule recipeModule = new RecipeModule(recipes);
        return isInProportion(recipeModule);
    }

    private int calculateModuleDifference(RecipeModule usedRecipeModule, Recipe recipe) {
        RecipeModule mergedRecipeModule = new RecipeModule();
        mergedRecipeModule.setProtein(usedRecipeModule.getProtein() + recipe.getProtein_g());
        mergedRecipeModule.setCarbohydrate(usedRecipeModule.getCarbohydrate() + recipe.getCarbohydrate_g());
        mergedRecipeModule.setFat(usedRecipeModule.getFat() + recipe.getFat_g());

        double allModules = mergedRecipeModule.getCarbohydrate() + mergedRecipeModule.getProtein() + mergedRecipeModule.getFat();
        double percentageCarbohydrate = mergedRecipeModule.getCarbohydrate() / allModules;
        double percentageFat = mergedRecipeModule.getFat() / allModules;
        double percentageProtein = mergedRecipeModule.getProtein() / allModules;

        double overAllDifference = 0;
        if (percentageCarbohydrate < 0.45) {
            // Calculate difference and add it to overAllDifference
        } else if (percentageCarbohydrate > 0.55) {
            // Calculate difference and add it to overAllDifference
        }
        return 0;
    }

    /**
     * Generated all recipes except the last one
     *
     * @param cal
     * @param recipesToGenerate Number of recipes to generate
     * @return
     */
    private List<Recipe> generateRecipes(int cal, int recipesToGenerate, List<Recipe> usedRecipes) {
        List<Recipe> generatedRecipes = new ArrayList<>();
        if (recipesToGenerate - usedRecipes.size() - 1 >= 1) {
            generatedRecipes = generateRandomRecipes(cal, recipesToGenerate, usedRecipes);
        }
        List<Recipe> mergedRecipes = new ArrayList<>();
        mergedRecipes.addAll(generatedRecipes);
        mergedRecipes.addAll(usedRecipes);
        if (isInProportion(mergedRecipes)) {
            return generatedRecipes;
        } else {
            RecipeModule recipeModule = new RecipeModule();
            int availableCals = cal;
            for (Recipe recipe : mergedRecipes) {
                availableCals-= recipe.getEnergy_cal();
                recipeModule.setCarbohydrate(recipeModule.getCarbohydrate() + recipe.getCarbohydrate_g());
                recipeModule.setFat(recipeModule.getFat() + recipe.getFat_g());
                recipeModule.setProtein(recipeModule.getProtein() + recipe.getProtein_g());
            }

//            Recipe recipe = generatedRecipeForModule(availableCals, )
        }
        return generatedRecipes;
    }

    private List<Recipe> generateRandomRecipes(int cal, int recipesToGenerate, List<Recipe> usedRecipes) {
        List<Recipe> generatedRecipes = new ArrayList<>();
        double averageCalPerRecipe = (double) ((cal / recipesToGenerate));
        List<Recipe> recipesInGeneratedRanged = new ArrayList<>();
        while (generatedRecipes.size() + usedRecipes.size() < recipesToGenerate) {
            int randomCal = random.nextInt(70);
            if (recipesInGeneratedRanged.size() == 0) {
                recipesInGeneratedRanged = this.recipeRepository.getRecipeBetweenCalRange((int) averageCalPerRecipe - randomCal, (int) averageCalPerRecipe + randomCal);
                recipesInGeneratedRanged.removeIf(recipe -> usedRecipes.contains(recipe) || generatedRecipes.contains(recipe));
                if (recipesInGeneratedRanged.size() == 0) {
                    System.out.println("PROBLEM!");
                    recipesInGeneratedRanged = this.recipeRepository.getLowerThanRecipes((int) averageCalPerRecipe + randomCal - 25);
                    recipesInGeneratedRanged.removeIf(recipe -> usedRecipes.contains(recipe) || generatedRecipes.contains(recipe));
                    if (recipesInGeneratedRanged.size() != 0) {
                        generatedRecipes.add(recipesInGeneratedRanged.get(0));
                        recipesInGeneratedRanged.clear();
                        continue;
                    } else {
                        return recipesInGeneratedRanged;
                    }
                }
            }
            int randomIndex = random.nextInt(recipesInGeneratedRanged.size());
            generatedRecipes.add(recipesInGeneratedRanged.get(randomIndex));
            recipesInGeneratedRanged.clear();
        }
        return generatedRecipes;
    }

    private Recipe generatedRecipeForModule(int cal, RecipeModule usedRecipeModule) {
        int randomCal = random.nextInt(70);
        List<Recipe> repositoryRecipes = this.recipeRepository.getRecipeBetweenCalRange(cal - randomCal, cal + randomCal);
        Recipe bestResult = null;
        int currentMinimalDifference = 0;
        for (Recipe recipe : repositoryRecipes) {
            if (isInProportion(usedRecipeModule, recipe)) {
                return recipe;
            } else {

            }
        }
        return bestResult;
    }

    private List<Recipe> getNumberOfRecipes(Optional<Integer> numberOfRecipes) {
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


}
