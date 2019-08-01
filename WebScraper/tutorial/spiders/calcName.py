from fuzzywuzzy import fuzz
from fuzzywuzzy import process

def calcNameScore(refName, name): 
    score = round((fuzz.WRatio(refName, name, force_ascii=True, full_process=True) / 100.0), 2)
    return score