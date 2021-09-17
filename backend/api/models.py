from mongoengine import Document
from mongoengine.fields import (
    StringField,
    URLField,
    ListField,
    IntField,
)


# Create your models here.

class Company(Document):
    meta = {"collection": "companyStacks"}
    companyName = StringField()
    techStack = ListField(StringField())
    companyLogo = StringField()

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
