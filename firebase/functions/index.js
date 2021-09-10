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
// 아직 함수를 firebase에 올리지는 않았어요.
exports.scheduledFunctionCrontab = functions.pubsub.schedule('5 11 * * *')
.timeZone('America/New_York')
.onRun((context) => {
  console.log('This will be run every day at 11:05 AM Eastern!');

  database.ref('userlist/').once('value', (snapshot) => {
    if (snapshot.exists()) {
      snapshot.forEach((childSnapshot) => {
        var childKey = childSnapshot.key;
        var childData = childSnapshot.val();
        console.log(childData['username']);
        parsing(childData['username'])
        .then(userdata => {
          database.ref(`usertopics/${childData['username']}`).set({
            usertopics: userdata[0].topicName,
          });
          return userdata;
        })
      });
    } else {
      console.log("No data available");
    }
  }).catch((error) => {
    console.error(error);
  });
  return null;
    
});