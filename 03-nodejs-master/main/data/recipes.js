'use strict';

const mysql = require('mysql');
const config = require('../util/config').config;
const recipeConverters = require('../util/rowConverters/recipeConverters');

function getRecipes(sortOrder, sort, mealType, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            console.log('Retrieving all recipes');
            let sql = 'SELECT * FROM recipes;';
            connection.query(sql, (err, rows) => {
                connection.end();
                if (err) {
                    return cb(err);
                } else {
                    let recipes = rows.map(recipeConverters.row2recipe);
                    let filteredRecipes = recipes.filter(recipe => {
                        return mealType ? (recipe.mealType === mealType) : true;
                    });
                    if (sort) {
                        let sortedRecipes = filteredRecipes.sort((a, b) => a[sort] - b[sort]);
                        if (sortOrder === 'desc') {
                            sortedRecipes = sortedRecipes.reverse();
                        }
                        return cb(err, sortedRecipes);
                    }
                    return cb(err, filteredRecipes);
                }
            })
        }
    });
}

function addRecipe(recipe, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Creating new recipe');
            let sql = 'INSERT INTO recipes(userId, recipeName, mealType, servings, prepareTime, price, description, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?);';
            connection.query(sql, [recipe.userId, recipe.recipeName, recipe.mealType, recipe.servings, recipe.prepareTime, recipe.price, recipe.description, recipe.image], (err, result) => {
                connection.end();
                if (err) {
                    console.log(err);
                    cb(err);
                } else {
                    addIngredientsOfRecipe(recipe.ingredients, result.insertId, (err) => {
                        if (err) {
                            console.log(err);
                            cb(err);
                        } else {
                            addStepsOfRecipe(recipe.steps, result.insertId, (err) => {
                                if (err) {
                                    console.log(err);
                                    cb(err);
                                } else {
                                    cb(err, result.insertId);
                                }
                            });
                        }
                    });
                }
            });
        }
    });
}

function addIngredientsOfRecipe(ingredients, recipeId, cb) {
    let values = recipeConverters.ingredients2values(ingredients, recipeId);
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Creating new ingredients');
            let sql = 'INSERT INTO ingredients(recipeId, ingredient) VALUES ?;';
            connection.query(sql, [values], (err) => {
                connection.end();
                cb(err);
            });
        }
    });
}

function addStepsOfRecipe(steps, recipeId, cb) {
    let values = recipeConverters.steps2values(steps, recipeId);
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Creating new steps');
            let sql = 'INSERT INTO directions(recipeId, step, direction) VALUES ?;';
            connection.query(sql, [values], (err) => {
                connection.end();
                cb(err);
            });
        }
    });
}

function getRecipeById(recipeId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Retrieving recipe:', recipeId);
            let recipeSql = 'SELECT * FROM recipes WHERE recipeId = ?;';
            let ingredientsSql = 'SELECT * FROM ingredients WHERE recipeId = ?;';
            let stepsSql = 'SELECT * FROM directions WHERE recipeId = ?;';
            let commentsSql = 'SELECT * FROM comments WHERE recipeId = ?;';
            let sql = recipeSql+ingredientsSql+stepsSql+commentsSql;
            connection.query(sql, [recipeId, recipeId, recipeId, recipeId], (err, results) => {
                connection.end();
                if (err) {
                    console.log(err)
                    cb(err)
                } else {
                    let detailedRecipe = recipeConverters.row2recipe(results[0][0]);
                    detailedRecipe.ingredients = results[1].map(recipeConverters.row2ingredient);
                    detailedRecipe.steps = results[2].map(recipeConverters.row2step);
                    detailedRecipe.comments = results[3].map(recipeConverters.row2comment);
                    cb(err, detailedRecipe);
                }
            })
        }
    })
}

function getSuggestions(cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Retrieving suggestions');
            let breakfastRecipeSql = 'SELECT * FROM recipes WHERE mealType = "breakfast" ORDER BY RAND() LIMIT 1;';
            let supperRecipeSql = 'SELECT * FROM recipes WHERE mealType = "supper" ORDER BY RAND() LIMIT 1;';
            let dinnerRecipeSql = 'SELECT * FROM recipes WHERE mealType = "dinner" ORDER BY RAND() LIMIT 1;';
            connection.query(breakfastRecipeSql + supperRecipeSql + dinnerRecipeSql, (err, results) => {
                connection.end();
                if (err) {
                    console.log(err);
                    cb(err);
                } else {
                    let recipes = results
                    let suggestions = {
                        'breakfast': recipeConverters.row2recipe(recipes[0][0]),
                        'supper': recipeConverters.row2recipe(recipes[1][0]),
                        'dinner': recipeConverters.row2recipe(recipes[2][0])
                    }
                    cb(err, suggestions)
                }
            })
        }
    })
}

function removeRecipe(userId, recipeId, cb) {
    isOwnerOfRecipe(userId, recipeId, (err, isOwner) => {
        if (!isOwner) {
            cb(err, false)
        } else {
            let connection = mysql.createConnection(config);
            connection.connect((err) => {
                if (err) {
                    return cb(err);
                } else {
                    console.log('Deleting recipe: ' + recipeId);
                    let removeFavouritesSql = 'DELETE FROM favourites WHERE recipeId = ?;';
                    let removeStepsSql = 'DELETE FROM directions WHERE recipeId = ?;';
                    let removeIngredientsSql = 'DELETE FROM ingredients WHERE recipeId = ?;';
                    let removeCommentsSql = 'DELETE FROM comments WHERE recipeId = ?;';
                    let removeRecipeSql = 'DELETE FROM recipes WHERE recipeId = ?;';
                    let sql = removeFavouritesSql+removeStepsSql+removeIngredientsSql+removeCommentsSql+removeRecipeSql;
                    connection.query(sql, [recipeId, recipeId, recipeId, recipeId, recipeId], (err) => {
                        connection.end();
                        if (err) {
                            console.log(err);
                            cb(err);
                        } else {
                            cb(err, true)
                        }
                    })
                }
            })
        }
    });
}


function isOwnerOfRecipe(userId, recipeId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            let sql = 'SELECT * FROM recipes WHERE recipeId = ? and userId = ?;';
            connection.query(sql, [recipeId, userId], (err, results) => {
                connection.end();
                if (err) {
                    console.log(err);
                    cb(err);
                } else {
                    if (results.length == 1) {
                        cb(err, true);
                    } else {
                        cb(err, false);
                    }
                }
            })
        }
    })
}

module.exports = {
    getRecipes,
    addRecipe,
    getRecipeById,
    getSuggestions,
    removeRecipe
};