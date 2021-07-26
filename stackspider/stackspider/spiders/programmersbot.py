from scrapy import Request, Spider
from stackspider.items import CompanyItem
from urllib.parse import urljoin, urlparse
from scrapy.spidermiddlewares.httperror import HttpError
from twisted.internet.error import DNSLookupError
from twisted.internet.error import TimeoutError, TCPTimedOutError
from tqdm import tqdm
import re
import json

class ProgrammersbotSpider(Spider):
    '''
        프로그래머스(https://programmers.co.kr)
        개발자 채용(https://programmers.co.kr/job)을 크롤하는 스파이더
    '''
    name = 'programmersbot'
    allowed_domains = ['programmers.co.kr']
    start_urls = ['https://www.programmers.co.kr/companies']

    async def parse(self, response):
        '''
            기업 나열된 웹 페이지에서 해당 페이지의 전체 pagination 수를 가져오고,
            각 pagination에 대한 callback을 생성
        '''
        pagination = response.css('ul[class=pagination] a::attr(href)').getall()[-2]
        total_number_of_pages = int(re.findall(r'\d+', pagination)[0])
        
        for i in range(total_number_of_pages):
            url = urljoin(response.url, f"companies?page={i+1}")
            yield Request(url, callback=self.parseContent)


    async def parseContent(self, response):
        '''
            페이지 내에 있는 기업 페이지의 링크를 찾고 해당 url 페이지 호출
            웹 페이지에서 호출하는 api url 을 사용한다.
        '''
        list_of_companies = response.css('section[class=jobs__container] a::attr(href)').getall()
        
        for company_card_url in list_of_companies:
            url = urljoin(response.url, company_card_url)
            api_url = urljoin(response.url, "api/"+company_card_url)
            yield Request(url, callback=self.parseCompanyContent, errback=self.errback_httpbin)
    
    
    def parseCompanyContent(self, response):
        '''
            기업 소개 페이지에 있는 기술 스택 리스트를 매개변수로 다음 callback 에 넣어준다. 
        '''
        companyName = response.css('h1::text').get()
        list_of_tech_stacks = response.css('tr[class=heavy-use] code::text').getall()
        list_of_job_positions = response.css('ul[class=list-positions] a::attr(href)').getall()
        company_card = {
            "flag" : 1,
            "id" : int(response.url[36:]),
            "name" : companyName,
            "tech_stack" : list_of_tech_stacks
        }

        yield company_card

        for job_card_url in list_of_job_positions:
            url = urljoin(response.url[:26], "api/"+job_card_url)
            request = Request(url, callback=self.parseJobCardContent, errback=self.errback_httpbin)
            yield request
    
    
    def parseJobCardContent(self, response):
        json_of_job_card_content = json.loads(response.text)
        tech_stacks = {
            "flag" : 0,
            "id" : int(json_of_job_card_content["companyId"]),
            "tech_stack" : list(set(json_of_job_card_content["technicalTags"] + json_of_job_card_content["teamTechnicalTags"]))
        }
        yield tech_stacks


    def errback_httpbin(self, failure):
        self.logger.error(repr(failure))

        if failure.check(HttpError):
            response = failure.value.response
            self.logger.error('HttpError on %s', response.url)

        elif failure.check(DNSLookupError):
            request = failure.request
            self.logger.error('DNSLookupError on %s', request.url)

        elif failure.check(TimeoutError, TCPTimedOutError):
            request = failure.request
            self.logger.error('TimeoutError on %s', request.url)
        

