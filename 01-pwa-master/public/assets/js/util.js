"use strict";

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  }

  function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
  }

  function toDetailPage(e) {
    let clickedRecipe = e.target.closest(".recipe");
    let recipeId = clickedRecipe.id;
    window.location.replace("recipe.html?id=" + recipeId);
  }

  function urlBase64ToUint8Array(base64String) {
    var padding = '='.repeat((4 - base64String.length % 4) % 4);
    var base64 = (base64String + padding)
        .replace(/\-/g, '+')
        .replace(/_/g, '/');

    var rawData = window.atob(base64);
    var outputArray = new Uint8Array(rawData.length);

    for (var i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}

function handleOffline(){
  let errorMessage = document.createElement("div");
  errorMessage.id = "error-message";
  errorMessage.innerText = "No network connection";
  const header = document.querySelector("header");
  header.insertBefore(errorMessage, header.firstChild);
  document.querySelectorAll("button").forEach(button => {
    button.disabled = true;
  })
  document.querySelectorAll("a").forEach(link => {
    link.disabled = true;
  })
}

function handleOnline(){
  const header = document.querySelector("header");
  header.removeChild(header.firstChild);
  document.querySelectorAll("button").forEach(button => {
    button.disabled = false;
  })
  document.querySelectorAll("a").forEach(link => {
    link.disabled = false;
  })
}