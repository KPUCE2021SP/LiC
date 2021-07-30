# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

from scrapy import Field, Item


class CompanyItem(Item):
    """
    companyId :
        기업 id 번호,
        INT
    companyName :
        기업명,
        STR
    companyTechnicalTags :
        기업 소개 페이지에 있는 소프트웨어 도구,
        LIST
    """

    companyId = Field()
    companyName = Field()
    companyTechnicalTags = Field()
