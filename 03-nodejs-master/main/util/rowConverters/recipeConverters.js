'use strict';

function row2recipe(row) {
    return {
        recipeId: row.recipeId,
        userId: row.userId,
        recipeName: row.recipeName,
        mealType: row.mealType,
        servings: row.servings,
        prepareTime: row.prepareTime,
        price: row.price,
        description: row.description,
        image: row.image
    }
}

function ingredients2values(ingredients, recipeId) {
    let values = [];
    ingredients.forEach(ingredient => {
        values.push([recipeId, ingredient.ingredient]);
    })
    return values;
}

function steps2values(steps, recipeId) {
    let values = [];
    steps.forEach(step => {
        values.push([recipeId, step.step, step.direction]);
    });
    return values;
}

function row2comment(row) {
    return {
        userId: row.userId,
        commentContent: row.commentContent,
    }
}

function row2step(row) {
    return {
        step: row.step,
        direction: row.direction
    }
}

function row2ingredient(row) {
    return {
        ingredient: row.ingredient
    }
}

module.exports = {
    row2recipe,
    ingredients2values,
    steps2values,
    row2comment,
    row2step,
    row2ingredient
};