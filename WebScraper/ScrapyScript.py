import scrapy
from twisted.internet import reactor, defer
from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings
from tutorial.spiders.quotes_spider import QuotesSpider
from tutorial.spiders.quotes_spider2 import QuotesSpider2
from kafka import KafkaConsumer, KafkaProducer
from kafka.errors import KafkaError
import json
from io import BytesIO 
import base64

process = CrawlerProcess(get_project_settings())

@defer.inlineCallbacks
def _crawl(result, spider, spider2):
    consumer = KafkaConsumer('test', value_deserializer=lambda m: json.loads(m.decode('utf-8')), auto_offset_reset='latest', enable_auto_commit=True)
    producer = KafkaProducer(bootstrap_servers=['localhost:9092'], retries=5)
    platforms = []
    for msg in consumer:
        # extract message from Json 
        message = msg.value.get('id')
        name = msg.value.get('name')
        country = msg.value.get('location')  
        keywords = msg.value.get('keywords') 
        numPage = msg.value.get('numPage') 
        platforms = msg.value.get('platforms')
        sessionID = msg.value.get('sessionID')
        encoded_refImage_bytes = bytes(msg.value.get('file'), 'utf-8')
        refImage_bytes2 = base64.b64decode(encoded_refImage_bytes)
        if message == "scrape":
            break 
    consumer.close()
    if (len(platforms) == 1):
        if(platforms[0] == 'twitter'):
            process.crawl(spider, refImage_bytes=refImage_bytes2, refName=name, refCountry=country, refWord=keywords, sessionID = sessionID, numPage = numPage)
        else:
            process.crawl(spider2, refImage_bytes=refImage_bytes2, refName=name, refCountry=country, refWord=keywords, sessionID = sessionID, numPage = numPage)
    else: 
       process.crawl(spider, refImage_bytes=refImage_bytes2, refName=name, refCountry=country, refWord=keywords, sessionID = sessionID, numPage = numPage)
       process.crawl(spider2, refImage_bytes=refImage_bytes2, refName=name, refCountry=country, refWord=keywords, sessionID = sessionID, numPage = numPage)
    yield process.join()
    future = producer.send('test', b'done')

    # Block for 'synchronous' sends
    try:
        record_metadata = future.get(timeout=10)
    except KafkaError:
        pass

    _crawl(None, QuotesSpider, QuotesSpider2)
    return None
_crawl(None, QuotesSpider, QuotesSpider2)
process.start()

