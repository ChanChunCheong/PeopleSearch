# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html

import sqlite3
import json
from datetime import datetime
from elasticsearch import Elasticsearch
from scrapy.utils.project import get_project_settings
from kafka import KafkaProducer
from kafka.errors import KafkaError

class TutorialPipeline(object):
    def __init__(self):
        self.es = Elasticsearch()
        self.settings = get_project_settings()
        self.count = 1
    
    def process_item(self, item, spider):
        self.store_db(item)
        print(json.dumps(dict(item)))
        return item

    def store_db(self, item):
        
        res = self.es.index(index= self.settings['ELASTICSEARCH_INDEX'], doc_type= self.settings['ELASTICSEARCH_TYPE'], body=dict(item))
        self.count += 1 
        print(res['result'])

    # def close_spider(self, spider):
    #     producer = KafkaProducer(bootstrap_servers=['localhost:9092'], retries=5)
    #     # Asynchronous by default
    #     future = producer.send('test2', b'done')

    #     # Block for 'synchronous' sends
    #     try:
    #         record_metadata = future.get(timeout=10)
    #     except KafkaError:
    #         # Decide what to do if produce request failed...
    #         # log.exception()
    #         pass

    #     # Successful result returns assigned partition and offset
    #     print (record_metadata.topic)
    #     print (record_metadata.partition)
    #     print (record_metadata.offset)
    #     print("Ended")
