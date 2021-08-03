const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const { composeWithMongoose } = require("graphql-compose-mongoose");

const Company = new Schema(
    {
        companyName: String,
        tech_stack: [String],
    },
    { timestamps: { createdAt: "created_at", updateAt: "update_at" } }
)

module.exports = {
    CompanySchema: mongoose.model("company", Company),
    CompanyTC: composeWithMongoose(mongoose.model("company", Company)),
};

