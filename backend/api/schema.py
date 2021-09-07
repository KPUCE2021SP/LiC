import graphene
from graphene.relay import Node
from graphene_mongo import MongoengineConnectionField, MongoengineObjectType
from .models import Company as CompanyModel, Tool
from .models import Tool as ToolModel

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

    company_by_name = graphene.Field(Companies, name=graphene.String(required=True))
    tool_by_name = graphene.Field(Tools, name=graphene.String(required=True))
    tool_by_slug = graphene.Field(Tools, slug=graphene.String(required=True))


    def resolve_company_by_name(root, info, name):
        try:
            return CompanyModel.objects.get(companyName=name)
        except CompanyModel.DoesNotExist:
            return None

    def resolve_tool_by_name(root, info, name):
        try:
            return ToolModel.objects.get(name=name)
        except ToolModel.DoesNotExist:
            return None 
        
    def resolve_tool_by_slug(root, info, slug):
        try:
            return ToolModel.objects.get(slug=slug)
        except ToolModel.DoesNotExist:
            return None


schema = graphene.Schema(query=Query, types=[Companies, Tools])