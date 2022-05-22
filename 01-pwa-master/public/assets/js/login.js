"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("login").addEventListener("click", login);

    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function login(e) {
    e.preventDefault();
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let url = config.host + "users/login";
    fetch(url, 
    {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"username": username, "password": password})
    })
    .then(response => response.json())
    .then(data => {
        if (data.userId && data.jwt) {
            localStorage.setItem("userId", data.userId);
            document.cookie = "jsonwebtoken=" + data.jwt;
            subscribeUserToPush();
        } else {
            window.alert("This user does not exist");
        }
    })
}