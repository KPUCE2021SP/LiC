# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter
from pymongo import MongoClient
import logging
from scrapy.exceptions import DropItem
from scrapy.utils.project import get_project_settings
settings = get_project_settings()


# class StackspiderPipeline:
#     def process_item(self, item, spider):
#         adapter = ItemAdapter(item)
#         return item


class MongoDBPipeline:
    def __init__(self):
        connection = MongoClient(
            host=settings['MONGODB_SERVER'],
            port=settings['MONGODB_PORT'],
            username=settings['USERNAME'],
            password=settings['PASSWORD']
        )

        db = connection[settings['MONGODB_DB']]
        self.collection = db[settings['MONGODB_COLLECTION'][0]] # set to programmers


    def process_item(self, item, spider):
        # how to handle each post
        valid = True
        for data in item:
            if not data:
                valid = False
                raise DropItem("Missing {0}!".format(data))
        if valid:
            self.collection.insert(dict(item))
            logging.debug("Posted to MongoDB database!")
        return item
