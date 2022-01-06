package ch.eatthis.backend.recipes;

public class RecipeQueries {

    public final static String getAll = "SELECT * FROM recipe;";

    public final static String getRecipe = "SELECT * FROM recipe WHERE id = :id";

}
