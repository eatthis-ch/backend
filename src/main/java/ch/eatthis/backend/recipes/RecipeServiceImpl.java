package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
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
    public List<Recipe> generateRecipes(Optional<String[]> usedRecipes, Optional<Integer> numberOfRecipes) {
        int neededRecipes = 0;
        if (usedRecipes.isPresent()) {
            neededRecipes = usedRecipes.get().length - numberOfRecipes.orElse(5);
            if (neededRecipes < 1) {
                // TODO: Add own conflict exception
                throw new RuntimeException();
            }
        } else {
            neededRecipes = numberOfRecipes.orElse(5);
        }
        List<Recipe> allRecipes = new ArrayList<>(); // usedRecipes.map(this::filterUsedRecipes).orElseGet(this.recipeRepository::findAll);
        // TODO: Throw exception if no recipes are getting loaded
        List<Recipe> selectedRecipes = new ArrayList<>();
        for (int i = 1; i < neededRecipes; i++) {
            int index = random.nextInt(allRecipes.size());
            selectedRecipes.add(allRecipes.get(index));
            allRecipes.remove(index);
        }
        return selectedRecipes;
    }

    private List<Recipe> filterUsedRecipes(String[] usedRecipes) {
        List<Recipe> allRecipes = new ArrayList<>(); // this.recipeRepository.findAll();
        Iterator<Recipe> iterator = allRecipes.iterator();
        for (String usedRecipe : usedRecipes) {
            String recipeId = usedRecipe;
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                if (recipe.getId() == recipeId) {
                    iterator.remove();
                    break;
                }
            }
        }
        return allRecipes;
    }
}
