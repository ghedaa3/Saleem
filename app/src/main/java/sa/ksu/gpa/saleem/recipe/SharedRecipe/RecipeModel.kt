package sa.ksu.gpa.saleem.recipe.SharedRecipe


data class RecipeModel(var recipeId: String,
                       var recipeTitle: String,
                       var recipePicture: String?,
                       var recipeCalories:String,
                       var recipeDate:String) {
    constructor() : this("", "", null,"","")

}