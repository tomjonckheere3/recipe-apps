
const APPLICATION_KEY = "BCvlhDwALCXt9zkuTt6TNSltpzFUOsZWlgtIrBQRNmxMWTmue8lP63M5yuJ7xI5dkxSeXu9xoia1Oh1CAFmGOX4";

function registerServiceWorker() {
    if ("serviceWorker" in navigator) {
        navigator.serviceWorker.register("/sw.js").then(function(res) {
            console.log("Successfuly registered service worker with scope");
        })
        .catch((err) => {
            console.log("Error registering the service worker: ", err);
        });
        navigator.serviceWorker.ready.then(() => enableNotifications());
    }
}

function enableNotifications() {
    Notification.requestPermission().then(permission => {
        if (permission === "granted") {
            console.log("Granted");
        } else if (permission === "denied") {
            console.log("Denied");
        } else {
            console.log("Default");
        }
    })
}

function subscribeUserToPush() {
    if ("PushManager" in window) {
        navigator.serviceWorker.ready.then(registration => {
            const subscribeOptions = {
                userVisibleOnly: true,
                applicationServerKey: urlBase64ToUint8Array(APPLICATION_KEY)
            };
            return registration.pushManager.subscribe(subscribeOptions);
        }).then(pushSubscription => {
            console.log(pushSubscription)
            const userId = localStorage.getItem("userId");
            registerUserForNotifications(userId, pushSubscription);
        })
    }
}

function registerUserForNotifications(userId, pushSubscription) {
    let url = `${config.host}users/${userId}/notifications/register`;
    fetch(url,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(pushSubscription)
        }
    )
    .then(response => {
        console.log(response);
        window.location.replace("suggestions.html");
    });
}

registerServiceWorker();
enableNotifications();
