package ch.eatthis.backend.recipes;

import ch.eatthis.backend.recipes.model.Recipe;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<Recipe> generateRecipes(@RequestParam(required = false) Optional<String[]> usedRecipes, @RequestParam(required = false) Optional<Integer> numberOfRecipes, @RequestParam() Optional<Integer> calories) {
        usedRecipes.ifPresent(strings -> System.out.println(this.recipeService.castToString(strings)));
        calories.ifPresent(integer -> System.out.println(integer));

//        return this.recipeService.generateRecipes(usedRecipes, numberOfRecipes);
        return this.recipeService.getNumberOfRecipes(numberOfRecipes);
    }

    @GetMapping
    public List<Recipe> getAll() {
        return this.recipeService.getAllRecipes();
    }

}
