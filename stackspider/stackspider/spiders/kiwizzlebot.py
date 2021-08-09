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
    start_urls = [f"https://kiwizzle.com/api/v1/job/{i}" for i in range(4799, 4800)]

    custom_settings = {
        "ITEM_PIPELINES": {
            # "stackspider.pipelines.KiwizzlePipeline": 300,  # 호스팅 하면 해당 파이프라인 다시 추가하기
            "stackspider.pipelines.RawPipeline": 800,
        }
    }

    def parse(self, response):
        # if response.status == 200:
        #     jsons = json.loads(response.text)
        #     yield jsons
        ...
