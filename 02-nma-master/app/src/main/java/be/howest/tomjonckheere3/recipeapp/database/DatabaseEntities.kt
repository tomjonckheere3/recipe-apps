package be.howest.tomjonckheere3.recipeapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.howest.tomjonckheere3.recipeapp.domain.NormalRecipe

@Entity
data class DatabaseRecipe (
        @PrimaryKey
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

fun DatabaseRecipe.asNormalRecipe(): NormalRecipe {
        return NormalRecipe(
                recipeId = recipeId,
                recipeName = recipeName,
                servings = servings,
                prepareTime = prepareTime,
                price = price,
                image = image
        )
}

fun List<DatabaseRecipe>.asNormalRecipes(): List<NormalRecipe> {
        return map {
                NormalRecipe(
                        recipeId = it.recipeId,
                        recipeName = it.recipeName,
                        servings = it.servings,
                        prepareTime = it.prepareTime,
                        price = it.price,
                        image = it.image
                )
        }
}

@Entity
data class DatabaseUser(
        @PrimaryKey
        val userId: Int,
        val token: String,
        val username: String
)

@Entity
data class DatabaseFavouriteRecipe(
        @PrimaryKey
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

@Entity
data class DatabaseMyRecipe(
        @PrimaryKey
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
