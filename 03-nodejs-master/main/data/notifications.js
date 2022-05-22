'use strict';

let mysql = require('mysql');
const config = require('../util/config').config;
const webpush = require('web-push');
const recipes = require('./recipes');
const users = require('./users.js');
const notificationConverters = require('../util/rowConverters/notificationConverters');


const pushKeys = {
    publicKey: 'BCvlhDwALCXt9zkuTt6TNSltpzFUOsZWlgtIrBQRNmxMWTmue8lP63M5yuJ7xI5dkxSeXu9xoia1Oh1CAFmGOX4',
    privateKey: 'zTDBVHONTMMCWRmOuZ-VlPIgsPQuLIDACEr-i3PdJMA'
}

webpush.setVapidDetails(
    "mailto:tom.jonckheere3@student.howest.be",
    pushKeys.publicKey,
    pushKeys.privateKey
)

let subscribers = {};

function registerUserForNotifications(userId, pushSubscription) {
    subscribers[userId] = pushSubscription;
    console.log(`Registered user ${userId} for notifications`);
    console.log(subscribers);
}

function sendFavouritedNotificationToUser(userId, favouritedRecipeId) {
    recipes.getRecipeById(favouritedRecipeId, (err, recipe) => {
        let ownerOfFavouritedRecipe = recipe.userId;
        users.getUserById(userId, (err, user) => {
            const data = { user: user.username, recipe: recipe.recipeName };
            console.log(JSON.stringify(data));
            console.log(subscribers[ownerOfFavouritedRecipe]);
            sendNotification(ownerOfFavouritedRecipe, data);
        })
    })
}

function sendNotification(userId, data) {
    let notificationText = `${data["user"]} favourited your recipe: ${data["recipe"]}`;
    let notificationData = {
        body: notificationText
    }
    webpush.sendNotification(subscribers[userId], JSON.stringify(notificationData))
    .then(() => {
        addReceivedNotification(userId, notificationText);
    })
    .catch(err => console.log(err));
}

function addReceivedNotification(userId, notification) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            console.log(err);
        } else {
            let time = getCurrentTimeString();
            let sql = 'INSERT INTO notifications(userId, notificationContent, date) VALUES(?, ?, ?)';
            connection.query(sql, [userId, notification, time], (err, result) => {
                connection.end();
                if (err) {
                    console.log(err);
                } else {
                    console.log("Added notification:", result.insertId);
                }
            })
        }
    })   
}

function getNotificationsOfUser(userId, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            console.log(err);
            cb(err); 
        } else {
            let sql = 'SELECT * FROM notifications WHERE userId = ? ORDER BY date DESC';
            connection.query(sql, [userId], (err, rows) => {
                connection.end();
                if (err) {
                    console.log(err)
                    cb(err);
                } else {
                    cb(err, rows.map(notificationConverters.row2notification));
                }
            })
        }
    })
}

function getCurrentTimeString() {
    let date = new Date();

    let hour = date.getHours();
    hour = (hour < 10 ? "0" : "") + hour;

    let min  = date.getMinutes();
    min = (min < 10 ? "0" : "") + min;

    let sec  = date.getSeconds();
    sec = (sec < 10 ? "0" : "") + sec;

    let year = date.getFullYear();

    let month = date.getMonth() + 1;
    month = (month < 10 ? "0" : "") + month;

    let day  = date.getDate();
    day = (day < 10 ? "0" : "") + day;

    return year + ":" + month + ":" + day + ":" + hour + ":" + min + ":" + sec;
}

module.exports = {
    registerUserForNotifications,
    sendFavouritedNotificationToUser,
    getNotificationsOfUser
}
