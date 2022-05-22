<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Favourite extends Model
{
    use HasFactory;

    public $timestamps = false;

    protected $fillable = [
        "recipeId",
        "userId"
    ];

    protected $primaryKey = ["recipeId", "userId"];

    public $incrementing = false;
}
