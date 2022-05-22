"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("back").addEventListener("click", () => {
        window.location.replace("profile.html");
    });
    loadFavouriteRecipes();
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function loadFavouriteRecipes() {
    let userId = localStorage.getItem("userId");
    let url = config.host + "users/" + userId + "/favourites";
    let jwt = getCookie("jsonwebtoken");
    fetch(url,
    {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "authorization": "Bearer " + jwt 
        }
    })
    .then(response => response.json())
    .then(recipes => {
        renderUserRecipes(recipes, true);
        document.querySelectorAll("button.removeRecipe").forEach(removeButton => {
            removeButton.addEventListener("click", removeRecipeFromFavourites);
        });
    })
    .catch(() => {
        store.getItem("myFavourites").then(myRecipes => {
            renderUserRecipes(myRecipes, true);
        })
    })
}

function removeRecipeFromFavourites(e) {
    e.preventDefault();
    let recipeId = e.target.id
    let userId = localStorage.getItem("userId");
    let url = config.host + "users/" + userId + "/favourites/" + recipeId;
    let jwt = getCookie("jsonwebtoken");
    fetch(url, 
    {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "authorization": "Bearer " + jwt 
        }
    })
    .then(response => {
        if (response.ok) {
            e.target.closest(".recipe").remove();
            window.alert("Successfully removed recipe");
        } else {
            window.alert("failed to remove favourite");
        }
    })
}