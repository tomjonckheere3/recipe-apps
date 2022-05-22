"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadNotifications();
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function loadNotifications() {
    let userId = localStorage.getItem("userId");
    let jwt = getCookie("jsonwebtoken");
    let url = `${config.host}users/${userId}/notifications`;
    fetch(url,
    {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "authorization": "Bearer " + jwt
        }
    })
    .then(response => response.json())
    .then(notifications => {
        renderNotifications(notifications);
    })
    .catch(() => {
        store.getItem("notifications").then(notifications => {
            renderNotifications(notifications);
        })
    })
}

function renderNotifications(notifications) {
    let receivedNotificationsList = document.getElementById("receivedNotifications");
    notifications.forEach(notification => {
        receivedNotificationsList.innerHTML += `
            <li id="${notifications.notificationId}" class="notifications">
                <p class="date"><span>${notification.date}</span></p>
                <p class="notificationContent">${notification.notificationContent}</p>
            </li>`
    });
    store.setItem("notifications", notifications);
}