# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class TutorialItem(scrapy.Item):
    # define the fields for your item here like:
    name = scrapy.Field()
    link = scrapy.Field()
    imgSrc = scrapy.Field()
    imgScore = scrapy.Field()
    baselink = scrapy.Field()
    location = scrapy.Field()
    dateJoined = scrapy.Field()
    description = scrapy.Field()
    nameScore = scrapy.Field()
    locScore = scrapy.Field()
    wordScore = scrapy.Field()
    platform = scrapy.Field()
    image_urls = scrapy.Field()
    sessionID = scrapy.Field()
    lastUsed = scrapy.Field()
    totalScore = scrapy.Field()
    work = scrapy.Field()
    education = scrapy.Field()