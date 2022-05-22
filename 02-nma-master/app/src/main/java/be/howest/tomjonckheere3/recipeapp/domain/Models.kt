package be.howest.tomjonckheere3.recipeapp.domain

import com.squareup.moshi.Json

data class NormalRecipe(
        val recipeId: Int,
        var recipeName: String,
        val servings: Int,
        val prepareTime: Int,
        val price: Int,
        val image: String
)

data class DetailedRecipe(
        val recipeName: String,
        val mealType: String,
        val servings: Int,
        val prepareTime: Int,
        val price: Int,
        val description: String,
        val image: String,
        val steps: List<Step>,
        val ingredients: List<Ingredient>,
        val comments: List<Comment>
)

data class Step(
        val step: Int,
        val direction: String
)

data class Ingredient(
      val ingredient: String
)

data class Comment(
        val userId: Int,
        val commentContent: String
)

data class Credentials(
        val username: String,
        val password: String
)

data class RecipeData(
        var userId: Int?,
        val recipeName: String,
        val mealType: String,
        val servings: Int,
        val prepareTime: Int,
        val price: Int,
        val description: String,
        val image: String?,
        val steps: List<Step>,
        val ingredients: List<Ingredient>
)

data class AddRecipeResponse(
        val recipeId: Int
)

data class RegisterData(
        val username: String,
        val password: String,
        val confirmPassword: String
)

data class LoginResponse(
        val jwt: String?,
        val userId: Int
)

data class CommentData(
        var userId: Int?,
        val commentContent: String
)

data class AddCommentResponse(
        val commentId: Int
)

data class FavouriteData(
        val recipeId: Int
)

data class AddFavouriteResponse(
        val success: String?,
        val error: String?
)

data class SuggestedNormalRecipes(
    val breakfast: NormalRecipe,
    val supper: NormalRecipe,
    val dinner: NormalRecipe
)


