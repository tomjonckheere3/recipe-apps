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
    let url = config.host + "users/" + userId + "/recipes";
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
        renderUserRecipes(recipes);
        document.querySelectorAll("button.removeRecipe").forEach(removeButton => {
            removeButton.addEventListener("click", removeRecipe);
        });
    })
    .catch(() => {
        store.getItem("myRecipes").then(myRecipes => {
            renderUserRecipes(myRecipes);
        })
    })
}

function removeRecipe(e) {
    e.preventDefault();
    let recipeId = e.target.id
    let userId = localStorage.getItem("userId");
    let url = config.host + "users/" + userId + "/recipes/" + recipeId;
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
            window.alert("failed to remove recipe");
        }
    });
}

