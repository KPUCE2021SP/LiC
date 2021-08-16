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

// crawling한 데이터를 parsing
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
