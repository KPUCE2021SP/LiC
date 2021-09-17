const firebase = require("firebase");


// real-time database setting
let config = {
  apiKey: "YOUR API KEY",
  authDomain: "stacklounge-62ffd.firebaseapp.com",
  databaseURL: "https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app",
  storageBucket: "stacklounge-62ffd.appspot.com" 
};
firebase.initializeApp(config);

// Get a reference to the database service
let database = firebase.database();

module.exports = database;
