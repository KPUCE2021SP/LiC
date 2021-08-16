const { graphql } = require("@octokit/graphql");


// github api-v4를 이용하여 뷰어의 상태정보 받아오기.
const graphqlWithAuth = graphql.defaults({
  headers: {
    authorization: `token ${token}`,
  },
});

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