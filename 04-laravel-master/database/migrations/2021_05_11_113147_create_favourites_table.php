<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFavouritesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('favourites', function (Blueprint $table) {
            $table->primary(["recipeId", "userId"]);
            $table->integer("recipeId")->unsigned();
            $table->foreign("recipeId")->references("recipeId")->on("recipes")->onDelete("cascade");
            $table->integer("userId")->unsigned();
            $table->foreign("userId")->references("userId")->on("users")->onDelete("cascade");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('favourites');
    }
}
