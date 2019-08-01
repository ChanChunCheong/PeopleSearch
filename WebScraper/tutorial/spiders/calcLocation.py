from geotext import GeoText
from haversine import haversine, Unit
import math
import re

HALF_EARTH_CIRCUMFERENCE = 20037.0

def calcLocScore(refLat, refLong, target, countriesList, citiesList): 
    targetPlaces = GeoText(target)
    targetLat = 0
    targetLong = 0
    scoreList = []
    if  target and target.strip():
        #location which uses short forms for names
        if (len(targetPlaces.countries) == 0 and len(targetPlaces.cities) == 0):
            words = [x.strip() for x in re.split(',|/| ', target.upper())]
            print (words)
            for data in countriesList:
                #some data do not have the 3 letter short form
                if(len(data) < 5 ):
                    if(data[0] in words):
                        targetLat = float(data[1])
                        targetLong = float(data[2])
                        score = locScore(targetLat, targetLong, refLat, refLong)
                        scoreList.append(score)
                else:
                    if(data[0] in words or data[4] in words):
                        targetLat = float(data[1])
                        targetLong = float(data[2])
                        score = locScore(targetLat, targetLong, refLat, refLong)
                        scoreList.append(score)
            #if theres no match 
            if (len(scoreList) == 0):
                scoreList.append(0)
        else:
            for country in targetPlaces.countries:
                for data in countriesList:
                    if data[3] == country:
                        targetLat = float(data[1])
                        targetLong = float(data[2])
                        score = locScore(targetLat, targetLong, refLat, refLong)
                        break
                    else:
                        score = 0
                scoreList.append(score)
            for city in targetPlaces.cities:
                for data in citiesList:
                    #capital
                    if data[0] == city or data[7] == city:
                        targetLat = float(data[2])
                        targetLong = float(data[3])
                        score = locScore(targetLat, targetLong, refLat, refLong)
                        break
                    else:
                        score = 0
                scoreList.append(score)
        #choose the highest score out of all the scanned countries and cities
        highestScore = max(scoreList)
    else:
        #no location
        highestScore = 0
    return highestScore 

#insert scores for calculation distance here 
def locScore(targetLat, targetLong, refLat, refLong):
    score = haversineDistanceScore(targetLat, targetLong, refLat, refLong)
    return score

def haversineDistanceScore(lat1Deg, lon1Deg, lat2Deg, lon2Deg):
    point1 = (lat1Deg, lon1Deg)
    point2 = (lat2Deg, lon2Deg)
    haversineDistance = haversine(point1, point2)
    score = calcHaversineDistanceScore(haversineDistance)
    return score

def calcHaversineDistanceScore(haversineDistance):
    score = round(((HALF_EARTH_CIRCUMFERENCE - haversineDistance) / HALF_EARTH_CIRCUMFERENCE), 2)
    return score