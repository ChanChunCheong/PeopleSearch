import face_recognition
from PIL import Image
import requests
from io import BytesIO 

MAX_IMG_SCORE = 1.0

def calcRefImg(src):
    picture_of_me = face_recognition.load_image_file(src)
    return face_recognition.face_encodings(picture_of_me)[0]

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

def calcImgScore_Highest(ref_encoding, imgAddr, imgList):
    scoreList = []
    for img in imgList: 
        scoreList.append(calcImgScore(ref_encoding, img))
    scoreList.append(calcImgScore(ref_encoding, imgAddr))   
    print('checkhere')
    print(scoreList)
    highestScore = max(scoreList)
    return highestScore