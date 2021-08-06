from mongoengine import Document
from mongoengine.fields import (
    StringField, ListField, DictField, URLField, IntField
)

class CompanyProgrammers(Document):
    meta = {"collection": "programmers"}
    companyName = StringField()
    techStack = ListField()


class CompanyKiwizzle(Document):
    meta = {"collection": "kiwizzle"}
    companyName = StringField()
    techStack = ListField()


class CompanyJumpit(Document):
    meta = {"collection": "jumpit"}
    companyName = StringField()
    techStack = ListField()


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