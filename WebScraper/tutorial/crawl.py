import scrapy
from.spiders.quotes_spider import QuotesSpider
from scrapy.crawler import CrawlerProcess

class MySpider(scrapy.Spider):
    # Your spider definition
    ...

process = CrawlerProcess({
    'USER_AGENT': 'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)'
})

process.crawl(QuotesSpider)
process.crawl(QuotesSpider2)
process.start() # the script will block here until the crawling is finished