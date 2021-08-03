const { CompanyTC } = require("../model/companies");
require("../mutation/user.mutation");

const CompanyQuery = {
    companyById: CompanyTC.getResolver("findById"),
    companyByIds: CompanyTC.getResolver("findByIds"),
    companyOne: CompanyTC.getResolver("findOne"),
    companyMany: CompanyTC.getResolver("findMany"),
    companyCount: CompanyTC.getResolver("count"),
    companyConnection: CompanyTC.getResolver("connection"),
    companyPagination: CompanyTC.getResolver("pagination"),
};

const CompanyMutation = {
    companyCreateOne: CompanyTC.getResolver("createOne"),
    companyCreateMany: CompanyTC.getResolver("createMany"),
    companyUpdateById: CompanyTC.getResolver("updateById"),
    companyUpdateOne: CompanyTC.getResolver("updateOne"),
    companyUpdateMany: CompanyTC.getResolver("updateMany"),
    companyRemoveById: CompanyTC.getResolver("removeById"),
    companyRemoveOne: CompanyTC.getResolver("removeOne"),
    companyRemoveMany: CompanyTC.getResolver("removeMany"),
};

module.exports = { CompanyQuery, CompanyMutation };

