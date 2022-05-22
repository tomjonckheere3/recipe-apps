<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Recipe extends Model
{
    use HasFactory;

    public $timestamps = false;

    protected $fillable = [
        "userId",
        "recipeName",
        "mealType",
        "servings",
        "prepareTime",
        "price",
        "description",
        "image"
    ];

    protected $primaryKey = "recipeId";
}
