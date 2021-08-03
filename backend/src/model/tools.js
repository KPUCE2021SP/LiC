const mongoose = require("mongoose");
const { composeWithMongoose } = require("graphql-compose-mongoose");
const Schema = mongoose.Schema;

const Tool = new Schema(
    {
        name: String,
        title: String,
        slug: String,
        canonicalUrl: String,
        id: String,
        imageUrl: String,
        ossRepo: String,
        description: String,
        websiteUrl: String,
    },
    { timestamps: { createdAt: "created_at", updateAt: "update_at" } }
);

module.exports = {
    ToolSchema: mongoose.model("tools", Tool),
    ToolTC: composeWithMongoose(mongoose.model("tools", Tool)),
};