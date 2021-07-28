# -*- coding: utf-8 -*-
from scrapy import Request, Spider
from stackspider.items import CompanyItem
from urllib.parse import urljoin, urlparse
import requests
import json

class JumpitbotSpider(Spider):
    name = 'jumpitbot'
    allowed_domains = ['jumpit.co.kr']
    start_urls = ['https://www.jumpit.co.kr']

    def parse(self, response):
        '''
        웹에서 list?page api를 토대로 전체 page의 개수를 찾고 
        page url을 호출한다.
            yield:
                각 page를 호출하여 callback 함수에서 반환된 값.
                반환 코드는 
                        정상 호출 시 : <200>
                        redirect 시 : <3**>
                        도중 문제 발생 시 : <4**> 또는 <5**>
        '''
        
        page_num = 0
        while True:
            api_data = urljoin(response.url, f'/api/position/list?page={page_num}')
            data = requests.get(api_data).text
            if len(data) < 400:
                break
            page_num += 1
            yield Request(api_data, callback=self.parseCompanyApiContent)

    def parseCompanyApiContent(self, response):
        '''
        기업 공고문의 각 페이지에 대한url을 얻는다.
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
        response_body = json.loads(response.text)

        for i in range(len(response_body['result']['content'])):
            company_id = response_body['result']['content'][i]['id']
            url = urljoin(response.url[:29], 'position/'+str(company_id))
            yield Request(url, callback=self.parseJobContent)

    
    def parseJobContent(self, response):
        '''
            Company ID에 따른 api url에서 가져온 json값을 정리한다.
        '''
        json_ = json.loads(response.text)
        json_response = json_['result']

        tech = []
        for i in range(len(json_response['techStacks'])):
            tech.append(json_response['techStacks'][i]['stack'])

        tech_stacks = {
            "id" : int(json_response['companyProfileId']),
            "name" : json_response['companyName'],
            "tech_stack" : tech
        }

        yield tech_stacks
        


       
        
