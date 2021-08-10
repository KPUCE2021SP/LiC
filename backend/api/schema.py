import graphene
from graphene.relay import Node
from graphene_mongo import MongoengineConnectionField, MongoengineObjectType
from models import Company as CompanyModel
from models import Tool as ToolModel

class Companies(MongoengineObjectType):

    class Meta:
        model = CompanyModel
        interfaces = (Node,)

class Tools(MongoengineObjectType):

    class Meta:
        model = ToolModel
        interfaces = (Node,)


class Query(graphene.ObjectType):
    node = Node.Field()
    all_companies = MongoengineConnectionField(Companies)
    all_tools = MongoengineConnectionField(Tools)


schema = graphene.Schema(query=Query, types=[Companies, Tools])