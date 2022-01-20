package ch.eatthis.backend.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeModule {

    private int protein, carbohydrate, fat;

    public RecipeModule(List<Recipe> recipes) {
        if (recipes.size() == 0) {
            protein = 0;
            carbohydrate = 0;
            fat = 0;
            return;
        }
        for (Recipe recipe : recipes) {
            protein=+ recipe.getProtein_g();
            carbohydrate=+ recipe.getCarbohydrate_g();
            fat=+ recipe.getFat_g();
        }
    }
}
