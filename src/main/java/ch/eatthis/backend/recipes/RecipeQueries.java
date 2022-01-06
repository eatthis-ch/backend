package ch.eatthis.backend.recipes;

public class RecipeQueries {

    public final static String allRecipes = "SELECT * FROM recipe;";

    public final static String getRecipe = "SELECT * FROM recipe WHERE id = :id;";

    public final static String getRecipeBetweenCalString = "SELECT * From recipe WHERE energy_cal BETWEEN :lowerValue AND :higherValue;";

}
