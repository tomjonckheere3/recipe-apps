<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateRecipesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('recipes', function (Blueprint $table) {
            $table->increments("recipeId");
            $table->integer("userId")->unsigned();
            $table->foreign("userId")->references("userId")->on("users")->onDelete("cascade");
            $table->string("recipeName");
            $table->string("mealType");
            $table->integer("servings");
            $table->integer("prepareTime");
            $table->integer("price");
            $table->char("description", 255);
            $table->longText("image");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('recipes');
    }
}
