def calcKeywordScore(refString, keywords):
    numCount = 0
    if (refString == None):
        return 0
    else: 
        for keyword in keywords:
            print("keyword is" + keyword)
            numCount += refString.lower().count(keyword.lower())
        score = numCount * 2.0
        return score