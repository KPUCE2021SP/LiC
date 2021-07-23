# -*- coding: utf-8 -*-
from scrapy import Request, Spider
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
            url = urljoin(response.url, f'/api/position/list?page={page_num}')
            data = requests.get(url).text
            if len(data) < 400:
                break
            page_num += 1
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
        
        card_content = JobCardItem()
        card_content['cardId'] = job_position_content_json['id']               
        card_content['companyId'] = job_position_content_json['companyId']
        card_content['jobTitle'] = job_position_content_json['title']
        card_content['jobCategory'] = job_position_content_json['categoryNames']
        card_content['technicalTags'] = job_position_content_json['technicalTags'],
        card_content['teamTechnicalTags'] = job_position_content_json['teamTechnicalTags'],

        
