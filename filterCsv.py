import csv
import os

TRAIN_AUDIOSET_PATH = os.path.join(os.getcwd(), "trainAudioSet")
GOOGLE_AUDIOSET_PATH = os.path.join(os.getcwd(), "googleAudioSet")
googleAudiosetList = os.listdir(GOOGLE_AUDIOSET_PATH)
print("TRAIN_AUDIOSET_PATH : ", TRAIN_AUDIOSET_PATH)
print("GOOGLE_AUDIOSET_PATH : ", GOOGLE_AUDIOSET_PATH)
print("googleAudiosetList : ", googleAudiosetList)

CodeArray = ["/t/dd00002", "/m/0bt9lr", "/m/028v0c",
             "/m/01yrx", "/m/0ytgt", "/m/0261r1"]
dataSetName = {"/t/dd00002": "babyCry", "/m/0bt9lr": "dog",
               "/m/028v0c": "silence", "/m/01yrx": "cat",
               "/m/0ytgt": "childSpeech", "/m/0261r1": "babbling"}

googlePreprocessingDataList = []
# 구글 데이터셋 전처리
for googleAudioset in googleAudiosetList:
    if(googleAudioset.split('.')[1] != "csv"):
        continue
    googleAudiosetPath = os.path.join(GOOGLE_AUDIOSET_PATH, googleAudioset)
    print("googleAudiosetPath : ", googleAudiosetPath)
    with open(googleAudiosetPath, 'r', encoding='utf-8') as f:
        rdr = csv.reader(f)
        count = 0
        for line in rdr:
            newLine = []
            for value in line:
                # 공백제거
                newValue = value.replace(" ", "")
                # 큰따옴표 제거
                newValue = newValue.replace('"', "")
                newLine.append(newValue)
            googlePreprocessingDataList.append(newLine)

# 목표 데이터 탐색
for code in CodeArray:
    print("code : ", code)
    writeLine = []
    for dataLine in googlePreprocessingDataList:
        try:
            if dataLine.index(code):
                writeLine.append(dataLine)
        except ValueError:
            pass
    writeCsvFileName = os.path.join(
        TRAIN_AUDIOSET_PATH, dataSetName[code] + ".csv")
    with open(writeCsvFileName, 'w+', encoding='utf-8', newline='') as f:
        wr = csv.writer(f)
        print(len(writeLine))
        for line in writeLine:
            wr.writerow(line)
