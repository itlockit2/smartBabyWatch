import os
import subprocess

TRIM_AUDIOSET_PATH = os.path.join(os.getcwd(), "trimAudioSet")
SPLIT_TRIM_AUDIOSET_PATH = os.path.join(os.getcwd(), "splitTrimAudioSet")

audioLength = [[1, 4], [4, 7], [7, 10]]


def splitAudioFile(inputFilePath, outputFilePath, fileName):
    for count in range(3):
        resultPath = os.path.join(
            outputFilePath, fileName + "_" + str(count) + ".wav")
        start, end = audioLength[count]
        command = "ffmpeg -ss " + str(start) + " -to " + str(end) + \
            " -i " + inputFilePath + " " + resultPath
        popen = subprocess.Popen(command, stdout=subprocess.PIPE,
                                 stderr=subprocess.PIPE, shell=True)
        (stdoutdata, stderrdata) = popen.communicate()


trimAudiosetList = os.listdir(TRIM_AUDIOSET_PATH)
for trimAudioClass in trimAudiosetList:
    if(trimAudioClass == ".DS_Store"):
        continue
    print(trimAudioClass)
    trimAudioClassPath = os.path.join(TRIM_AUDIOSET_PATH, trimAudioClass)
    splitAudioClassPath = os.path.join(
        SPLIT_TRIM_AUDIOSET_PATH, trimAudioClass)
    print("trimAudioClassPath : ", trimAudioClassPath)
    print("splitAudioClassPath : ", splitAudioClassPath)
    if not os.path.isdir(splitAudioClassPath):
        os.mkdir(splitAudioClassPath)
    trimAudioList = os.listdir(trimAudioClassPath)
    for trimAudioData in trimAudioList:
        print(trimAudioData)
        fileName = trimAudioData.split('.')[0]
        inputFilePath = os.path.join(trimAudioClassPath, trimAudioData)
        print("fileName : ", fileName)
        print("inputFilePath : ", inputFilePath)
        splitAudioFile(inputFilePath, splitAudioClassPath, fileName)
