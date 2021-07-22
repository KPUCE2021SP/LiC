# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

from scrapy import Field, Item


class JobCardItem(Item):
    '''
        각 job-position id 링크에서 보여주는 개발자 채용 내용에서의 정보를 저장
    '''
    cardId = Field()
    companyId = Field()
    jobTitle = Field()
    jobCategory = Field()
    technicalTags = Field()
    teamTechnicalTags = Field()
    


class CompanyItem(Item):
    '''
        기업 id 링크에서 보여주는 개발자 채용 내용에서의 정보를 저장
    '''
    companyId = Field()
    companyName = Field()
    companyTechnicalTags = Field()
    companyLogo = Field()
    address = Field()
    lat = Field()
    lng = Field()
    homeUrl = Field()
