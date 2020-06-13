import youtube_dl
import os
import sys
import csv

TRAIN_AUDIOSET_PATH = os.path.join(os.getcwd(), "trainAudioSet")
YOUTUBE_SAVE_PATH = os.path.join(os.getcwd(), "youtubeOriginal")
trainAudioList = os.listdir(TRAIN_AUDIOSET_PATH)

dataSetName = {"/t/dd00002": "babyCry", "/m/0bt9lr": "dog",
               "/m/028v0c": "silence", "/m/01yrx": "cat",
               "/m/0ytgt": "childSpeech", "/m/0261r1": "babbling"}


def download_video_and_subtitle(output_dir, youtube_video_link):

    download_path = os.path.join(output_dir, '%(id)s.%(ext)s')
    ydl_opts = {
        'format': 'bestaudio',  # 가장 좋은 화질로 선택(화질을 선택하여 다운로드 가능)
        'outtmpl': download_path,  # 다운로드 경로 설정
    }

    try:
        with youtube_dl.YoutubeDL(ydl_opts) as ydl:
            ydl.download([youtube_video_link])
    except Exception as e:
        print('error', e)


downloadList = {}
youtubeFolderList = os.listdir(YOUTUBE_SAVE_PATH)
for youtubeFolder in youtubeFolderList:
    if youtubeFolder == '.DS_Store':
        continue
    youtubeFolderPath = os.path.join(YOUTUBE_SAVE_PATH, youtubeFolder)
    youtubeFileList = os.listdir(youtubeFolderPath)
    fileObject = []
    for youtubeFile in youtubeFileList:
        fileObject.append(youtubeFile.split('.')[0])
    downloadList[youtubeFolder] = fileObject


for trainCsv in trainAudioList:
    categoryName = trainCsv.split('.')[0]
    if trainCsv.split('.')[1] != 'csv' or categoryName != "babbling":
        continue
    outputPath = os.path.join(YOUTUBE_SAVE_PATH, categoryName)
    print(outputPath)
    if not os.path.isdir(outputPath):
        os.mkdir(outputPath)
    trainCsvPath = os.path.join(TRAIN_AUDIOSET_PATH, trainCsv)
    print(trainCsvPath)
    with open(trainCsvPath, 'r', encoding='utf-8') as f:
        rdr = csv.reader(f)
        for line in rdr:
            print(line[0])
            try:
                if downloadList[categoryName].index(line[0]):
                    print(line[0], ' already Download')
                    pass
            except ValueError:
                youtubeLink = "https://www.youtube.com/watch?v=" + line[0]
                download_video_and_subtitle(outputPath, youtubeLink)
