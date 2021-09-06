const functions = require("firebase-functions");
const database = require("./config");
const parsing = require("./crawl");

// 사용자가 들어왔을 때, 처음 로그인을 하는 경우에 호출된다.
// 사용자의 이름을 가지고 크롤링한 정보를 실시간 DB에 저장한다.
// 사용자의 이름을 userlist에 추가하고 star한 topic들을 usertopics/{사용자이름}에 추가한다.
exports.lic = functions
  .https.onCall((data, context) => {
  
  const getHTML = (username) => {
    parsing(username)
    .then(userdata => {
      database.ref('userlist/').push({
        username
      })
      
      database.ref(`usertopics/${data}`).set({
        usertopics: userdata[0].topicName,
      });
    })
  }
  getHTML(data);
})

// 특정 시간마다 github에서 받아온 topics들을 update해준다.
exports.scheduledFunctionCrontab = functions.pubsub.schedule('every 5 minutes').onRun((context) => {
  console.log('This will be run every day at 12:00');

  database.ref('userlist/').once('value', (snapshot) => {
    snapshot.forEach((childSnapshot) => {
      var childKey = childSnapshot.key;
      var childData = childSnapshot.val();
      parsing(childData['username'])
      .then(userdata => {
        database.ref(`usertopics/${data}`).set({
          usertopics: userdata[0].topicName,
        });
      })
    });
  });
});