"use strict";

let recipesDiv = document.getElementById("recipes");
let totalRecipesSpan = document.getElementById("totalRecipes");

let params = {};

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadRecipes();
    document.getElementById("add").addEventListener("click", () => {
        window.location.replace("createRecipe.html")
    });
    document.querySelectorAll("#mealTypes input[type=checkbox]").forEach(mealType => {
        mealType.addEventListener("click", addFilter)
    });
    document.querySelectorAll("select").forEach(select => {
        select.addEventListener("change", loadRecipes);
    });
    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function loadRecipes() {
    recipesDiv.innerHTML = "";
    let url = new URL(config.host + "recipes");
    Object.keys(params).forEach(key => 
        url.searchParams.append(key, params[key]));

    let sortSelect = document.getElementById("sortBy");
    let sortBy = sortSelect.options[sortSelect.selectedIndex].value;
    url.searchParams.append("sort", sortBy);

    let sortOrderSelect = document.getElementById("sortOrder");
    let sortOrder = sortOrderSelect.options[sortOrderSelect.selectedIndex].value;
    url.searchParams.append("sortOrder", sortOrder);

    fetch(url)
    .then(response => response.json())
    .then(recipes => {
        renderRecipes(recipes);
    })
    .catch(() => {
        store.getItem("recipes").then(recipes => {
            renderRecipes(recipes);
        });
    });
}

function renderRecipes(recipes) {
    totalRecipesSpan.innerHTML = recipes.length;
    recipes.forEach(recipe => {
        let description = recipe.description;
        if (description.length > 125) {
            description = description.substring(0, 125) + "... "
        }
        recipesDiv.innerHTML += `
            <div id="${recipe.recipeId}" class="recipe">
                <div class="recipeImage">
                    <img src="data:image/jpg;base64,${recipe.image}">
                </div>
                <div class="recipeInfo" id="breakfastInfo">
                    <a href="#" class="recipeName">${recipe.recipeName}</a>
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
                    <p class="description">${description}<a href="#" class="more">More</a></p>
                    <p class="price">€${recipe.price}</p>
                </div>
            </div>`;
        });
    document.querySelectorAll("a.recipeName").forEach(recipe => {
        recipe.addEventListener("click", toDetailPage);
    });
    document.querySelectorAll("a.more").forEach(recipe => {
        recipe.addEventListener("click", toDetailPage);
    });
    store.setItem("recipes", recipes);
}

function addFilter(e) {
    document.querySelectorAll("#mealTypes input[type=checkbox]").forEach(mealType => {
        mealType.checked = false;
    });
    e.target.checked = true;
    let filterOn = e.target.value;
    params.mealType = filterOn;
    loadRecipes();
}