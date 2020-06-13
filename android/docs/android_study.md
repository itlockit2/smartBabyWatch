## Intent-filter

- 암시적 인텐트 수신을 위해서는 앱의 컴포넌트에 대해 하나 이상의 <intent-filter> 를 메니페스트에 선언해야함
- 종류
  - <action>
    - name 속성에서 허용된 인텐트 작업 선언
    - 문자열
  - <data>
    - 허용된 데이터 유형 선언
    - 하나 이상의 속성을 사용하여 데이터의 URI 와 MIME 유형을 나타냄
  - <category>
    - name 속성에서 허용된 인텐트 카테고리 선언
    - 문자열

------

## 앱의 자동 데이터 백업을 막는 방법

1. AndroidManifest.xml 파일 open

2. application 요소에 android:allowBackup 추가 후 값 false 로 설정

   ```xml
   android:allowBackup="false"
   ```

3. application 요소에 android:fullBackupContent 요소 추가 후 값 false 로 설정

   ```xml
   android:fullBackupConent="false"
   ```

4. (옵션) 프로젝트에 다른 플러그인이 있는 경우, allowBackup 속성과 충돌 가능성 존재,
   이 경우 application 요소에 tools:replace 속성 추가 후, 값을 allowBackup 으로 설정

   ```xml
   android:allowBackup="false"
   tools:replace="allowBackup"
   anroid:fullBackupContent="false"
   ```

------

## 키보드 Window soft input mode 설정

1. AndoridManifest.xml 파일 Open
2. 키보드가 실행될 Actiivty 요소에 android:windowSoftInputMode 속성 추가

- 옵션
  - **설정 x** : adjustUnspecifed 와 stateUnspecified 가 적용 됨
  - **adjustPan** : 키보드가 올라오면 EditText 에 맞춰 화면 UI 제거 (위 아리로 잘림)
  - **adjustResize** 키보드가 올라와도 EditText 와 UI 가 화면에 보이도록 Acitivity 를 resize
  - **adjustUnspecified** : 시스템이 알아서 상황에 맞는 옵션 설정. (default)
  - **stateHidden** : Activity 실행 시 키보드가 자동으로 올라오는 것을 방지
  - **stateAlwaysHidden** : Activity 실행 시 항상 키보드가 자동으로 올라오는 것을 방지 (액티비티 이동 포함)
  - **stateVisible** : Activity 실행 시 키보드가 자동으로 올라 옴 (EditText 에 포커스)
  - **stateAlwaysVisible** : Activity 실행 시 항상 키보드가 자동으로 올라 옴 (EditText 에 포커스, 액티비티 이동 포함)
  - **stateUnchanged** : 키보드를 마지막 설정 상태로 유지
  -  **stateUnspecified** : 키보드의 상태가 설정 안 된 상태. 시스템이 적절한 키보드 상태를 설정해 주거나 테마에 따라 설정 (default)

------

## 화면 회전 시 view 유지

- 화면 회전 시 기본적으로 새로운 layout 으로 재시작 됨
  - onDestroy() -> onCreate()
- AndroidManifest.xml 에 android:configChanges 설정
  - onDestory(), onCreate() 대신 onConfigrationChnaged() 호출

1. AndroidManifest.xml 파일 Open
2. 설정하고자하는 Aciviity 요소에 android:configChanges 속성 추가

- 옵션
  - **mcc** : SIM 이 Detect 되고 MCC 가 Update 될 경우
  - **mnc** : SIM 이 Detect 되고 MNC 가 Update 될 경우
  - **local** : User 가 새로운 Language 를 선택했을 때 (locale 이 변경될 때)
  - **touchscreen** : Touch Screen Hardware 가 바뀌었을 때 (보통 일어나지 않음) 
  - **keyboard** : User가 External Keyboard 를 꽂았을 때를 비롯한 Keybord Type 변경 시
  - **keyboardHidden** : User 가 Hardware Keyboard를 보이고 감추는 등의 Keyboard 의 Accessibility 가 변경되었을 때
  - **navigaion** : Navigaion Type (트랙볼 / DPad) 가 변경되었을 때 (보통 일어나지 않음)
  - **orientation **: User 가 Device 를 돌리는 등의 행위로 Screen 의 Orientation 이 변경되었으 ㄹ경우
  - **screenLayout** : Screen의 Layout 이 변했을 때, 다른 Display 가 Activate 되었을 경우 Layout 이 변한 경우
  - **fontScale** : User 가 새로운 Font Size 를 선택했을 때
  - **uiMode** : User 가 Device 를 Desk 나 Car Dock 등에 비치하여 Interface Mode 를 바꾸었을 때

------

## Activity 화면 방향 설정

1. AndroidManifest.xml 파일 Opne
2. 원하는 Activity 요소에 android:screenOrientatin 속성 설정

- 옵션
  - **unspeified** : 시스템에서 방향 결정. 선택된 디바이스마다 방향 달라질 수 있음 (default)
  - **behind** : 액티비티 스택에서 이전 액티비티 방향과 같은 방향 표시
  - **landscape** : 가로 방향으로 고정
  - **portrait** : 세로 방향으로 고정
  - **reverseLandscape** : 가로 방향 고정, 일반적인 방향에서 역뱡향으로 표시
  - **reversePortrait **: 세로 방향 고정, 일반적인 방향에서 역방향으로 표시
  - **sensorLandscape** : 가로 방향 고정, 센서에 따라 정/역방향으로 표시
  - **sensorPortrait** : 세로 방향 고정, 센서에 따라 정/역방향으로 표시
  - **userLandscape** : 가로 방향 고정, 센서 장치 및 사용자 센서 기본설정에 따라 정/역방향으로 표시 
  - **userPortrait** : 세로 방향 고정, 센서 장치 및 사용자 센서 기본설정에 따라 정/역방향으로 표시 
  - **sensor** : 기기 회전 센서에 의해 방향 결정
  - **fullSensor** : 4 방향 중 하나의 기기 방향 센서에 의해 결정
  - **nosensor** : 물리적인 센서를 참조하지 않고 방향 결정. unspecified 와 동일한 규정 사용
  - **user** : 사용자가 현재 선호하는 방향 표시
  - **fullUser** : 센서 기반 회전 기능이 잠긴 경우 user 와 동일, 그렇지 않을 경우 fullSensor 와 동일
  - **locked** : 무엇이든 현재의 회전으로 방향을 잠금

------

## SHA-1 키 

```cmd
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

