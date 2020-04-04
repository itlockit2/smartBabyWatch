import csv
import os

TRAIN_AUDIOSET_PATH = os.path.join(os.getcwd(), "trainAudioSet")
GOOGLE_AUDIOSET_PATH = os.path.join(os.getcwd(), "googleAudioSet")
googleAudiosetList = os.listdir(GOOGLE_AUDIOSET_PATH)
print("TRAIN_AUDIOSET_PATH : ", TRAIN_AUDIOSET_PATH)
print("GOOGLE_AUDIOSET_PATH : ", GOOGLE_AUDIOSET_PATH)
print("googleAudiosetList : ", googleAudiosetList)
BabyCryCode = "/t/dd00002"

for googleAudioset in googleAudiosetList:
    googleAudiosetPath = os.path.join(GOOGLE_AUDIOSET_PATH, googleAudioset)
    print("googleAudiosetPath : ", googleAudiosetPath)
    with open(googleAudiosetPath, 'r', encoding='utf-8') as f:
        rdr = csv.reader(f)
        writeLine = []
        count = 0
        for line in rdr:
            afterTrim = []
            for value in line:
                # 공백제거
                newValue = value.replace(" ", "")
                # 큰따옴표 제거
                newValue = newValue.replace('"', "")
                afterTrim.append(newValue)
            try:
                if afterTrim.index(BabyCryCode):
                    writeLine.append(afterTrim)
                    count = count + 1
            except ValueError:
                pass
        print(count)
