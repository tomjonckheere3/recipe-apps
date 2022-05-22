"use strict";

const recipeId = getRecipeIdFromUrl();

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadRecipe();
    document.getElementById("back").addEventListener("click", () => {
        window.location.replace("recipes.html");
    });
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function getRecipeIdFromUrl() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    let recipeId = urlParams.get("id");
    if (isNaN(recipeId) || recipeId == null) {
        window.location.replace("recipes.html");
    }
    return recipeId;
}

function loadRecipe() {
    let url = config.host + "recipes/" + recipeId;
    fetch(url)
    .then(response => response.json())
    .then(recipe => {
        renderRecipe(recipe);
    });
}


function renderRecipe(recipe) {
    document.querySelector("main").innerHTML = `
            <div id="recipeHeader">
                <h1>${recipe.recipeName}</h1>
                <button id="addFavourite">Add to Favourite</button>
            </div>
            <div id="recipeInfo">
                <img src="data:image/jpg;base64,${recipe.image}">
                <div>
                    <p>Servings: <span id="servings">${recipe.servings}</span> persons</p>
                    <p>Average cost: <span id="price">€${recipe.price}</span></p>
                    <p>Prepare time: <span id="prepareTime">${recipe.prepareTime}</span> minutes</p>
                    <p id="description">${recipe.description}</p>
                </div>
            </div>
            <div id="ingredients" class="extendedDetails">
                <h2>Ingredients</h2>
                <ul id="ingredientsList">
                </ul>
            </div>
            <div id="recipe" class="extendedDetails">
                <h2>Recipe</h2>
                <ul id="stepsList">
                </ul>
            </div>
            <div id="comments" class="extendedDetails">
                <h2>Comments</h2>
                <form>
                    <input type="text" id="commentText" name="commentText" placeholder="leave a comment">
                    <input type="submit" id="addComment" name="addComment" value="Place">
                </form>
                <ul id="commentsList">
                </ul>
            </div>`;
    document.getElementById("addComment").addEventListener("click", addComment);
    document.getElementById("addFavourite").addEventListener("click", addFavourite);
    renderIngredients(recipe.ingredients);
    renderSteps(recipe.steps);
    renderComments(recipe.comments);
}

function renderIngredients(ingredients) {
    ingredients.forEach(ingredient => {
        document.getElementById("ingredientsList").innerHTML += `<li>${ingredient.ingredient}</li>`;
    })
}

function renderSteps(steps) {
    steps.forEach(step => {
        document.getElementById("stepsList").innerHTML += `
            <li>
                <h3>STEP ${step.step}</h3>
                <p>${step.direction}</p>
            </li>`
    })
}

function renderComments(comments) {
    comments.forEach(comment => {
        document.getElementById("commentsList").innerHTML += `
        <li>
            <div class="commentHeader">
                <div class="user">
                    <img src="assets/images/icons/servingsIcon.png">
                    <h3>Tom Jonckheere</h3>
                </div>
            </div>
            <p class="commentContent">${comment.commentContent}</p>
        </li>`
    });
}

function addComment(e) {
    e.preventDefault();
    let comment = document.getElementById("commentText").value;
    if (comment != "") {
        let userId = localStorage.getItem("userId");
        let body = {
            "userId": userId,
            "commentContent": comment
        };
        syncWithServer(body);
    }
}

function syncWithServer(comment) {
    let jwt = getCookie("jsonwebtoken");
    if ("serviceWorker" in navigator && "SyncManager" in window) {
        console.log("Syncing with server");
        navigator.serviceWorker.controller.postMessage({comment: comment, jwt: jwt, recipeId: recipeId});
        navigator.serviceWorker.ready.then(reg => {
            reg.sync.register("sync-add-comment");
        });
    } else {
        let url = `${config.host}recipes/${recipeId}/comments`;
        fetch(url,
        {
            method: "POST",
            headers: 
            {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + jwt,
            },
            body: JSON.stringify(body)
        });
    }
}

function addFavourite(e) {
    e.preventDefault();
    let userId = localStorage.getItem("userId");
    let jwt = getCookie("jsonwebtoken");
    let url = `${config.host}users/${userId}/favourites`;
    let body = {
        recipeId: recipeId,
        sendNotification: true
    };
    fetch(url,
    {
        method: "POST",
        headers: 
        {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + jwt,
        },
        body: JSON.stringify(body)
    })
    .then(response => response.json())
    .then(data => {
        if (!data.error) {
            window.alert("Added to favourites");
        } else {
            window.alert(data.error)
        }
    });
}