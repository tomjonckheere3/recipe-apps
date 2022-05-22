function renderUserRecipes(recipes, favourites) {
    recipes.forEach(recipe => {
        console.log(recipe)
        document.getElementById('recipes').innerHTML += `
        <div id="${recipe.recipeId}" class="recipe">
            <div class="recipeImage">
                <img src="data:image/jpg;base64,${recipe.image}"/>
            </div>
            <div class="recipeInfo">
                <div class="recipeHeader">
                    <a href="#" class="recipeName">${recipe.recipeName}</a>
                    <button class="removeRecipe" id="${recipe.recipeId}">X</button>
                </div>
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
                <p class="price">${recipe.price}</p>
            </div>
        </div>`
    });
    document.querySelectorAll("a.recipeName").forEach(recipe => {
        recipe.addEventListener("click", toDetailPage);
    });
    if (favourites) {
        store.setItem("myFavourites", recipes);
    } else {
        store.setItem("myRecipes", recipes);
    }
}