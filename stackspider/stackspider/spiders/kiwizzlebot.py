from scrapy import Request, Spider
from collections import defaultdict
from stackspider.items import CompanyItem
import json

# "https://static.toss.im/greenhouse/jobs/jobs.json?v=2"
# "https://career.woowahan.com/w1/recruits?category=jobGroupCodes%3ABA005001&recruitCampaignSeq=0&jobGroupCodes=BA005001&page=0&size=100&sort=updateDate%2Cdesc"


class KiwizzlebotSpider(Spider):
    """
    키위즐(https://programmers.co.kr)
    개발자 채용(https://programmers.co.kr/job)을 크롤하는 스파이더
    """

    name = "kiwizzlebot"
    allowed_domains = ["kiwizzle.com/search"]
    start_urls = [f"https://kiwizzle.com/api/v1/job/{i}" for i in range(10000, 19164)]

    custom_settings = {
        "CONCURRENT_REQUESTS" : 30,
        "DOWNLOAD_DELAY" : 0.1,
        "ITEM_PIPELINES": {
            # "stackspider.pipelines.RawPipeline": 100,
            "stackspider.pipelines.KiwizzlePipeline": 300,  # 호스팅 하면 해당 파이프라인 다시 추가하기
        }
    }

    def start_requests(self):
        headers = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64; rv:48.0) Gecko/20100101 Firefox/48.0'}
        for url in self.start_urls:
            yield Request(url, headers=headers)

    def parse(self, response):
        if response.status == 200:
            jsons = json.loads(response.text)
            yield jsons
    
