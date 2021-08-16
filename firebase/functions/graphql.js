const { graphql } = require("@octokit/graphql");


// github api-v4를 이용하여 뷰어의 상태정보 받아오기.
// token은 github에서 발급받은 토큰을 넣어주면 된다.
const graphqlWithAuth = graphql.defaults({
  headers: {
    authorization: `token ${token}`,
  },
});

// 로그인 정보, 이름, emoji, bio, 이미지 url을 grapql api를 이용해 받아온다.
async function viewer() {
  const data = await graphqlWithAuth(`
  {
    viewer { 
      login
      name
      status{
        emoji
      }
		  bio
      avatarUrl
    }
  }
`)
return data;
};

module.exports = viewer;