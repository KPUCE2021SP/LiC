const { ToolTC } = require("../model/tools");
require("../mutation/user.mutation");

const ToolQuery = {
    toolById: ToolTC.getResolver("findById"),
    toolByIds: ToolTC.getResolver("findByIds"),
    toolOne: ToolTC.getResolver("findOne"),
    toolMany: ToolTC.getResolver("findMany"),
    toolCount: ToolTC.getResolver("count"),
    toolConnection: ToolTC.getResolver("connection"),
    toolPagination: ToolTC.getResolver("pagination"),
};

const ToolMutation = {
    toolCreateOne: ToolTC.getResolver("createOne"),
    toolCreateMany: ToolTC.getResolver("createMany"),
    toolUpdateById: ToolTC.getResolver("updateById"),
    toolUpdateOne: ToolTC.getResolver("updateOne"),
    toolUpdateMany: ToolTC.getResolver("updateMany"),
    toolRemoveById: ToolTC.getResolver("removeById"),
    toolRemoveOne: ToolTC.getResolver("removeOne"),
    toolRemoveMany: ToolTC.getResolver("removeMany"),
};

module.exports = { ToolQuery, ToolMutation };