# People Search Aggregator

## Sections
- [Description](#description)
- [Objective](#objective)
- [Setup](#setup)
- [Debug](#debug)
- [Workflow](#workflow)
- [Search Parameters](#search-parameters)
- [Scoring Metrics](#scoring-metrics)
- [User Interface](#user-interface)
- [Performance](#performance)
- [Limitations](#limitations)
- [Future Improvements](#future-improvements)

---
## Description

With the number of social media users steadily increasing every year, it was reported that there are currently over 3 billion active social media users around the world. With only the name of a long-lost friend or person, it is difficult to search across various social media platforms for his/her presences and identify their various social media accounts. A name search will yield too many results. 

Furthermore, many people do not indicate their real name or upload their photo as their profile picture on their social media thus making this an even more challenging task. Combing through all the search results to identify the person will take tremendous amount of time and effort.

----

## Objective

Leveraging on big data technology and artificial intelligences, the objective of this project is to develop a people search aggregator software system that simultaneously search social media profiles from various platforms, processes and then rank the search results in an intelligent manner such that it is easier for a user to identify the person he/she was looking for. 

---
## Setup

### **Tools Used** 

- Vue.js **(Front End)**
- Spring Java **(Back End)** 
- Elasticsearch **(Database)**
- Scrapy & Selenium **(Web Scraper)**
- Apache Kafka **(Apache Kafka)**

### **Installation**

**1. Install Anaconda 3**

> Open Anaconda Prompt. Navigate to the directory which Scraper folder is installed. Type "Python ScrapyScript.py" to run the webscraper

For example, 
```
//For example

(base) C:\Users\CChunChe\elastictest>Python ScrapyScript.py
```

**2. Install [Apache Kafka 2.11 - 2.2.0 tgz (scala)]**(https://kafka.apache.org/downloads)
>Follow instruction on the website on  how to extract out the folder

>Place start_zookeeper.bat and start_kafka.bat in the same directory as the folder "kafka_2.12-2.2.0"

> edit the start_zookeeper.bat and start_kafka.bat 
```
//edit the directory if need to
pushd kafka_2.12-2.2.0\bin\windows
```

> Run start_zookeeper.bat first then start_kafka.bat

**3. Install Node.js**

> Node.js is required to deploy Vue.js 

> Open the terminal inside the Vue.js folder and type **"npm run dev"**. This will run the programme

```
//For example

C:\Users\CChunChe\Documents\PeopleSearch\SpringBootVueJs\vue-cli> npm run dev

// This message will apper after some processing

 DONE  Compiled successfully in 32011ms          
 Your application is running here: http://localhost:8080
```
**4. Install Elasticsearch**

>Extract out "elasticsearch-7.0.1-windows-x86_64.zip"

>Navigate to bin/elasticsearch.bat and run elasticsearch 

### **Running the programme**

1. Run zookeeper, kafka and elasticsearch through their bat. files
2. Run Python application. 
3. Run Java application 
3. Run Vue.js file through "npm run dev" 
---
## Debug

### **1. Unable to create logfile in Kafka**

> Delete the "tmp" folder in local disk (C:). Restart Kafka and Zookeeper

### **2. Port local host: 8090 still in use**

This problem will occur if Java program is re-run without terminating it. This results in Tomcat being re-deploy without closing it. 

> Re-run the java program and press the "terminate" button. 

### **3. Python application got stuck halfway while scraping**

This is due to Kafka consumer expiring early. 

> Close anaconda prompt. Re-run the scraper.

### **4. Web Scraper got blocked while trying to scrape**

This is due to repeated scraping of data. Currently, only Facebook has this issue. 

> Wait for around 20 minutes before scraping again.
---

## Workflow

The workflow is as shown in the picture and follows the sequence 

![Workflow](\README_Image\workflow.JPG)

1. Vue.js will send a search request to Java using Axios 
2. Java will then send a JSON message containing the search parameters to the python webscraping application using Kafka
3. Python webscraping application (Scrapy and Selnium) will scrape for data after receiving the message and store the data in Elasticsearch 
4. Python webscraping application will send a message "done" back to Java
6. Upon receiving the message, Java will retrieve the crawled data from Elasticsearch.
7. Java will send the data back as response to Vue.js

---
## Search Parameters 

### **Name Matching** 

  Nname matching tool used is [**fuzzywuzzy 0.17.0**](https://pypi.org/project/fuzzywuzzy/), a python library. It uses *Levenshtein Distance* to calculate the differences between sequences. It has 4 different algorithms for name matching. 

1. Simple Ratio
```python
>>> fuzz.ratio("this is a test", "this is a test!")
    97
``` 
2. Partial Ratio
```python
>>> fuzz.partial_ratio("this is a test", "this is a test!")
    100
```

3. Token Sort Ratio
```python
>>> fuzz.ratio("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear")
    91
>>> fuzz.token_sort_ratio("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear")
    100
```
4. Token Set Ratio
```python
>>> fuzz.token_sort_ratio("fuzzy was a bear", "fuzzy fuzzy was a bear")
    84
>>> fuzz.token_set_ratio("fuzzy was a bear", "fuzzy fuzzy was a bear")
    100
```

> Name matching is done using the **WRatio** (weighted ratio) algorithm. **WRatio** uses the runs the 4 algorithms and returns the aggregate score.

```python
>>> fuzz.WRatio('geeks for geeks', 'Geeks For Geeks')
    100
>>> fuzz.WRatio('geeks for geeks!!!','geeks for geeks') 
    100
# whereas simple ratio will give for above case 
fuzz.ratio('geeks for geeks!!!','geeks for geeks') 
91
```



### **Location Matching**

Location matching is done by **calculating the Haversine distance** beween 2 points. 

> Haversine distance refers to the **shortest spherical distance** between two points (x and y) on the Earth based on using their latitudes and longitudes measured along the surface.

The following function below will return the shortest distance as the higher score.
``` python
def calcHaversineDistanceScore(haversineDistance):
    score = round(((HALF_EARTH_CIRCUMFERENCE - haversineDistance) / HALF_EARTH_CIRCUMFERENCE), 2)
    return score
```



### **Keywords Matching**
Keyword mathing is done by **matching the number of times** the keyword is found in the string. 

> For Twitter, the description in the social media profile is matched against the specified keywords

> For Facebook, the work and education in the social media profile is matched against the specified keywords

### **Image Recognition**
Image recognition is done using the [**Facial Recognition api for Python by ageitgey**](https://github.com/ageitgey/face_recognition). It calculates the face distance between the face encodings of the reference image and the specified image. A lower face distance will return a higher score.

``` python
MAX_IMG_SCORE = 1.0

def calcImgScore(ref_encoding, imgAddr):
    unknown_encodings = []
    response = requests.get(imgAddr)
    unknown_picture = face_recognition.load_image_file(BytesIO(response.content))
    face_locations = face_recognition.face_locations(unknown_picture)
    if(face_locations == []):
        return 0
    print(face_locations)
    unknown_face_encoding = face_recognition.face_encodings(unknown_picture)[0]
    unknown_encodings = [
        unknown_face_encoding
    ]
    face_distance = face_recognition.face_distance(unknown_encodings, ref_encoding)
    #so that the higher the better
    score = MAX_IMG_SCORE - face_distance[0]
    return score

```

---
## Scoring Metrics 

The scoring of the system varies as shown in the table below. The metric is determined through trial and error. 
>**Location is given a low weightage as not all social media platform has that field.**

| Search Field | Marks |
| :-----------: | :-----------: |
| Name | 30 |
| Location | 5 |
| Image | 65 |
| Keyword | 2/word |
| **Total**| 100 + (2/word) |

---
## User Interface

### **Home Page**
![Search Field](\README_Image\UI_home_page.JPG)
By clicking on the search button at the top right hand corner, it will pop up a search field.

### **Search Field**
![Search Field](\README_Image\UI_searchfield.JPG)
The search field has 6 parameters to fill up and choose to generate the search request. To obtain an accurate result, the information provided have to be as accurate and detailed as possible.

>The number of pages selected to scrape will affect the speed of the search request. 

### **Search Results**

The respective social media platforms will provide different information of the users. The application will store up to **5 recent search results**. The desired search results can be selected via the options below the navigation bar.

![Twitter](\README_Image\UI_searchresults_twitter.JPG)

>The search results for Twitter will **show the location, date joined, aggregate score and the description of the profile**.

![Facebook](\README_Image\UI_searchresults_facebook.JPG)

>The search results for Facebook will **show the location, aggregate score, education and work information of the profile**.
---
## Performance 

### **Speed of Search**
The performance of the system varies as shown in the table below. **Most of the overhead time required is due to the need to time sleep to allow Selenium to scroll through the pages**.

| Number of pages search | Time required |
| :-----------: | :-----------: |
| Twitter: 1 (avg 28 entries)| 1 min 15 sec |
| Twitter: 2 (avg 40 entries)| 1 min 30 sec |
| Twitter: Max (avg 60 entries)| 2 min |
| Facebook: 1 (avg 27 entries)| 1 min 15 sec |
| Facebook: 2 (avg 35 entries)| 1 min 30 sec |
| Facebook: Max (500 entries)| 26 min |
| Combine: 1 (avg 55 entries)| 3 mins |

---

## Limitations

### **Time Required For Search**

The time required for data scraping is dependent on the number of pages user chooses to scrape.

There are 2 main factors which account for majority of the time required.

1. The automated web page scrolling by Selenium requires time.sleep in between each scrolling to allow the data to be scraped. By reducing the time.sleep, it will result in the web scraper ends prematurely

2. The main bulk of the analysis of the data was done in a single python module. Due to time limitation, a distributed system which the analysis of data is distributed to different modules and analysed asynchronously was not able to be achieved. Instead the analysis of each data was done synchronously which resulted in huge amount of time required. 

### **Limited Social Media Platforms**

Currently, there are only 2 social media platforms, Twitter and Facebook that the application can scrape from. The data to be scraped from the web pages was done using CSS selector. Due to time limitation, data was not able to be scraped from more social media platforms as each platforms have different information and orgranisation of data. Further analysis of data by comparing the data retrieved from the different social media platforms was not able to be achieved. 

### **High Dependency On Social Media's Search Engine**

Since the data retrieved was done by scraping the search results of the social media's search engine, the performance of the application depends highly on the social media's search engine. 

- #### Privacy setting of users 

The visibility of the profile to the public social media's search engine depends on whether the user has enabled his profile to be searched through the public search engine. As shown in the pictures below, Chong Yew Heng can only be found after he has disabled the privacy setting.

![Privacy](\README_Image\dillon_not_found.JPG)

Chong Yew Heng's Facebook profile is not found in the Facebook public search engine.

![Privacy](\README_Image\dillon_privacy_setting.JPG)

The is due to the privacy setting of Chong Yew Heng's Facebook profile has been set to prevent him from being search outside of Facebook search engine.

![Privacy](\README_Image\dillon_found.JPG)

By enabling access to the profile outside of Facebook search engine, Chong Yew Heng's profile can now be found.

- #### Relevance of search results 

The results returned by the search engine is highly dependent on the relevance or popularity of the social media profile. In addition, there is a maximum number of results that can be returned. Therefore, there might be a chance that a low profile social media user might not be found.

- #### Accuracy of information  

To obtain the correct social media profile out of the long list of users with the same name, a close to full name given to be searched is essential. As seen from the pictures below, without surname of the person given in the search, there is a high chance that the person might not be returned by the social media's search engine.

![Privacy](\README_Image\chun_cheong_not_found.JPG)
![Privacy](\README_Image\chun_cheong_found_2.JPG)
![Privacy](\README_Image\chun_cheong_found.JPG)


### **Possibility Of Being Block From Social Media**

Repeated searches on social media without pauses in between will result in being block from scraping by Social Media. Although this is not seen in Twitter, Facebook will block user from scraping for a short duration when it has detected automated web scraper. 

---
## Future Improvements

### **Further In-depth Crawling & Analysis**
Currently, the data scraped is only limited to the social media profile information that is visible to the public. In the future, the posts or tweets of the person can be scraped and analyse for their online activity. The photo albums of the social media profile are often blocked by the social media from scraping. Therefore, if there is a way to by pass that, then more photos can be checked against and the profile can be identified with better accuracy

### **Distributed System**

The workload of analysing different parts of the data can be distributed into different modules. This can be done through using Apache Kafka as the message broker. This way the analysis of data can be done asynchronously which will greatly increase the speed of analysing the data, thus reducing the time required to obtain the search results. 

### **Integration of Search Results**

As the application is expanded to more social media platforms, the search results obtained from the respective platforms can be compared and analysed to generate further data on similar profiles. This will help to further filter the search results for social media users who use several social media platforms. In addition, these data obtained can be integrated into one platform which can view all the data at once. This will provide a more accurate analysis of the users' social media online activity. 
