from scrapy import Request, Spider
from stackspider.items import CompanyItem
from urllib.parse import urljoin, urlparse
import re
import json

class ProgrammersbotSpider(Spider):
    '''
        프로그래머스(https://programmers.co.kr)
        개발자 채용(https://programmers.co.kr/job)을 크롤하는 스파이더
    '''
    name = 'programmersbot'
    allowed_domains = ['programmers.co.kr']
    start_urls = ['https://www.programmers.co.kr/job']

    def parse(self, response):
        '''
        해당 웹에서 가져와야 할 전체 pagination의 개수를 찾고 각 pagination url 호출
                yield:
                    각 pagination의 페이지를 호출하고 callback 함수에서 반환된 값
                    반환 코드는 
                        정상 호출 시 : <200>
                        redirect 시 : <3**>
                        도중 문제 발생 시 : <4**> 또는 <5**>
        '''
        pagination = response.css('div[id=paginate] a::attr(href)').getall()[-2]
        total_number_of_pages = int(re.findall(r'\d+', pagination)[0])
        
        for i in range(total_number_of_pages):
            url = urljoin(response.url, f"job?page={i+1}")
            yield Request(url, callback=self.parseContent)


    def parseContent(self, response):
        '''
            페이지 내에 있는 직무 페이지의 링크를 찾고 해당 url 페이지 호출
            웹 페이지에서 호출하는 api url 을 사용한다.
                yield:
                    각 직무 id 페이지 url에 대한 반환 값
                    반환 코드는 
                        정상 호출 시 : <200>
                        redirect 시 : <3**>
                        도중 문제 발생 시 : <4**> 또는 <5**>
        '''
        urls = response.css('ul[class=list-positions] a::attr(href)').getall()
        
        for link in urls:
            url = urljoin(response.url, "api/"+link)
            yield Request(url, callback=self.parseJobContent)


    def parseJobContent(self, response):
        '''
            job-position 페이지를 파싱한다.
            job-position 은 api url 이 따로 있어서 해당 url을 호출 후 json reponse 에서 값 정리
            yield:
            TODO:
                현재 job_position 내용과 company_id 내용이 곂쳐서 같은 collection document에 
                저장이 되고 있다. pipeline 호출을 별도의 pipeline 을 설정해서 해야함
                card_content : 
                    Job Position api url 에서의 필요한 json 값을 따로 정리 후 
                    MongoDB pipeline 을 활용하여 연결된 MongoDB에 document로 저장
                        - 해당 Item객체의 자세한 정보는 items.py 참고
                        - class JobCardItem(Item)
            yield:
                company id 만 따로 정리해준 url을 호출한다.
                Request:
                    url : company id별 정보 페이지 url
                    callback : 호출 및 값을 반환할 callback 함수
                반환 코드는 
                        정상 호출 시 : <200>
                        redirect 시 : <3**>
                        도중 문제 발생 시 : <4**> 또는 <5**>
        '''
        
        job_position_content_json = json.loads(response.text)

        job_card_stack = list(set(job_position_content_json['technicalTags'] + job_position_content_json['teamTechnicalTags']))
        
        url = urljoin(self.start_urls[0], f"companies/{job_position_content_json['companyId']}")
        request = Request(
            url, 
            callback=self.parseCompanyContent,
            meta={"job_card_stack": job_card_stack}
        )

        yield request
        
    

    def parseCompanyContent(self, response):
        '''
            기업 소개 페이지에 있는 기술 스택 리스트를 매개변수로 다음 callback 에 넣어준다. 
            yield:
                Request:
                    url : company id 마다 있는 api 페이지 url
                    callback : 호출 및 값을 반환할 callback 함수
                    cb_kwargs : callback 함수에 넘길 매개변수
        '''
        code = response.css('code::text').getall()
        stack_information = list(set(code + response.meta.get('job_card_stack')))
        companyId = int(urlparse(response.url).path[11:])
        url = urljoin(response.url[:28], f"api/companies/{companyId}")
        request = Request(
            url, 
            callback=self.parseCompanyApiContent,
            cb_kwargs=dict(stack_information=stack_information)
        )

        yield request

    def parseCompanyApiContent(self, response, stack_information):
        '''
            Company ID 에 따른 api url 에서 가져온 json 값을 정리한다.
            yield:
                comapny_content : 
                    MongoDB pipeline 을 활용하여 연결된 MongoDB에 document로 저장
                        - 해당 Item객체의 자세한 정보는 items.py 참고 
                        - class CompanyItem(Item)
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
        