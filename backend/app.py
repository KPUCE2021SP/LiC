import asyncio
from pymongo import MongoClient
import json
from flask_graphql import GraphQLView
from schema import schema
from flask import Flask
from mongoengine import connect
# db = MongoClient("mongodb://seanhong2000:Suskyssc2@localhost:27017")

# a = json.load(fp=open("jumpit.json"))
# col = db['stackdb']['jumpit']
# for i in a:
#     col.insert_one(i)
class Model:
    def __init__(self):
        self.db = MongoClient("mongodb://seanhong2000:Suskyssc2@localhost:27017")
        self.collections = ["programmers", "kiwizzle", "jumpit"]
        self.L = []

    async def get_collections(self, collections_name):
        collection = self.db['stackdb'][collections_name]
        cursor = collection.find({"companyName":"쏘카"})
        return list(cursor)

    async def main(self):
        L = await asyncio.gather(
            self.get_collections(self.collections[0]),
            self.get_collections(self.collections[1]),
            self.get_collections(self.collections[2])
        )
        print(L)
        return L

app = Flask(__name__)
app.debug = True

app.add_url_rule(
    '/graphql',
    view_func=GraphQLView.as_view('graphql', schema=schema, graphiql=True)
)

# asyncio.run(Model().main())
if __name__ == '__main__':
    connect(host="mongodb://seanhong2000:Suskyssc2@127.0.0.1:27017/stackdb?authSource=admin", alias="default")
    app.run()