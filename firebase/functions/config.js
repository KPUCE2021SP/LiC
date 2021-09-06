const firebase = require("firebase");


// real-time database setting
let config = {
  apiKey: "your api key",
  authDomain: "stacklounge-62ffd-21842.firebaseapp.com",
  databaseURL: "https://stacklounge-62ffd-21842.firebaseio.com/",
  storageBucket: "stacklounge-62ffd-21842.appspot.com" 
};
firebase.initializeApp(config);

// Get a reference to the database service
let database = firebase.database();

module.exports = database;