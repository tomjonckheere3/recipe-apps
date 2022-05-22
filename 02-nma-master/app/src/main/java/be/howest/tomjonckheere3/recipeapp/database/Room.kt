package be.howest.tomjonckheere3.recipeapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.GET

@Dao
interface RecipeDao {

    @Query("select * from databaserecipe")
    fun getRecipes(): LiveData<List<DatabaseRecipe>>

    @Query("select * from databaserecipe where mealType = 'breakfast' order by random() limit 1")
    fun getRandomBreakfastRecipe(): DatabaseRecipe

    @Query("select * from databaserecipe where mealType = 'supper' order by random() limit 1")
    fun getRandomSupperRecipe(): DatabaseRecipe

    @Query("select * from databaserecipe where mealType = 'dinner' order by random() limit 1")
    fun getRandomDinnerRecipe(): DatabaseRecipe

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<DatabaseRecipe>)
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentUser(currentUser: DatabaseUser)

    @Query("delete from databaseuser")
    fun deleteCurrentUser()

    @Query("select * from databaseuser limit 1")
    fun getCurrentUser(): DatabaseUser
}

@Dao
interface MyRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyRecipes(recipes: List<DatabaseMyRecipe>)

    @Query("select * from databasemyrecipe")
    fun getMyRecipes() : LiveData<List<DatabaseRecipe>>

    @Query("delete from databasemyrecipe")
    fun deleteMyRecipes()
}

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteRecipes(recipes: List<DatabaseFavouriteRecipe>)

    @Query("select * from databasefavouriterecipe")
    fun getFavouriteRecipes() : LiveData<List<DatabaseRecipe>>

    @Query("delete from databasefavouriterecipe")
    fun deleteFavourites()
}

@Database(entities = [DatabaseRecipe::class, DatabaseUser::class, DatabaseMyRecipe::class, DatabaseFavouriteRecipe::class], version = 1)
abstract class RecipesDatabase: RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val userDao: UserDao
    abstract val myRecipeDao: MyRecipeDao
    abstract val favouritesDao: FavouriteDao
}

private lateinit var INSTANCE: RecipesDatabase

fun getDatabase(context: Context): RecipesDatabase {
    synchronized(RecipesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                RecipesDatabase::class.java,
                "recipes").build()
        }
    }
    return INSTANCE
}