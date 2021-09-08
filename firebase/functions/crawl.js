const axios = require("axios");
const cheerio = require("cheerio");

// github topic 크롤링하기 위한 async await
const getHTML = async(username) => {
  try {
    return await axios.get(`https://github.com/stars/${encodeURI(username)}/topics`)
    
  }catch(err) {
    console.log(err);
  }
}

// 이유는 모르겠지만, 17번 줄이 실행되는데 시간이 오래걸린다.
// 사용자의 star한 topics들을 userData에 넣어준다음 반환해준다.
const parsing = async(username) => {
  const html = await(getHTML(username));
  const $ = cheerio.load(html.data);
  const $topicList = $(".col-md-9 > ul > li");
  
  let topics = [];
  let userData = [];
  $topicList.each((idx, node) => {
    topics.push(
      $(node).find("a > div > p.f3.lh-condensed.mt-1.mt-md-0.mb-0").text()
    );
  });
  userData.push({
    username: username,
    topicName: topics,
  })
  return userData;
}


module.exports = parsing;