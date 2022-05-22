"use strict";

const PORT  = 8888;

const http = require('http');
const express = require('express');
const recipes = require('./data/recipes');
const users = require('./data/users');
const comments = require('./data/comments');
const notifications = require('./data/notifications');
const jwt = require('./util/jwt');

const app = express();
app.use(express.urlencoded({ extended: true, limit:"150mb"}));
app.use(express.json({limit: '150mb'}));
app.use(express.json());
app.use((req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
    res.header(
      "Access-Control-Allow-Headers",
      "Origin, X-Requested-With, Content-Type, Accept, authorization"
    );
    next();
  });

const server = http.createServer(app);

server.listen(PORT, () => {
    console.log('Listening on port: ' + PORT)
});

app.get('/api/recipes', (req, res) => {
    let sortOrder = req.query.sortOrder;
    let sort = req.query.sort;
    let mealtype = req.query.mealType;
    recipes.getRecipes(sortOrder, sort, mealtype, (err, recipes) => {
        res.json(recipes);
    })
});

app.post('/api/recipes', jwt.authenticateToken, (req, res) => {
    let recipe = req.body;
    if (req.tokenUserId != recipe.userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to add a recipe with user ID ${userId}`);
        res.sendStatus(403);
    } else {
        recipes.addRecipe(recipe, (err, recipeId) => {
            res.json({recipeId: recipeId})
        });
    }
});

app.get('/api/recipes/suggestions', (req, res) => {
    recipes.getSuggestions((err, suggestedRecipes) => {
        res.json(suggestedRecipes);
    })
});

app.get('/api/recipes/:recipeId', (req, res) => {
    let recipeId = req.params.recipeId;
    recipes.getRecipeById(recipeId, (err, recipe) => {
        res.json(recipe)
    });
});

app.post('/api/recipes/:recipeId/comments', jwt.authenticateToken, (req, res) => {
    let recipeId = req.params.recipeId;
    let tokenUserId = req.tokenUserId;
    let body = req.body;
    if (tokenUserId != body.userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to add a recipe with user ID ${body.userId}`);
        res.sendStatus(403);
    } else {
        comments.addComment(recipeId, body, (err, commentId) => {
            res.json({commentId: commentId});
        });
    }
});

app.post('/api/users', (req, res) => {
    let user = req.body;
    users.addUser(user, (err, userId) => {
        if (err) {
            res.end(err);
        } else if (userId) {
            const token = jwt.generateAccessToken({ username: req.body.username, userId: userId });
            res.json({jwt: token, userId: userId});
        } else {
            res.json({'error': 'This user already exists'});
        }
    });
});

app.post('/api/users/login', (req, res) => {
    let credentials = req.body;
    console.log(credentials)
    users.checkForMatchingUser(credentials, (err, userId) => {
        if (err) {
            res.json({jwt: null, userId: -1});
        } else {
            const token = jwt.generateAccessToken({ username: req.body.username, userId: userId });
            res.json({jwt: token, userId: userId});
        }
    })
})

app.get('/api/users/:userId', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        users.getUserById(userId, (err, user) => {
            res.json(user)
        })
    }
})

app.get('/api/users/:userId/recipes', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        users.getRecipesOfUser(userId, (err, recipes) => {
            res.json(recipes);
        })
    }
});

app.delete('/api/users/:userId/recipes/:recipeId', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let recipeId = req.params.recipeId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        recipes.removeRecipe(userId, recipeId, (err, success) => {
            if (err) {
                console.log(err);
            } else if (success) {
                console.log(success)
                res.sendStatus(200);
            } else {
                res.end('You are not the owner of this recipe');
            }
        });
    }
});

app.get('/api/users/:userId/favourites', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        users.getFavouritesOfUser(userId, (err, recipes) => {
            res.json(recipes)
        });
    }
});

app.post('/api/users/:userId/favourites', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let recipeId = req.body.recipeId;
    let tokenUserId = req.tokenUserId;
    let sendNotification = req.body.sendNotification;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        users.addFavouriteRecipe(userId, recipeId, (err, success) => {
            if (err) {
                switch(err.errno) {
                    case 1216:
                        res.json({error: 'User or recipe does not exist'});
                        break;
                    case 1062:
                        res.json({error:'Recipe is already a favourite'});
                        break;
                    default:
                        res.json({error: 'Something went wrong'});
                };
            }
            else {
                if (sendNotification) {
                    notifications.sendFavouritedNotificationToUser(userId, recipeId);
                    res.json({success: "Added recipe to favourites"});
                } else {
                    res.json({success: "Added recipe to favourites"});
                }
            }
        })
    }
});

app.delete('/api/users/:userId/favourites/:recipeId', jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let recipeId = req.params.recipeId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        users.removeFavouriteRecipe(userId, recipeId, (err, success) => {
            if (err) {
                console.log(err);
            } else {
                res.sendStatus(200);
            };
        })
    }
});

app.get("/api/users/:userId/notifications", jwt.authenticateToken, (req, res) => {
    let userId = req.params.userId;
    let tokenUserId = req.tokenUserId;
    if (tokenUserId != userId) {
        console.log(`Access denied: user ${tokenUserId} is trying to access data of user ${userId}`);
        res.sendStatus(403);
    } else {
        notifications.getNotificationsOfUser(userId, (err, notifications) => {
            if (err) {
                console.log(err);
                res.statusCode(500);
            } else {
                res.json(notifications);
            }
        });
    }
})

app.post('/api/users/:userId/notifications/register', (req, res) => {
    let userId = req.params.userId;
    let pushSubscription = req.body;

    notifications.registerUserForNotifications(userId, pushSubscription);
    res.end();
});