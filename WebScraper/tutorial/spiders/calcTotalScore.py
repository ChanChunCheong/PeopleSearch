def calcTotalScore(nameScore, imgScore, locScore, wordScore):
    totalScore = nameScore*30.0 + imgScore*65.0 + locScore*5.0 + wordScore*1.0 
    score = round(totalScore, 2)
    return score 
