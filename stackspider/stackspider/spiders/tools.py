import scrapy


class ToolsSpider(scrapy.Spider):
    name = 'tools'
    allowed_domains = ['']
    start_urls = ['']

    def parse(self, response):
        ...
