import graphene
from graphene.relay import Node
from graphene_mongo import MongoengineConnectionField, MongoengineObjectType
from models import CompanyJumpit as JumpitModel
from models import CompanyKiwizzle as KiwizzleModel
from models import CompanyProgrammers as ProgrammersModel
from models import Tool as ToolModel

class Jumpit(MongoengineObjectType):

    class Meta:
        model = JumpitModel
        interfaces = (Node,)


class Kiwizzle(MongoengineObjectType):

    class Meta:
        model = KiwizzleModel
        interfaces = (Node,)


class Programmers(MongoengineObjectType):

    class Meta:
        model = ProgrammersModel
        interfaces = (Node,)


class Tools(MongoengineObjectType):

    class Meta:
        model = ToolModel
        interfaces = (Node,)


class Query(graphene.ObjectType):
    node = Node.Field()
    all_companies = MongoengineConnectionField(Jumpit)
    all_tools = MongoengineConnectionField(Tools)
    tool = graphene.Field(Tools)

schema = graphene.Schema(query=Query, types=[Jumpit, Kiwizzle, Programmers, Tools])