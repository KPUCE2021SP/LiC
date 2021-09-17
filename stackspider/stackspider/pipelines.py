# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from typing import List
from itemadapter import ItemAdapter
from pymongo import MongoClient
import logging
from scrapy.exceptions import DropItem
from scrapy.utils.project import get_project_settings
import json
from collections import defaultdict

settings = get_project_settings()


class ProgrammersPipeline:
    """
    Pipeline used for programmersbot to create and update MONGODB collection
    """

    def open_spider(self, spider):
        self.json_response = defaultdict()
        connection = MongoClient(
            host=settings["MONGODB_SERVER"],
            port=settings["MONGODB_PORT"],
            username=settings["USERNAME"],
            password=settings["PASSWORD"],
        )
        db = connection[settings["MONGODB_DB"]]
        self.collection = db[settings["MONGODB_COLLECTION"][0]]  # set to programmers

    def close_spider(self, spider):
        for key in self.json_response:
            self.json_response[key]["techStack"] = list(
                set(self.json_response[key]["techStack"])
            )
            document = self.collection.find_one(
                {"companyName": self.json_response[key]["companyName"]}
            )
            if not document == None:
                if not len(document["techStack"]) == len(
                    self.json_response[key]["techStack"]
                ):
                    tech_stack = document["techStack"]
                    tech_stack.extend(self.json_response[key]["techStack"])
                    self.json_response[key]["techStack"] = list(set(tech_stack))

            self.collection.update_one(
                {"companyName": self.json_response[key]["companyName"]},
                {"$set": self.json_response[key]},
                upsert=True,
            )
    def process_item(self, item, spider):
        if item["flag"]:
            self.json_response[str(item["id"])] = {"companyName": "", "techStack": [], "companyLogo": ""}
            logging.info(f"NAME : {item['name']}")
            self.json_response[str(item["id"])]["companyName"] = item["name"]
            for tech in item["techStack"]:
                self.json_response[str(item["id"])]["techStack"].append(tech)
            self.json_response[str(item["id"])]["companyLogo"] = item["companyLogo"]
            
        else:
            for tech in item["techStack"]:
                self.json_response[str(item["id"])]["techStack"].append(tech)

        return item


class JumpitPipeline:
    """
    Pipeline used for jumpitbot to create and update MONGODB collection
    """

    def open_spider(self, spider):
        self.json_response = defaultdict()
        connection = MongoClient(
            host=settings["MONGODB_SERVER"],
            port=settings["MONGODB_PORT"],
            username=settings["USERNAME"],
            password=settings["PASSWORD"],
        )
        db = connection[settings["MONGODB_DB"]]
        self.collection = db[settings["MONGODB_COLLECTION"][0]]  # set to jumpit

    def close_spider(self, spider):
        for key in self.json_response:
            self.json_response[key]["techStack"] = list(
                set(self.json_response[key]["techStack"])
            )
            document = self.collection.find_one(
                {"companyName": self.json_response[key]["companyName"]}
            )
            if not document == None:
                if not len(document["techStack"]) == len(
                    self.json_response[key]["techStack"]
                ):
                    tech_stack = document["techStack"]
                    tech_stack.extend(self.json_response[key]["techStack"])
                    self.json_response[key]["techStack"] = list(set(tech_stack))

            self.collection.update_one(
                {"companyName": self.json_response[key]["companyName"]},
                {"$set": self.json_response[key]},
                upsert=True,
            )

    def process_item(self, item, spider):
        if str(item["id"]) in self.json_response:
            for tech in item["techStack"]:
                self.json_response[str(item["id"])]["techStack"].append(tech)
        else:
            self.json_response[str(item["id"])] = {"companyName": "", "techStack": [], "companyLogo": ""}
            self.json_response[str(item["id"])]["companyName"] = item["name"]
            for tech in item["techStack"]:
                self.json_response[str(item["id"])]["techStack"].append(tech)

        return item

class KiwizzlePipeline:
    """
    Pipeline used for kiwizzlebot to create and update MONGODB collection
    WARNING: 해당 웹페이지 관리자가 너무 많은 호출을 한 IP에서 하면 차단함
    """

    def open_spider(self, spider):
        self.key = json.load(fp=open("kiwizzle_api.json", "r"))
        connection = MongoClient(
            host=settings["MONGODB_SERVER"],
            port=settings["MONGODB_PORT"],
            username=settings["USERNAME"],
            password=settings["PASSWORD"],
        )
        db = connection[settings["MONGODB_DB"]]
        self.collection = db[settings["MONGODB_COLLECTION"][0]]  # set to kiwizzle

        self.json_content = defaultdict()

    def close_spider(self, spider):
        for key in self.json_content:
            self.json_content[key]["techStack"] = list(
                set(self.json_content[key]["techStack"])
            )
            document = self.collection.find_one(
                {"companyName": self.json_response[key]["companyName"]}
            )
            if not document == None:
                if not len(document["techStack"]) == len(
                    self.json_response[key]["techStack"]
                ):
                    tech_stack = document["techStack"]
                    tech_stack.extend(self.json_response[key]["techStack"])
                    self.json_response[key]["techStack"] = list(set(tech_stack))

            self.collection.update_one(
                {"companyName": self.json_response[key]["companyName"]},
                {"$set": self.json_response[key]},
                upsert=True,
            )

    def process_item(self, item, spider):
        try:
            self.json_content[str(item["companyId"])]
        except KeyError:
            self.json_content[str(item["companyId"])] = {
                "companyName": "",
                "techStack": [],
                "companyLogo": ""
            }

        self.json_content[str(item["companyId"])]["companyName"] = self.key["company"][
            str(item["companyId"])
        ]

        for language_key in item["language"]:
            self.json_content[str(item["companyId"])]["techStack"].append(
                self.key["language"][str(language_key)]
            )

        return item


class RawPipeline:
    """
    Pipeline used for Raw Json files for leftover data
    Creates or Updates Kiwizzle collection documents
    """

    def open_spider(self, spider):
        connection = MongoClient(
            host=settings["MONGODB_SERVER"],
            port=settings["MONGODB_PORT"],
            username=settings["USERNAME"],
            password=settings["PASSWORD"],
        )
        db = connection[settings["MONGODB_DB"]]
        self.collection = db[settings["MONGODB_COLLECTION"][0]]  # set to kiwizzle

        self.json_content = json.load(fp=open("raw_data.json"))

    def close_spider(self, spider):
        for key in self.json_content:
            document = self.collection.find_one({"companyName": key})
            if not document == None:
                if not len(document["techStack"]) == len(self.json_content[key]):
                    tech_stack = document["techStack"]
                    tech_stack.extend(self.json_content[key])
                    self.json_content[key] = list(set(tech_stack))

            self.collection.update_one(
                {"companyName": key},
                {"$set": {
                    "techStack": self.json_content[key],
                    "companyLogo": ""
                    }
                },
                upsert=True,
            )


    def process_item(self, item, spider):
        ...


class JsonPipeline:
    def open_spider(self, spider):
        self.json_response = defaultdict()
        connection = MongoClient(
            host=settings["MONGODB_SERVER"],
            port=settings["MONGODB_PORT"],
            username=settings["USERNAME"],
            password=settings["PASSWORD"],
        )
        db = connection[settings["MONGODB_DB"]]
        self.collection = db[settings["MONGODB_COLLECTION"][1]]  # set to tools collection


    def close_spider(self, spider):
        fixture = json.load(fp=open("stack_fixture.json"))
        if not self.collection.find_one({"id": 0}):
            self.collection.insert_many(fixture)


    def process_item(self, item, spider):
        ...
