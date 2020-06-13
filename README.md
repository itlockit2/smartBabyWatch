# 2020 전남대학교 소프트웨어공학과 캡스톤 프로젝트

<h1 align="center">Welcome to Infant Monitoring System 👋</h1>
<p>
  <img src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
 
  <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" target="_blank" />
  
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Cc.logo.circle.svg/40px-Cc.logo.circle.svg.png"/>
  <span> License: Only free non-commercial use</span>
</p>

> Prevention of Sudden Infant Death Syndrome through Infant Monitoring. And it helps parents to be more comfortable, easy and fun to raise children

## 설치

```sh
npm install
```

## 사용

```sh
npm run start
```

## 테스트

```sh
npm run test
```

## 브라우저에서의 자세추적 : PoseNet Mode

PoseNet을 사용하여 단일 포즈 또는 다중 포즈를 추정 할 수 있습니다.

### 실제 스트리밍 미디어 분석 결과

![ poseNet ](images/poseNet.gif "poseNet")

## 어두운 상황에서의 자세추적

PoseNet can predict 17 key points in dark environments.

![ poseNet ](images/poseNetBaby.gif "poseNet")

## Pytorch를 이용한 감정분석

### 슬픈 표정 분석

![ sad ](images/cry_predict.png "sad")

### 행복한 표정 분석

![ happy ](images/happy_predict.png "happy")

## 감정인식 데이터셋

- Dataset from https://www.kaggle.com/c/challenges-in-representation-learning-facial-expression-recognition-challenge/data
  Image Properties: 48 x 48 pixels (2304 bytes)
  labels: 0=Angry, 1=Disgust, 2=Fear, 3=Happy, 4=Sad, 5=Surprise, 6=Neutral
  The training set consists of 28,709 examples. The public test set consists of 3,589 examples. The private test set consists of another 3,589 examples.

## 정확도 비교

- Model： VGG19 ; PublicTest_acc： 71.496% ; PrivateTest_acc：73.112% <Br/>
- Model： Resnet18 ; PublicTest_acc： 71.190% ; PrivateTest_acc：72.973%

## 저자

🛴 **SUNGHO, YUN**

- Github: [@itlockit2](https://github.com/itlockit2)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br/>Feel free to check (itlockit@gmail.com).

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2020 [SUNGHO, YUN](https://github.com/itlockit2) .<br/>
This project is [Only free non-commercial use](https://git.swmgit.org/root/p1004_doif/LICENSE) licensed.

---

_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
