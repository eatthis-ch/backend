package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import ch.eatthis.backend.recipes.model.RecipeModule;
import ch.eatthis.backend.recipes.model.RecipeModuleType;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        List<Recipe> generatedRecipes = new ArrayList<>();
        if (recipesNumber - usedRecipes.size() - 1 >= 1) {
            generatedRecipes = generateRandomRecipes(calories, recipesNumber - 1, usedRecipes);
        }
        List<Recipe> mergedRecipes = new ArrayList<>();
        mergedRecipes.addAll(generatedRecipes);
        mergedRecipes.addAll(usedRecipes);
        generatedRecipes.add(generateLastRecipe(mergedRecipes, calories));

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

    private List<Recipe> generateRandomRecipes(int cal, int recipesToGenerate, List<Recipe> usedRecipes) {
        List<Recipe> generatedRecipes = new ArrayList<>();
        double averageCalPerRecipe = (double) ((cal / recipesToGenerate));
        List<Recipe> recipesInGeneratedRanged = new ArrayList<>();
        List<Recipe> mergedRecipes = new ArrayList<>();
        mergedRecipes.addAll(usedRecipes);
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
                        mergedRecipes.add(recipesInGeneratedRanged.get(0));
                        recipesInGeneratedRanged.clear();
                        continue;
                    } else {
                        return recipesInGeneratedRanged;
                    }
                }
            }
            int randomIndex = random.nextInt(recipesInGeneratedRanged.size());
            generatedRecipes.add(recipesInGeneratedRanged.get(randomIndex));
            mergedRecipes.add(recipesInGeneratedRanged.get(randomIndex));
            recipesInGeneratedRanged.clear();

            if(generatedRecipes.size() + usedRecipes.size() == recipesToGenerate) {
                double averageOfFatPercentage = 0D;
                double averageOfProteinPercentage = 0D;
                double averageOfCarbohydratePercentage = 0D;
                for (Recipe recipe : mergedRecipes) {
                    averageOfFatPercentage += recipe.getFat_percent();
                    averageOfProteinPercentage += recipe.getProtein_percent();
                    averageOfCarbohydratePercentage += recipe.getCarbohydrate_percent();
                }
                averageOfFatPercentage = averageOfFatPercentage / mergedRecipes.size();
                averageOfProteinPercentage = averageOfProteinPercentage / mergedRecipes.size();
                averageOfCarbohydratePercentage = averageOfCarbohydratePercentage / mergedRecipes.size();
                if (!(isInPercentageRange(RecipeModuleType.FAT, averageOfFatPercentage, 0.1) &&
                        isInPercentageRange(RecipeModuleType.PROTEIN, averageOfProteinPercentage, 0.1) &&
                        isInPercentageRange(RecipeModuleType.CARBOHYDRATE, averageOfCarbohydratePercentage, 0.1))) {
                    generatedRecipes.clear();
                    mergedRecipes.clear();
                    mergedRecipes.addAll(usedRecipes);
                }
            }
        }
        return generatedRecipes;
    }

    private Recipe generateLastRecipe(List<Recipe> utilizedRecipes, int cal) {
        int usedCal = 0;
        double sumOfFatPercentage = 0D;
        double sumOfProteinPercentage = 0D;
        double sumOfCarbohydratePercentage = 0D;
        for (Recipe recipe : utilizedRecipes) {
            usedCal+= recipe.getEnergy_cal();
            sumOfFatPercentage += recipe.getFat_percent();
            sumOfProteinPercentage += recipe.getProtein_percent();
            sumOfCarbohydratePercentage += recipe.getCarbohydrate_percent();
        }
        if (usedCal >= cal) {
            throw new RuntimeException("ERROR System.100");
        }
        int availableCal = cal - usedCal;
        double searchingFatPercent = calculateNeededPercentage(RecipeModuleType.FAT, sumOfFatPercentage, utilizedRecipes.size());
        double searchingProteinPercent = calculateNeededPercentage(RecipeModuleType.PROTEIN, sumOfProteinPercentage, utilizedRecipes.size());
        double searchingCarbPercent = calculateNeededPercentage(RecipeModuleType.CARBOHYDRATE, sumOfCarbohydratePercentage, utilizedRecipes.size());
        double percentRange = 0.05D;
        List<Recipe> queriedRecipes = this.recipeRepository.getDefinedRecipes(availableCal, random.nextInt(70), searchingFatPercent, searchingProteinPercent, searchingCarbPercent, percentRange);
        queriedRecipes.removeIf(utilizedRecipes::contains);
        while (queriedRecipes.size() == 0) {
            percentRange += 0.05D;
            queriedRecipes = this.recipeRepository.getDefinedRecipes(availableCal, random.nextInt(70), searchingFatPercent, searchingProteinPercent, searchingCarbPercent, percentRange);
        }
        return queriedRecipes.get(random.nextInt(queriedRecipes.size()));
    }

    private double calculateNeededPercentage(RecipeModuleType recipeModuleType, double sumOfPercentage, int countRecipes) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        switch (recipeModuleType) {
            case FAT:
                return percentageIsBetween(0.3D, 0.35D, sumOfPercentage, countRecipes, df);
            case PROTEIN:
                return percentageIsBetween(0.1D, 0.2D, sumOfPercentage, countRecipes, df);
            case CARBOHYDRATE:
                return percentageIsBetween(0.45D, 0.55D, sumOfPercentage, countRecipes, df);
            default:
                return 0D;
        }
    }

    private double percentageIsBetween(double lowerValue, double higherValue, double sumOfPercentage, int countRecipes) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return percentageIsBetween(lowerValue, higherValue, sumOfPercentage, countRecipes, df);
    }

    private double percentageIsBetween(double lowerValue, double higherValue, double sumOfPercentage, int countRecipes, DecimalFormat decimalFormat) {
        double startValue = 0D;
        for (int i = 0; i < 1000; i++) {
            double currentPercent = sumOfPercentage + startValue / countRecipes + 1;
            currentPercent = Double.parseDouble(decimalFormat.format(currentPercent));
            if (Math.abs(lowerValue - currentPercent) == Math.abs(higherValue - currentPercent)) {
                return startValue;
            }
            startValue += 0.01D;
        }
        return 0D;
    }

    private boolean isInPercentageRange(RecipeModuleType recipeModuleType, double percentage, double difference) {
        switch (recipeModuleType) {
            case FAT:
                return percentage >= 0.3D - difference || percentage <= 0.35D + difference;
            case PROTEIN:
                return percentage >= 0.1D - difference || percentage <= 0.2D + difference;
            case CARBOHYDRATE:
                return percentage >= 0.45D - difference || percentage <= 0.55D + difference;
            default:
                return false;
        }
    }
}
