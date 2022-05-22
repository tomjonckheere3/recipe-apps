package be.howest.tomjonckheere3.recipeapp.network

import be.howest.tomjonckheere3.recipeapp.database.DatabaseFavouriteRecipe
import be.howest.tomjonckheere3.recipeapp.database.DatabaseMyRecipe
import be.howest.tomjonckheere3.recipeapp.database.DatabaseRecipe
import be.howest.tomjonckheere3.recipeapp.database.DatabaseUser
import be.howest.tomjonckheere3.recipeapp.domain.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class Recipe(
        val recipeId: Int,
        val userId: Int,
        val mealType: String,
        val recipeName: String,
        val servings: Int,
        val prepareTime: Int,
        val price: Int,
        val image: String,
        val description: String
)

fun Recipe.asNormalRecipe(): NormalRecipe {
        return NormalRecipe(
                recipeId, recipeName, servings, prepareTime, price, image
        )
}

fun List<Recipe>.asDatabaseModel(): List<DatabaseRecipe> {
        return map {
                DatabaseRecipe(
                        recipeId = it.recipeId,
                        userId = it.userId,
                        recipeName = it.recipeName,
                        mealType = it.mealType,
                        servings = it.servings,
                        prepareTime = it.prepareTime,
                        price = it.price,
                        image = it.image,
                        description = it.description
                )
        }
}

fun List<Recipe>.asDatabaseMyRecipe(): List<DatabaseMyRecipe> {
        return map {
                DatabaseMyRecipe(
                        recipeId = it.recipeId,
                        userId = it.userId,
                        recipeName = it.recipeName,
                        mealType = it.mealType,
                        servings = it.servings,
                        prepareTime = it.prepareTime,
                        price = it.price,
                        image = it.image,
                        description = it.description
                )
        }
}

fun List<Recipe>.asDatabaseFavouriteRecipe(): List<DatabaseFavouriteRecipe> {
        return map {
                DatabaseFavouriteRecipe(
                        recipeId = it.recipeId,
                        userId = it.userId,
                        recipeName = it.recipeName,
                        mealType = it.mealType,
                        servings = it.servings,
                        prepareTime = it.prepareTime,
                        price = it.price,
                        image = it.image,
                        description = it.description
                )
        }
}

data class DetailedRecipeDTO(
        val recipeId: Int,
        val userId: Int,
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

fun DetailedRecipeDTO.asDomainModel(): DetailedRecipe {
        return DetailedRecipe(
                recipeName = recipeName,
                mealType = mealType,
                servings = servings,
                prepareTime = prepareTime,
                price = price,
                description = description,
                image = image,
                steps = steps,
                ingredients = ingredients,
                comments = comments
        )
}

@JsonClass(generateAdapter = true)
data class User(
       val userId: Int,
       val username: String,
       val profilePicture: String?
)

fun User.asDatabaseModel(token: String): DatabaseUser {
        return DatabaseUser(
                userId = userId,
                token = token,
                username = username
        )
}


