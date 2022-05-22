<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateStepsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('steps', function (Blueprint $table) {
            $table->primary(["recipeId", "step"]);
            $table->integer("recipeId")->unsigned();
            $table->foreign("recipeId")->references("recipeId")->on("recipes")->onDelete("cascade");
            $table->integer("step");
            $table->string("direction");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('steps');
    }
}
