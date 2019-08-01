import scrapy
import time
import json
from selenium import webdriver
from ..items import TutorialItem
from kafka import KafkaConsumer
from kafka import TopicPartition
from urllib.parse import urlencode
from urllib.parse import unquote
from .calcImage import calcImgScore, calcRefImg, calcImgScore_Highest
from .calcLocation import calcLocScore
from .calcName import calcNameScore
from .calcKeyword import calcKeywordScore
from .calcTotalScore import calcTotalScore
from .refCountryandCities import getRefCities, getRefCountries, getRefLatLong
from io import BytesIO 
import base64

class QuotesSpider(scrapy.Spider):
    name = "quotes2"

    def __init__(self, refImage_bytes, refName, refCountry, refWord, sessionID, numPage):
        params = { 
            "f": "users",
            "vertical": "news",
            "q": refName,
            "src": "typd"
        }
        self.start_urls = ["https://twitter.com/search?" + urlencode(params)]
        self.my_face_encoding = calcRefImg(BytesIO(refImage_bytes))
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
        name = response.css('.u-textInheritColor::text').get()
        link = response.css('.u-linkComplex-target::text').get()
        baselink = unquote(response.request.url)
        imgSrc = response.css('img.ProfileAvatar-image::attr(src)').get()
        location = response.css('.ProfileHeaderCard-locationText::text').get()
        dateJoined = response.css('.js-tooltip.u-dir::text').get()
        description = response.css('.ProfileHeaderCard-bio::text').get()

        nameScore = calcNameScore(self.refName, name)
        imgScore = calcImgScore(self.my_face_encoding, imgSrc)
        locScore = calcLocScore(self.refLatLong[0], self.refLatLong[1], location, self.countriesList, self.citiesList)
        wordScore = calcKeywordScore(description, self.refWord)

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
        items['platform'] = 'Twitter'
        items['lastUsed'] = "yes"
        items['sessionID'] = self.sessionID
        items['totalScore'] = calcTotalScore(nameScore, imgScore, locScore, wordScore)
        items['work'] = ''
        items['education'] = ''

        yield items

    def parse(self, response):
        self.driver.get(response.url)
 # Let the user actually see something!
        count = 0
        lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        count += 1 
        time.sleep(5)
        while(True):
            if(count >= self.page_count):  
                break 
            lastCount = lenOfPage
            lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
            count += 1
            time.sleep(3)
            if(lastCount == lenOfPage):  
                break 
        # lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        # match = False
        # time.sleep(5)
        # while(match==False):
        #     lastCount = lenOfPage
        #     lenOfPage = self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);var lenOfPage=document.body.scrollHeight;return lenOfPage;")
        #     time.sleep(3)
        #     if lastCount==lenOfPage:
        #         match=True
        
        items = TutorialItem()
        all_div_quotes = self.driver.find_elements_by_css_selector('.js-actionable-user') 
        print(len(all_div_quotes))

        for quotes in all_div_quotes:
            webLink = quotes.find_element_by_css_selector('a').get_attribute('href')
            request = scrapy.Request(webLink, callback=self.parseLink)
            request.meta['items'] = items
            
            yield request
    
        self.driver.quit()

