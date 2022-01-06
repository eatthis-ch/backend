package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 409, message = "Conflict")
})
@RequestMapping("api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/generate")
    public List<Recipe> generateRecipes(Optional<String[]> usedRecipes, Optional<Integer> numberOfRecipes) {
        return this.recipeService.generateRecipes(usedRecipes, numberOfRecipes);
    }

    @GetMapping
    public List<Recipe> getAll() {
        return this.recipeService.getAllRecipes();
    }

}
