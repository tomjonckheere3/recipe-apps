'use strict';

const mysql = require('mysql');
const bcrypt = require('bcrypt');
const config = require('../util/config').config;
const recipeConverters = require('../util/rowConverters/recipeConverters');
const userConverters = require('../util/rowConverters/userConverters');

const saltRounds = 10;

function addUser(user, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            let sql = 'SELECT * FROM users WHERE username = ?;';
            connection.query(sql, [user.username], (err, rows) => {
                if (err) {
                    console.log(err);
                    cb(err);
                } else if (rows.length > 0) {
                    cb(err, false);
                } else {
                    console.log('Creating new user');
                    
                    bcrypt.hash(user.password, saltRounds, function(err, hashedPassword) {
                        let sql = 'INSERT INTO users(username, password) VALUES(?, ?);';
                        connection.query(sql, [user.username, hashedPassword], (err, result) => {
                            connection.end();
                            if (err) {
                                console.log(err);
                                cb(err)
                            } else{
                                cb(err, result.insertId)
                            }
                        });
                    });  
                }
            });
        }
    });
}

function checkForMatchingUser(credentials, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            console.log('Checking for matching user');
            let sql = 'SELECT * FROM users WHERE username = ?;';
            connection.query(sql, [credentials.username, credentials.password], (err, rows) => {
                connection.end();
                if (err) {
                    console.log(err);
                    cb(err);
                } else {
                    try {
                        let user = rows[0];
                        bcrypt.compare(credentials.password, user.password, function(err, result) {
                            if (result) {
                                let userId = user.userId;
                                cb(err, userId);
                            } else {
                                cb(err);
                            }
                        });
                    } catch (exception) {
                        cb(exception.message)
                    }
                }
            });
        }
    });
}

function getUserById(userId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            console.log('Retrieving user:', userId)
            let sql = 'SELECT userId, username FROM users WHERE userId = ?;';
            connection.query(sql, [userId], (err, rows) => {
                connection.end();
                if (err) {
                    return cb(err);
                } else {
                    return cb(err, userConverters.row2user(rows[0]))
                }
            })
        }
    })
}

function getRecipesOfUser(userId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err)
        } else {
            console.log('Retrieving recipes of user:', userId);
            let sql = "SELECT * FROM recipes WHERE userId = ?;";
            connection.query(sql, [userId], (err, rows) => {
                connection.end();
                if (err) {
                    return cb(err);
                } else {
                    return cb(err, rows.map(recipeConverters.row2recipe));
                }
            });
        }
    });
}

function getFavouritesOfUser(userId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err)
        } else {
            console.log('Retrieving favourite recipes of user:', userId);
            let sql = "SELECT recipes.* FROM favourites JOIN recipes ON recipes.recipeId = favourites.recipeId WHERE favourites.userId = ?;";
            connection.query(sql, [userId], (err, rows) => {
                connection.end();
                if (err) {
                    return cb(err);
                } else {
                    return cb(err, rows.map(recipeConverters.row2recipe));
                }
            });
        }
    });
}

function addFavouriteRecipe(userId, recipeId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            console.log('Adding favourite recipe');
            let sql = 'INSERT INTO favourites(userId, recipeId) VALUES(?, ?)'
            connection.query(sql, [userId, recipeId], (err) => {
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

function removeFavouriteRecipe(userId, recipeId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            cb(err);
        } else {
            console.log('Removing favourite recipe');
            let sql = 'DELETE FROM favourites WHERE (recipeId = ?) and (userId = ?);';
            connection.query(sql, [recipeId, userId], (err) => {
                connection.end();
                if (err) {
                    console.log(err);
                    cb(err);
                } else {
                    cb(err, true);
                }
            })
        }
    })
}


module.exports = {
    addUser,
    getUserById,
    getRecipesOfUser,
    getFavouritesOfUser,
    checkForMatchingUser,
    addFavouriteRecipe,
    removeFavouriteRecipe
};