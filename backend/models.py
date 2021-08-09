from mongoengine import Document
from mongoengine.fields import (
    StringField, 
    URLField, 
    ListField,
    IntField,
)

class Company(Document):
    meta = {"collection": "companyStacks"}
    companyName = StringField()
    techStack = ListField(StringField())

class Tool(Document):
    meta = {"collection": "tools"}
    _id = StringField()
    name = StringField()
    title = StringField()
    slug = StringField()
    canonicalUrl = URLField()
    id = IntField()
    imageUrl = URLField()
    ossRepo = URLField()
    description = StringField()
    websiteUrl = URLField()