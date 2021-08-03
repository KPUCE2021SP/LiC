const express = require("express");
const app = express();
const mongoose = require("mongoose");
const { graphqlHTTP } = require("express-graphql");
// const logger = require("../core/logger");
const extensions = ({ context }) => {
    return {
        runTime: Data.now() - context.startTime,
    };
};

// app.use();

app.listen(5000, async () => {
    console.log("server is running at, ", 5000);
    await mongoose.connect("mongodb://localhost:27017/graphql", {
        useNewUrlParser: true,
        useUnifiedTopology: true,
    });
});

mongoose.connection.on(
    "error", 
    console.error.bind(console, "MongoDB connection error:")
);

const graphqlSchema = require("./schemas/index");
app.use(
    "/graphql",
    graphqlHTTP((request) => {
        return {
            context: { startTime: Date.now() },
            graphql: true,
            schema: graphqlSchema,
            extensions,
        };
    })
);