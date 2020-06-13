import subprocess
import csv
import os

TRAIN_AUDIOSET_PATH = os.path.join(os.getcwd(), "trainAudioSet")
YOUTUBE_AUDIOSET_PATH = os.path.join(os.getcwd(), "youtubeOriginal")
TRIM_AUDIOSET_PATH = os.path.join(os.getcwd(), "trimAudioSet")


def trimAudioFile(inputFilePath, outputFilePath, start, end):
    # ffmpeg -ss 10 -to 16 -i input.mp3 output.mp3
    command = "ffmpeg -ss " + start + " -to " + end + \
        " -i " + inputFilePath + " " + outputFilePath
    popen = subprocess.Popen(command, stdout=subprocess.PIPE,
                             stderr=subprocess.PIPE, shell=True)
    (stdoutdata, stderrdata) = popen.communicate()


trainAudiosetList = os.listdir(TRAIN_AUDIOSET_PATH)
csvObject = {}
for trainCsv in trainAudiosetList:
    if trainCsv.split('.')[1] != 'csv' or trainCsv.split('.')[0] == 'babyCry' or trainCsv.split('.')[0] == 'silence' or trainCsv.split('.')[0] == 'cat':
        continue
    trainCsvName = trainCsv.split('.')[0]
    trainObject = {}
    trainCsvPath = os.path.join(TRAIN_AUDIOSET_PATH, trainCsv)
    print(trainCsvPath)
    with open(trainCsvPath, 'r', encoding='utf-8') as f:
        rdr = csv.reader(f)
        for line in rdr:
            videoID = line[0]
            start = line[1]
            end = line[2]
            trainObject[videoID] = [start, end]
    csvObject[trainCsvName] = trainObject

youtubeAudiosetList = os.listdir(YOUTUBE_AUDIOSET_PATH)
for audioClass in youtubeAudiosetList:
    if audioClass == '.DS_Store':
        continue
    audioClassPath = os.path.join(YOUTUBE_AUDIOSET_PATH, audioClass)
    audioFileList = os.listdir(audioClassPath)
    for audioFile in audioFileList:
        if audioFile == '.DS_Store':
            continue
        print(audioFile)
        inputAudioFilePath = os.path.join(audioClassPath, audioFile)
        audioFileId = audioFile.split('.')[0]
        outputAudioFileName = audioFileId + ".wav"
        outputAudioFilePath = os.path.join(
            TRIM_AUDIOSET_PATH, audioClass, outputAudioFileName)
        start = csvObject[audioClass][audioFileId][0]
        end = csvObject[audioClass][audioFileId][1]
        if(os.path.isfile(outputAudioFilePath)):
            print('already process')
            continue
        trimAudioFile(inputAudioFilePath, outputAudioFilePath, start, end)
