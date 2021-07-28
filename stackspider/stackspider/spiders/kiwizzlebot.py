from scrapy import Request, Spider
from collections import defaultdict
from stackspider.items import CompanyItem
import json

class KiwizzlebotSpider(Spider):
    name = 'kiwizzlebot'
    allowed_domains = ['kiwizzle.com/search']
    start_urls = [f"https://kiwizzle.com/api/v1/job/{i}" for i in range(1,4800)]
    
    custom_settings = {
        'ITEM_PIPELINES': {
            'stackspider.pipelines.KiwizzlePipeline' : 300,
        }
    }
    def parse(self, response):    
        if(response.status == 200):            
            jsons = json.loads(response.text)
            yield jsons
        
        
             
    
