import scrapy
import time
import json
from selenium import webdriver
from ..items import TutorialItem
from kafka import KafkaConsumer
from kafka import TopicPartition
from urllib.parse import urlencode
from urllib.parse import unquote
import urllib.parse
from .calcImage import calcImgScore, calcRefImg, calcImgScore_Highest
from .calcLocation import calcLocScore
from .calcName import calcNameScore
from .calcKeyword import calcKeywordScore
from .calcTotalScore import calcTotalScore
from .refCountryandCities import getRefCities, getRefCountries, getRefLatLong
from io import BytesIO 
import base64

class QuotesSpider2(scrapy.Spider):
    name = "quotes3"

    def __init__(self, refImage_bytes, refName, refCountry, refWord, sessionID, numPage):
        self.my_face_encoding = calcRefImg(BytesIO(refImage_bytes))
        self.start_urls = ["https://www.facebook.com/public/" + urllib.parse.quote(refName)]
        self.driver = webdriver.Chrome(executable_path=r'C:\Users\CChunChe\Downloads\chromedriver_win32\chromedriver.exe')  # Optional argument, if not specified will search path.
        self.refName = refName
        self.refCountry = refCountry
        self.refWord = refWord
        self.citiesList = getRefCities('tutorial\spiders\database\worldcities.csv')
        self.countriesList = getRefCountries('tutorial\spiders\database\countries.csv')
        self.refLatLong = getRefLatLong(self.countriesList, self.refCountry)
        self.sessionID = sessionID
        self.page_count = numPage

    #call this function when the link has been visited
    def parseLink(self, response):
        print(response.request.url)
        items = response.meta['items']
        name = response.css('._2nlv::text').get(default='')
        link = 'Profile'
        imgSrc = response.css('img._11kf.img::attr(src)').get()
        location = response.css('#current_city a::text').get(default='')
        dateJoined = ''
        description = ''
        baselink = unquote(response.request.url)
        imglist = response.css('._xcx img::attr(src)').getall() 
        education = response.css('#u_0_g a::text').get(default='')
        work = response.css('#u_0_f a::text').get(default='')

        nameScore = calcNameScore(self.refName, name)
        imgScore = calcImgScore_Highest(self.my_face_encoding, imgSrc, imglist)
        locScore = calcLocScore(self.refLatLong[0], self.refLatLong[1], location, self.countriesList, self.citiesList)
        education_wordScore = calcKeywordScore(education , self.refWord)
        work_wordScore = calcKeywordScore(work , self.refWord)
        wordScore = education_wordScore + work_wordScore
       
        # city = response.css('#current_city a::text').get(default='')
        # hometown = response.css('#hometown .fcg::text').get(default='')

        items['name'] = name
        items['link'] = link
        items['imgSrc'] = imgSrc
        items['baselink'] = baselink
        items['location'] = location
        items['dateJoined'] = dateJoined
        items['description'] = description
        items['nameScore'] = nameScore
        items['imgScore'] = imgScore
        items['locScore'] = locScore
        items['wordScore'] = wordScore
        items['platform'] = 'Facebook'
        items['image_urls'] = imglist
        items['lastUsed'] = "yes"
        items['sessionID'] = self.sessionID
        items['totalScore'] = calcTotalScore(nameScore, imgScore, locScore, wordScore)
        items['work'] = work
        items['education'] = education

        yield items

    def parse(self, response):
        count = 0
        self.driver.get(response.url)

 # Let the user actually see something!
        # lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        # count += 1 
        # time.sleep(1)
        # while(True):
        #     if(count >= self.page_count):  
        #         break 
        #     lastCount = lenOfPage
        #     lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        #     count += 1
        #     if(lastCount == lenOfPage):  
        #         break 
        #     time.sleep(3)
        lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        match = False
        time.sleep(5)
        # while(match==False):
        #     lastCount = lenOfPage
        #     lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        #     time.sleep(3)
        #     if lastCount==lenOfPage:
        #         match=True   
        
        items = TutorialItem()
        all_div_quotes = self.driver.find_elements_by_css_selector('._ikh')

        for quotes in all_div_quotes:
            webLink = quotes.find_element_by_css_selector('a').get_attribute('href')
            request = scrapy.Request(webLink, callback=self.parseLink)
            request.meta['items'] = items
            yield request
            
        self.driver.quit()


