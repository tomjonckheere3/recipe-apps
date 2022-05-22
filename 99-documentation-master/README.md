# RecipeApp

## General

These apps are made for users to seek inspiration with new recipes or/and to share their own recipes with others.

Link to video of the app: https://youtu.be/EQFT_6FZ88w

Link to the app: https://wam-project-d51b4.firebaseapp.com/index.html

## NODE JS

### Setup

First, install the following node modules:

        npm install http
        npm install express
        npm install jsonwebtoken
        npm install mysql
        npm install web-push
        npm install bcrypt

Then, create the database with the recipeApp.sql dump file.

Finally, enter this line to start the server.

        node main/server.js

## Laravel

The base url of the heroku api: https://cryptic-shore-59745.herokuapp.com/api/

### Setup

Note: This setup is for the local laravel server only.

First, install web push:

        composer require laravel-notification-channels/webpush
        composer update

Then, update the .env file to work with a local database and enter the following commands in a cli to populate it:

        php artisan migrate
        php artisan db:seed

## NMA

### Setup

If, you are using a local server, add the ip address of that server to res/xml/network_security/config.xml:

        <domain includeSubdomains="true">{IP address of your server}</domain>

This allows us to send request to these ip addresses.

### Switching server

You can switch between server in network/RecipeApiService.kt by changing the base url of the retrofit builder:

        .baseUrl(LARAVEL_HEROKU_BASE_URL)

After this, it is best to uninstall the app on your vm or smartphone. 
Otherwise, the room database will contain the recipes from both servers.    

## PWA

The published app can be found here: https://wam-project-d51b4.firebaseapp.com/index.html

### Setup

The pwa should not require any extra steps to work.

### Switching server

Switching between API's can be done in the config.js file. 
When changing API's, also change the API in the sw.js file. 
Otherwise, the notifications will not work.





