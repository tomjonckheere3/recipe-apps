"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadSuggestions();
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function loadSuggestions() {
    let url = config.host + "recipes/suggestions";
    fetch(url)
    .then(response => response.json())
    .then(suggestions => {
        renderSuggestion(suggestions.breakfast);
        renderSuggestion(suggestions.supper);
        renderSuggestion(suggestions.dinner);
        document.querySelectorAll("div.recipe").forEach(recipe => {
            recipe.addEventListener("click", toDetailPage);
        });
    })
    .catch(() => {
        store.getItem("recipes").then(recipes => {
            let breakfast = recipes.find(recipe => recipe.mealType === "breakfast");
            let supper = recipes.find(recipe => recipe.mealType === "supper");
            let dinner = recipes.find(recipe => recipe.mealType === "dinner");
            renderSuggestion(breakfast);
            renderSuggestion(supper);
            renderSuggestion(dinner);
        });
    });
}

function renderSuggestion(recipe) {
    document.querySelector("main").innerHTML += `
        <div class="recipe" id="${recipe.recipeId}">
        <div class="recipeImage">
            <img src="data:image/jpg;base64,${recipe.image}">
        </div>
        <div class="recipeInfo">
            <h1 class="recipe">${recipe.recipeName}</h1>
            <div>
                <div class="servings">
                    <p>${recipe.servings}</p>
                    <img src="assets/images/icons/servingsIcon.png">
                </div>
                <div class="prepareTime">
                    <p>${recipe.prepareTime}</p>
                    <img src="assets/images/icons/prepareTime.png">
                </div>                
            </div>
            <p class="description">${recipe.description}</p>
        </div>
        <div class="button">
            <button>See details</button>
        </div>`;
}