"use strict";

let totalSteps = 1;
let totalIngredients = 1;

let nameInput = document.getElementById("name");
let descriptionInput = document.getElementById("description");
let MealTypeSelect = document.getElementById("mealType");
let imageInput = document.getElementById("picture");
let servingsInput = document.getElementById("servings");
let prepareTimeInput = document.getElementById("prepareTime");
let priceInput = document.getElementById("price");
let ingredientsList = document.getElementById("ingredients");
let stepsList = document.getElementById("steps");

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("back").addEventListener("click", () => {
        window.location.replace("recipes.html");
    });

    document.getElementById("submit").addEventListener("click", checkInput);
    document.getElementById("addIngredient").addEventListener("click", addIngredientInput);
    document.getElementById("addStep").addEventListener("click", addStepInput);

    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);
}

function checkInput(e) {
    e.preventDefault();
    let nameValidator = nameInput.value == "";
    let descriptionValidator = descriptionInput.value == "";
    let imageValidator = imageInput.value == "";
    let servingsValidator = servingsInput.value == "" || servingsInput.value < 1;        
    let prepareTimeValidator = prepareTimeInput.value == "" || prepareTimeInput.value < 1;
    let priceValidator = priceInput.value == "" || priceInput.value < 1;
    if (nameValidator || descriptionValidator || imageValidator || servingsValidator || prepareTimeValidator ||priceValidator) {
        window.alert("Please fill in all fields correctly");
    } else {
        addRecipe();
    }
}

function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        let encoded = reader.result.toString().replace(/^data:(.*,)?/, "");
        if ((encoded.length % 4) > 0) {
          encoded += '='.repeat(4 - (encoded.length % 4));
        }
        resolve(encoded);
      };
      reader.onerror = error => reject(error);
    });
  }

function addRecipe() {
    let userId = localStorage.getItem("userId");
    let ingredients = [];
    let steps = [];
    document.querySelectorAll("input.ingredient").forEach(ingredientInput => {
        ingredients.push({ingredient: ingredientInput.value});
    });
    document.querySelectorAll("input.step").forEach(stepInput => {
        let step = {step: stepInput.id, direction: stepInput.value};
        steps.push(step);
    });
    getBase64(imageInput.files[0]).then(base64Image => {
        let body = {
            "userId": userId,
            "recipeName": nameInput.value,
            "description": descriptionInput.value,
            "mealType": MealTypeSelect.value,
            "image": base64Image,
            "servings": servingsInput.value,
            "prepareTime": prepareTimeInput.value,
            "price": priceInput.value,
            "ingredients": ingredients,
            "steps": steps
        }
        syncWithServer(body);
    });
}

function syncWithServer(recipe) {
    let jwt = getCookie("jsonwebtoken");
    if ("serviceWorker" in navigator && "SyncManager" in window) {
        console.log("Syncing with server");
        navigator.serviceWorker.controller.postMessage({recipe: recipe, jwt: jwt});
        navigator.serviceWorker.ready.then(reg => {
            reg.sync.register("sync-add-recipe");
            window.location.replace("recipes.html");
        });
    } else {
        let url = config.host + "recipes";
        fetch(url,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + jwt,
            },
             body: JSON.stringify(body)
        })
        .then(response => response.json())
        .then(() => {
            window.location.replace("recipes.html")
        });
    }
}

function addIngredientInput(e) {
    e.preventDefault();
    totalIngredients++;
    let rememberIngredients = [];
    document.querySelectorAll("input.ingredient").forEach(ingredientInput => {
        rememberIngredients.push(ingredientInput.value);
    });
    ingredientsList.innerHTML += `<li><input type="text" class="ingredient" id="${totalIngredients}" value="ingredient ${totalIngredients}"></li>`;
    fillIngredients(rememberIngredients)
}

function fillIngredients(ingredients) {
    for (let i = 0; i < totalIngredients; i++) {
        ingredientsList.children[i].firstChild.value = ingredients[i];
    }
}

function addStepInput(e) {
    e.preventDefault();
    totalSteps++;
    let rememberSteps = [];
    document.querySelectorAll("input.step").forEach(stepInput => {
        rememberSteps.push(stepInput.value);
    });
    stepsList.innerHTML += `<li><input type="text" class="step" id="${totalSteps}" value="Step ${totalSteps}"></li>`;
    fillSteps(rememberSteps);
}

function fillSteps(steps) {
    for (let i = 0; i < totalSteps; i++) {
        stepsList.children[i].firstChild.value = steps[i];
    }
}