const NODEAPI = "http://localhost:8888/api/";
const LARAVELAPI_LOCAL = "http://recipe-api.howest/api/";
const LARAVELAPI_HEROKU = "https://cryptic-shore-59745.herokuapp.com/api/";


config = {
    "host": LARAVELAPI_HEROKU
};

const store = localforage.createInstance({name: "recipes"});