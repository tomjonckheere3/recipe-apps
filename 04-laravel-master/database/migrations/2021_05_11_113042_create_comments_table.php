<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCommentsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('comments', function (Blueprint $table) {
            $table->increments("commentId");
            $table->integer("recipeId")->unsigned();
            $table->foreign("recipeId")->references("recipeId")->on("recipes")->onDelete("cascade");
            $table->integer("userId")->unsigned();
            $table->foreign("userId")->references("userId")->on("users")->onDelete("cascade");
            $table->char("commentContent", 255);
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('comments');
    }
}
