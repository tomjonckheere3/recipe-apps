'use strict';

function row2notification(row) {
    return {
        notificationId: row.notificationId,
        notificationContent: row.notificationContent,
        date: row.date
    }
}

module.exports = {
    row2notification
}