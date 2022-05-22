'use strict';

function row2user(row) {
    return {
        userId: row.userId,
        username: row.username,
        profilePicture: row.profilePicture
    }
}

module.exports = {
    row2user
}