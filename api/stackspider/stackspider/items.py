# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

from scrapy import Field, Item


class CompanyItem(Item):
    '''
    companyId : 
        기업 id 번호, 
        INT
    companyName : 
        기업명, 
        STR
    companyTechnicalTags : 
        기업 소개 페이지에 있는 소프트웨어 도구, 
        LIST
    companyLogo : 
        기업 로고 url, 
        STR
    address : 
        기업 주소, 
        STR
    lat : 
        기업 latitude, 
        FLOAT
    lng : 
        기업 logitude, 
        FLOAT
    homeUrl : 
        기업 홈페이지, 
        STR
    '''
    companyId = Field()
    companyName = Field()
    companyTechnicalTags = Field()
    companyLogo = Field()
    address = Field()
    lat = Field()
    lng = Field()
    homeUrl = Field()
