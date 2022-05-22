<?php
namespace App\Modules\Recipes\Services;

use App\Models\Favourite;
use App\Models\Ingredient;
use App\Models\Recipe;
use App\Models\Step;
use App\Models\Comment;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
use stdClass;

class RecipeService {

    protected $rules = [
        "userId" => "required|integer",
        "recipeName" => "required|string",
        "servings" => "required|integer|min:1",
        "prepareTime" => "required|integer|min:1",
        "price" => "required|integer|min:1",
        "description" => "required|string",
        "image" => "required|string",
    ];

    public function __construct(Recipe $recipeModel, Ingredient $ingredientModel, Step $stepModel, Favourite $favouriteModel, Comment $commentModel)
    {
        $this->recipeModel = $recipeModel;
        $this->ingredientModel = $ingredientModel;
        $this->stepModel = $stepModel;
        $this->favouriteModel = $favouriteModel;
        $this->commentModel = $commentModel;
    }

    public function getRecipes(Request $request){

        $filters = new stdClass;

        if ($mealType = $request->query("mealType")) {
            Log::info("here");
            $filters->mealType = $mealType;
        }

        $sortOrder = "asc";
        $sortBy = "recipeId";
        $sortQueryParam = $request->query("sort");

        //Checks if recipe model has column with name of $sortBy.
        if ($this->recipeModel->getConnection()
                              ->getSchemaBuilder()
                              ->hasColumn($this->recipeModel->getTable(), $sortQueryParam)) {

            $sortBy = $request->query("sort");

            if ($request->query("sortOrder") == "desc") {
                $sortOrder = "desc";
            }
        }

        $queryBuilder = $this->recipeModel->newQuery();
        foreach ($filters as $filterKey => $filterValue) {
            Log::info($filterKey . " + " . $filterValue);
            $queryBuilder->where($filterKey, $filterValue);
        }

        $recipes = $queryBuilder->orderBy($sortBy, $sortOrder)->get();

        return $recipes;
    }

    public function addRecipe(Request $request){
        $this->errors = null;
        $validator = Validator::make($request->all(), $this->rules);

        if($validator->fails()) {
            $this->errors = $validator->errors();
            Log::info($this->errors);
            return response()->json($validator->errors()->toJson(), 400);
        }

        $result = $this->recipeModel->create($request->all());
        $recipeId = $result->recipeId;

        foreach($request->ingredients as $ingredient) {
            $ingredient["recipeId"] = $recipeId;
            Ingredient::create($ingredient);
        }

        foreach($request->steps as $step) {
            $step["recipeId"] = $recipeId;
            Step::create($step);
        }

        return $this->getRecipeById($result->recipeId);
    }

    public function getRecipeById($recipeId) {
        $recipe = $this->recipeModel->find($recipeId);
        $recipe["ingredients"] = $this->ingredientModel->where("recipeId", $recipeId)->get();
        $recipe["steps"] = $this->stepModel->where("recipeId", $recipeId)->get();
        $recipe["comments"] = $this->commentModel->where("recipeId", $recipeId)->get();
        return $recipe;
    }

    public function getSuggestions() {
        $breakfast =  $this->recipeModel::where("mealType", "dinner")->inRandomOrder()->first();
        $supper = $this->recipeModel::where("mealType", "supper")->inRandomOrder()->first();
        $dinner = $this->recipeModel::where("mealType", "dinner")->inRandomOrder()->first();
        return response()->json([
            "breakfast" => $breakfast,
            "supper" => $supper,
            "dinner" => $dinner
        ], 200);
    }

    public function getRecipesOfUser($userId) {
        $recipesOfUser = $this->recipeModel::where("userId", "=", $userId)
                                            ->get();
        return $recipesOfUser;
    }

    public function removeRecipe($userId, $recipeId) {

        if (!$recipe = $this->getRecipeById($recipeId)) {
            return response()->json([
                "error" => "This recipe does not exist"
            ], 404);
        }

        if ($userId != $recipe->userId) {
            return response()->json([
                "error" => "This user does not own this recipe"
            ], 401);
        }

        $this->recipeModel::destroy($recipeId);

        return response()->json("Removed recipe", 200);
    }

    public function getFavouritesOfUser($userId) {
        $favouritesOfUser = $this->favouriteModel::join("recipes", "favourites.recipeId", "=", "recipes.recipeId")
                                                ->where("favourites.userId", "=", $userId)
                                                ->select("recipes.*")
                                                ->get();

        return $favouritesOfUser;
    }

    public function removeFavouriteRecipe($userId, $recipeId) {

        $favourite = $this->favouriteModel::where("userId", "=", $userId)
                                            ->where("recipeId", "=", $recipeId)
                                            ->first();

        if (!$favourite) {
            return response()->json([
                "error" => "This favourite does not exist"
            ], 404);
        }

        $this->favouriteModel::where("userId", "=", $userId)
                            ->where("recipeId", "=", $recipeId)
                            ->delete();

        return response()->json("Removed favourite recipe", 200);
    }

    public function addFavouriteRecipe($userId, Request $request) {

        Log::info($userId);

        $recipeId = $request->recipeId;

        if (!$recipe = $this->getRecipeById($recipeId)) {
            return response()->json([
                "error" => "User or recipe does not exist"
            ], 200);
        }

        $duplicates = $this->favouriteModel->where("userId", $userId)
                                 ->where("recipeId", $recipeId)
                                 ->get();

        if (!$duplicates->isEmpty()) {
            return response()->json([
                "error" => "Recipe is already a favourite"
            ], 200);
        }

        $this->favouriteModel::create(["userId" => $userId, "recipeId" => $recipeId]);

        return response()->json([
            "success" => "Added recipe to favourites"
        ], 201);
    }

    public function getErrors(){
        return $this->errors;
    }

    public function hasErrors(){
        return !is_null($this->errors);
    }

}
