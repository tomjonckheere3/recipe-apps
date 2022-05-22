package be.howest.tomjonckheere3.recipeapp.network

import be.howest.tomjonckheere3.recipeapp.domain.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val LARAVEL_LOCAL_BASE_URL = "http://192.168.10.10/api/"
private const val LARAVEL_HEROKU_BASE_URL = "https://cryptic-shore-59745.herokuapp.com/api/"
private const val NODE_JS_BASE_URL = "http://192.168.0.157:8888/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(LARAVEL_HEROKU_BASE_URL)
    .build()

interface RecipeApiService {
    @Headers("Content-Type: application/json")
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): DetailedRecipeDTO

    @Headers("Content-Type: application/json")
    @POST("recipes")
    suspend fun addRecipe(@Header("authorization") token: String, @Body recipeData: RecipeData): AddRecipeResponse

    @Headers("Content-Type: application/json")
    @POST("recipes/{id}/comments")
    suspend fun addComment(@Header("authorization") token: String, @Path("id") recipeId: Int, @Body commentData: CommentData) : AddCommentResponse

    @Headers("Content-Type: application/json")
    @POST("users/login")
    suspend fun loginUser(@Body credentials: Credentials): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun addUser(@Body registerData: RegisterData): LoginResponse

    @Headers("Content-Type: application/json")
    @GET("users/{id}")
    suspend fun getUser(@Header("authorization") token: String ,@Path("id") userId: Int): User

    @Headers("Content-Type: application/json")
    @GET("users/{id}/recipes")
    suspend fun getRecipesOfUser(@Header("authorization") token: String, @Path("id") userId: Int): List<Recipe>

    @Headers("Content-Type: application/json")
    @GET("users/{id}/favourites")
    suspend fun getFavouritesOfUser(@Header("authorization") token: String, @Path("id") userId: Int): List<Recipe>

    @Headers("Content-Type: application/json")
    @POST("users/{id}/favourites")
    suspend fun addFavourite(@Header("authorization") token: String, @Path("id") userId: Int, @Body favouriteData: FavouriteData) : AddFavouriteResponse
}

object RecipeApi {
    val retrofitService : RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}