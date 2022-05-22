<?php

namespace App\Http\Controllers;

use App\Modules\Notifications\Services\NotificationService;
use App\Modules\Recipes\Services\RecipeService;
use App\Modules\Users\Services\UserService;
use Illuminate\Http\Request;

class RecipeApiController extends Controller
{
    public function __construct(RecipeService $recipeService, NotificationService $notificationService, UserService $userService)
    {
        $this->recipeService = $recipeService;
        $this->notificationService = $notificationService;
        $this->userService = $userService;
    }

    public function getRecipes(Request $request) {
        return $this->recipeService->getRecipes($request);
    }

    public function getRecipeById($recipeId) {
        return $this->recipeService->getRecipeById($recipeId);
    }

    public function getSuggestions() {
        return $this->recipeService->getSuggestions();
    }

    public function addRecipe(Request $request) {
        return $this->recipeService->addRecipe($request);
    }

    public function getRecipesOfUser($userId) {
        return $this->recipeService->getRecipesOfUser($userId);
    }

    public function removeRecipe($userId, $recipeId) {
        return $this->recipeService->removeRecipe($userId, $recipeId);
    }

    public function getFavouritesOfUser($userId) {
        return $this->recipeService->getFavouritesOfUser($userId);
    }

    public function removeFavouriteRecipe($userId, $recipeId) {
        return $this->recipeService->removeFavouriteRecipe($userId, $recipeId);
    }

    public function addFavouriteRecipe($userId, Request $request) {
        $responseRecipe = $this->recipeService->addFavouriteRecipe($userId, $request);
        if ($request->sendNotification) {
            $favouritedRecipe = $this->getRecipeById($request->recipeId);
            $username = $this->userService->getUserById($userId)->username;
            $this->notificationService->sendFavouritedNotification($username, $favouritedRecipe);
        }
        return $responseRecipe;
    }
}
