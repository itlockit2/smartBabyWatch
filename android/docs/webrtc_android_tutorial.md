# WebRTC Android Tutorial

https://roqkfwk.tistory.com/40?category=785055

### 1. 소스 다운


구글 git에서 제공하는 apprtc 샘플 사이트를 방문한다.

https://webrtc.googlesource.com/src/+/master/examples/androidapp/

[[tgz](https://webrtc.googlesource.com/src/+archive/master/examples/androidapp.tar.gz)]을 클릭하여 최신 예제를 다운로드 받는다.

### 2. Android project Gradle에 의존성 추가 및 수정

```java
implementation 'org.webrtc:google-webrtc:1.0.+'
```

```java
compileOptions {
    targetCompatibility 1.8
    sourceCompatibility 1.8
}
```

1 에서 받은 파일의 tar 파일 압축 해제 후, 

Third_party -> autobanh -> lib -> autobanh.jar 파일을 프로젝트의 app/libs 폴더에 삽입

(Autobanh 은 WebSocket 을 위한 라이브러리)

```
implementation files('libs/autobanh.jar')
```

### 3. 압축 푼 리소스 파일의 붙여넣기 및 수정

res 폴더 안의 파일 옮기기

1. Drawable-xhdpi 폴더 안의 png 파일 프로젝트의 drawable 폴더로 옮기기
2. .layout의 xml을 모두 프로젝트의 layout 폴더로 옮기기
3. .menu 폴더 프로젝트에 복사
4. .value 폴더 프로젝트에 복사 (override)
5. 프로젝트의 style.xml 에 다음을 추가

```java
<style name="CallActivityTheme" parent="android:Theme.Black">
    <item name="android:windowActionBar">false</item>
    <item name="android:windowFullscreen">true</item>
    <item name="android:windowNoTitle">true</item>
</style>
```

6. .xml 을 모두 복사

### 4. 매니페스트 파일 수정

user feature 및 user permission 추가

```java
<uses-feature android:name="android.hardware.camera"/>
<uses-feature android:name="android.hardware.camera.autofocus"/>
<uses-feature android:glEsVersion="0x00020000" android:required="true"/>
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.CAPTURE_VIDEO_OUTPUT"
    tools:ignore="ProtectedPermissions" />
```

-> CAPTURE_VIDEO_OUTPUT 권한 변경??

```java
<application
    android:allowBackup="false"
    android:debuggable="true"
    android:icon="@drawable/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="false"
    tools:ignore="HardcodedDebugMode">
    <activity
        android:name="ConnectActivity"
        android:label="@string/app_name"
        android:windowSoftInputMode="adjustPan">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data
                android:host="appr.tc"
                android:scheme="https" />
            <data
                android:host="appr.tc"
                android:scheme="http" />
        </intent-filter>
    </activity>
    <activity
        android:name="SettingsActivity"
        android:label="@string/settings_name"></activity>
    <activity
        android:name="CallActivity"
        android:configChanges="orientation|smallestScreenSize|screenSize|screenLayout"
        android:label="@string/app_name"
        android:screenOrientation="fullUser"
        android:theme="@style/CallActivityTheme"></activity>
</application>
```

### 5. 소스코드 붙여넣기 및 수정C