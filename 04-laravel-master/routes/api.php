<?php

use App\Http\Controllers\UserApiController;
use App\Http\Controllers\RecipeApiController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use Monolog\Handler\RotatingFileHandler;
use App\Http\Controllers\CommentApiController;
use App\Http\Controllers\NotificationApiController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/



Route::prefix('recipes')->group(function () {
    Route::get("/", [RecipeApiController::class, "getRecipes"])->name("api.recipes");
    Route::get("/suggestions", [RecipeApiController::class, "getSuggestions"])->name("api.recipes.suggestions");
    Route::get("/{recipeId}", [RecipeApiController::class, "getRecipeById"])->name("api.recipes.recipeId");

    Route::middleware(["auth.jwt"])->group(
        function () {
            Route::post("/", [RecipeApiController::class, "addRecipe"])->name("api.recipes.add");
            Route::post("/{recipeId}/comments", [CommentApiController::class, "addComment"])->name("api.recipes.comments.add");
        }
    );
});

Route::prefix('users')->group(function () {
    Route::post("/", [UserApiController::class, "register"])->name("api.users.add");
    Route::post("/login", [UserApiController::class, "login"])->name("api.users.login");

    Route::post("{userId}/notifications/register", [NotificationApiController::class, "registerUserForNotifications"])->name("api.users.notifications.register");

    Route::middleware(['auth.jwt'])->group(
        function () {
            Route::get("/{userId}", [UserApiController::class, "getUserById"])->name("api.users.userId");
            Route::get("/{userId}/recipes", [RecipeApiController::class, "getRecipesOfUser"])->name("api.users.recipes");
            Route::delete("/{userId}/recipes/{recipeId}", [RecipeApiController::class, "removeRecipe"])->name("api.users.recipes.remove");
            Route::get("/{userId}/favourites", [RecipeApiController::class, "getFavouritesOfUser"])->name("api.users.favourites");
            Route::delete("/{userId}/favourites/{recipeId}", [RecipeApiController::class, "removeFavouriteRecipe"])->name("api.users.favourites.remove");
            Route::post("/{userId}/favourites", [RecipeApiController::class, "addFavouriteRecipe"])->name("api.users.favourites.add");
            Route::get("/{userId}/notifications", [NotificationApiController::class, "getNotificationsOfUser"])->name("api.users.notifications");
        }
    );
});
