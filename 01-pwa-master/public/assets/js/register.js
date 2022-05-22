"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("register").addEventListener("click", registerUser);
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function registerUser(e) {
    e.preventDefault();
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let url = config.host + "users";
    fetch(url, 
    {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"username": username,"password": password})
    })
    .then(response => response.json())
    .then(data => {
        console.log(data)
        if (data.userId && data.jwt) {
            localStorage.setItem("userId", data.userId);
            document.cookie = "jsonwebtoken=" + data.jwt;
            subscribeUserToPush();
        } else {
            window.alert("This user already exists");
        }
    })
}