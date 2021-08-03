const { SchemaCompose, SchemaComposer } = require('graphql-compose');

const schemaComposer = new SchemaComposer();

const { CompanyQuery, CompanyMutation } = require('./company');
const { ToolQuery, ToolMutation } = require('./tool');

schemaComposer.Query.addFields({
    ...CompanyQuery,
    ...ToolQuery,
});

schemaComposer.Mutation.addFields({
    ...CompanyMutation,
    ...ToolMutation,
});

module.exports = schemaComposer.buildSchema();