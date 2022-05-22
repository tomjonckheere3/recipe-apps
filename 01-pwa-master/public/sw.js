const NODEAPI = 'http://localhost:8888/api/';
const LARAVELAPI_LOCAL = 'http://recipe-api.howest/api/';
const LARAVELAPI_HEROKU = 'https://cryptic-shore-59745.herokuapp.com/api/';

const host = LARAVELAPI_HEROKU;

const CACHE_NAME = "recipe-app-v1";

let cacheList = Array();

const CACHED_URLS = [
    "/",
    "/index.html",
    "/createRecipe.html",
    "/myFavourites.html",
    "/myRecipes.html",
    "/notifications.html",
    "/profile.html",
    "/recipe.html",
    "/recipes.html",
    "/register.html",
    "/suggestions.html",

    "/assets/css/reset.css",
    "/assets/css/header.css",
    "/assets/css/index.css",
    "/assets/css/createRecipe.css",
    "/assets/css/userRecipes.css",
    "/assets/css/notifications.css",
    "/assets/css/profile.css",
    "/assets/css/recipe.css",
    "/assets/css/recipes.css",
    "/assets/css/register.css",
    "/assets/css/suggestions.css",

    "/assets/js/config.js",
    "/assets/js/createRecipe.js",
    "/assets/js/init.js",
    "/assets/js/login.js",
    "/assets/js/myFavourites.js",
    "/assets/js/myRecipes.js",
    "/assets/js/notifications.js",
    "/assets/js/profile.js",
    "/assets/js/recipe.js",
    "/assets/js/recipes.js",
    "/assets/js/register.js",
    "/assets/js/suggestions.js",
    "/assets/js/userRecipes.js",
    "/assets/js/util.js",
    "/assets/js/lib/localforage.min.js",
    
    "/assets/images/icons/prepareTime.png",
    "/assets/images/icons/servingsIcon.png",

    "/icons/android-chrome-192x192.png",
    "/icons/android-chrome-512x512.png",
    "/icons/apple-touch-icon.png",
    "/icons/favicon-16x16.png",
    "/icons/favicon-32x32.png",
    "/icons/mstile-150x150.png",

    "/manifest.json"
];

self.addEventListener("install", e => {
    e.waitUntil(
        caches.open(CACHE_NAME).then(cache => {
            return cache.addAll(CACHED_URLS);
        })
    )
});

self.addEventListener("fetch", e => {
    e.respondWith(
        fetch(e.request).catch(() => {
            return caches.open(CACHE_NAME).then(cache => {
                return cache.match(e.request);
            })
        })
    )
});

self.addEventListener("push", e => {
    console.log(e)
    const data = e.data.json();
    console.log(e);
    const text = data.body;

    self.registration.showNotification("RecipeApp", { body: text});
});

self.addEventListener("message", e => {
    cacheList.push(e.data);
})

self.addEventListener("sync", e => {
    if (e.tag === "sync-add-recipe") {
        e.waitUntil(syncAddRecipe());
    } else if (e.tag === "sync-add-comment") {
        e.waitUntil(syncAddComment());
    }
});

function syncAddRecipe() {
    return Promise.all(cacheList.map(data => {
        const recipe = data["recipe"];
        const jwt = data["jwt"];
        const url = host + "recipes"
        return fetch(url,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + jwt,
                },
                body: JSON.stringify(recipe)
            });
    })).then(() => {
        cacheList = Array();
        console.log("Added recipe with background sync");
    });
}

function syncAddComment() {
    return Promise.all(cacheList.map(data => {
        const comment = data["comment"];
        const jwt = data["jwt"];
        const recipeId = data["recipeId"];
        const url = `${host}recipes/${recipeId}/comments`;
        fetch(url,
        {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwt,
            },
            body: JSON.stringify(comment)
        });
    })).then(() => {
        cacheList = Array();
        console.log("Added comment with background sync");
    });
}