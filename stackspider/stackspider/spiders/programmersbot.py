from scrapy import Request, Spider
from stackspider.items import JobCardItem, CompanyItem
from urllib.parse import urljoin, urlparse
import re
import json

class ProgrammersbotSpider(Spider):
    '''
        프로그래머스(https://programmers.co.kr) 의 개발자 채용(https://programmers.co.kr/job)을 크롤하는 스파이더

        포지션의 링크를 파싱하여 해당 기업의 기술 스택을 수집한다. 
        parse(self, response) 
            - https://programmers.co.kr/job?page={페이지 id} 의 최종 '페이지 id' 를 찾아 '페이지 id'의 새로운 콜백을 생성한다.
        parseContent(self, response)
            - https://programmers.co.kr/job?page={페이지 id} 내의 내용을 수집한다. 
            - 기업명과 해당되는 포지션의 정보 링크를 수집한다.
        parseJobContent(self, response)
            - https://programmers.co.kr/job_positions/{포지션 id} 내의 내용을 수집한다.
            - 해당 포지션의 '기술 스택' 태그를 수집한다.

        
    '''
    name = 'programmersbot'
    allowed_domains = ['programmers.co.kr']
    start_urls = ['https://www.programmers.co.kr/job']

    def parse(self, response):
        ''' 
            해당 웹에서 가져와야 할 전체 pagination의 개수를 반환한다. 
            /job?page={} 의 총 개수를 가져와 해당 각 페이지의 callback을 새로 만든다.
        '''
        pagination = response.css('div[id=paginate] a::attr(href)').getall()[-2]
        total_number_of_pages = int(re.findall(r'\d+', pagination)[0])
        
        for i in range(total_number_of_pages):
            url = urljoin(response.url, f"job?page={i+1}")
            yield Request(url, callback=self.parseContent)


    def parseContent(self, response):
        '''
            해당 pagination 의 새로운 url 에서 job position 의 href 링크를 가져온다.
        '''
        urls = response.css('ul[class=list-positions] a::attr(href)').getall()

        # Debug 용도로 사용
        # job_company_name = response.css('ul[class=list-positions] h6.company-name::text').getall()
        # job_company_name = [name.strip() for name in job_company_name if name.strip()]
        # self.logger.info("Current page's job urls: %s %s" % (job_urls, len(job_urls)))
        # self.logger.info("company name : %s %s" % (job_company_name, len(job_company_name)))
        # yield JobItem(href=job_urls, company_name=job_company_name)
        
        for link in urls:
            url = urljoin(response.url, "api/"+link)
            yield Request(url, callback=self.parseJobContent)


    def parseJobContent(self, response):
        '''
            해당 job-position page 를 파싱한다.
            Page의 `기술 스택` 정보를 수집한다.
            수집한 스택 정보를 scrapy의 Items 형태로 저장한다
        '''
        
        job_position_content_json = json.loads(response.text)

        card_content = JobCardItem()
        card_content['cardId'] = job_position_content_json['id']
        card_content['companyId'] = job_position_content_json['companyId']
        card_content['jobTitle'] = job_position_content_json['title']
        card_content['jobCategory'] = job_position_content_json['categoryNames']
        card_content['technicalTags'] = job_position_content_json['technicalTags'],
        card_content['teamTechnicalTags'] = job_position_content_json['teamTechnicalTags'],
        
        yield card_content
        url = urljoin(self.start_urls[0], f"companies/{job_position_content_json['companyId']}")
        yield Request(url, callback=self.parseCompanyContent)
        
    

    def parseCompanyContent(self, response):
        '''
            해당 기업의 소개 페이지에서 기술 스택을 수집합니다. 
        '''
        code = response.css('code::text').getall()
        companyId = int(urlparse(response.url).path[11:])
        url = urljoin(response.url[:28], f"api/companies/{companyId}")
        request = Request(
            url, 
            callback=self.parseCompanyApiContent,
            cb_kwargs=dict(stack_information=code)
        )

        yield request

    def parseCompanyApiContent(self, response, stack_information):
        '''
            수집한 스택 정보를 scrapy의 Items 형태로 저장한다
        '''
        json_response = json.loads(response.text)

        company_content = CompanyItem()
        company_content['companyId'] = json_response['id']
        company_content['companyName'] = json_response['name']
        company_content['companyTechnicalTags'] = stack_information
        company_content['companyLogo'] = json_response['logoUrl']
        company_content['address'] = json_response['address']
        company_content['lat'] = json_response['latitude']
        company_content['lng'] = json_response['longitude']
        company_content['homeUrl'] = json_response['homeUrl']

        yield company_content
        