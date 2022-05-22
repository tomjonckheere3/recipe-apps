"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("myRecipes").addEventListener("click", () => {
        window.location.replace("myRecipes.html");
    });
    document.getElementById("myFavourites").addEventListener("click", () => {
        window.location.replace("myFavourites.html");
    });
    document.getElementById("logout").addEventListener("click", logout)
    loadUser();
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function loadUser() {
    let userId = localStorage.getItem("userId");
    let url = config.host + "users/" + userId;
    let jwt = getCookie("jsonwebtoken");
    fetch(url,
    {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + jwt,
        }
    })
    .then(response => response.json())
    .then(user => {
        document.getElementById("username").innerText = user.username;
        store.setItem("user", user);
    })
    .catch(() => {
        store.getItem("user").then(user => {
            document.getElementById("username").innerText = user.username;
        })
    })
}

function logout() {
    localStorage.removeItem("userId");
    deleteAllCookies();
    window.location.replace("index.html");
}