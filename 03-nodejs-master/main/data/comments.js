'use strict';

const mysql = require('mysql');
const config = require('../util/config').config;

function addComment(recipeId, comment, cb) {
    let connection = mysql.createConnection(config);
    connection.connect((err) => {
        if (err) {
            return cb(err);
        } else {
            console.log('Creating new comment');
            let sql = 'INSERT INTO comments(recipeId, userId, commentContent) VALUES(?, ?, ?);';
            connection.query(sql, [recipeId, comment.userId, comment.commentContent], (err, result) => {
                connection.end();
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

module.exports = {
    addComment
}