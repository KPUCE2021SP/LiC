const functions = require("firebase-functions");
const database = require("./config");
const parsing = require("./crawl");
const viewer = require("./graphql");

exports.lic = functions.https.onCall((context) => {
  const uid = context.auth.uid;
  console.log("uid: ", uid);
  
  // uid는 3이라고 임의로 해뒀지만 authentication에서 가져와야 한다. 
  // 이름, 상태, 상태 메시지, 이미지 url, topic들을 realtime database에 저장한다.
  function writeUserData(uid, name, emoji, bio, imageurl, topics) {
    console.log("writeUser start");
    database.ref(`users/${uid}`).set({
      username: name,
      useremoji: emoji,
      userbio: bio,
      userImage: imageurl,
      usertopics: topics,
    });
  }

  // grapql.js에서 받아온 데이터와 crawl.js에서 받아온 데이터를 바탕으로
  // 데이터를 저장해준다.
  const makerequest = (uid) => {
    console.log("makerequest start");
    viewer()
    .then(data => {
      const datav = data.viewer;
      const username = datav.name;
      const emoji = datav.status.emoji;
      const bio = datav.bio;
      const imageurl = datav.avatarUrl;
      const githubId = datav.login;
      parsing(githubId)
      .then(data => {
        userData = data;
        writeUserData(uid, username, emoji, bio, imageurl, userData[0].topicName);
      })
    })
  }
  
  makerequest(uid);
})
