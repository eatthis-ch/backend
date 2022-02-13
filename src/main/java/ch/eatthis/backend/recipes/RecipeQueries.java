package ch.eatthis.backend.recipes;

public class RecipeQueries {

    public final static String allRecipes = "SELECT * FROM recipe ORDER BY id LIMIT :pageSize OFFSET :offset;";

    public final static String getRecipe = "SELECT * FROM recipe WHERE id = :id;";

    public final static String getRecipeBetweenCalString = "SELECT * From recipe WHERE energy_cal BETWEEN :lowerValue AND :higherValue;";

    public final static String getLowerThanRecipes = "SELECT * FROM recipe WHERE energy_cal <= :cal_value ORDER BY energy_cal DESC LIMIT 100;";

    public final static String getDefinedRecipes = "SELECT * FROM recipe WHERE energy_cal between :lowerCal and :higherCal and protein_percent " +
            "between :lowerProtein and :higherProtein AND fat_percent between :lowerFat and :higherFat and carbohydrate_percent between :lowerCarb and :higherCarb;";

}
