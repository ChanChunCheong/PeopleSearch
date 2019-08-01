import csv

def getRefCities(src): 
    citiesList = []
    with open(src, 'r', encoding='utf-8') as csvFile:
        reader = csv.reader(csvFile)
        for row in reader:
            citiesList.append(row)
    csvFile.close()
    return citiesList

def getRefCountries(src): 
    countriesList = []
    with open(src, 'r') as csvFile:
        reader = csv.reader(csvFile)
        for row in reader:
            countriesList.append(row)
    csvFile.close()
    return countriesList

def getRefLatLong(countriesList, refCountry):
    refLatLong = []
    for data in countriesList:
        if data[3] == refCountry:
            refLat = float(data[1])
            refLong = float(data[2])
            refLatLong.append(refLat) 
            refLatLong.append(refLong)
            break
    print("reference")
    print(refLatLong)
    return refLatLong