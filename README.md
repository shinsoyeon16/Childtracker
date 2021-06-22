# 👩‍👦 Child Tracker
Child Tracker는 아동의 범죄사고 방지 목적으로 사용되는 **아동추적기기 & 위치 확인 앱** 이며,

2020학년도 용인대학교 졸업작품발표회를 위해 개발된 프로젝트입니다.
<p align="center"><img src="https://user-images.githubusercontent.com/40010897/122970139-7e405f80-d3c8-11eb-935a-077ca681e49d.gif" width="350px" heigh="450">
<img src="https://user-images.githubusercontent.com/40010897/122955628-3bc45600-d3bb-11eb-9dc8-29d2b92e39f2.gif" width="300px" heigh="350"></p>

## 기획의도
스마트폰 사용 및 소지가 어려운 미취학 아동, 장애인을 위해

**미아 & 범죄 방지를 목적**으로 이 프로젝트를 기획했습니다.


## 개발 환경
<img src="https://user-images.githubusercontent.com/40010897/122971009-84830b80-d3c9-11eb-8e61-8d00b7a485a1.png" width="600px">

- 위치 추적 기기 - Arduino Sketch
- 안드로이드 앱 - Android Studio, Kakao Map API
- DBMS – MySQL
- Server - Bitnami WAMP, Sublime Text

## 개발 분담
신소연 : 웹서버, 아두이노, 앱의 로직을 담당

한서희 : 프로젝트 기획, 앱의 레이아웃 제작, 아두이노 조립 담당


## 주요 기능
### 안드로이드 앱 기능
- 회원 가입 / 로그인
- 서버로부터 불러온 GPS 데이터를 시간 순서대로 지도에 Polyline과 Marker로 표시
- 위치 추적 기기 등록 / 수정 / 삭제
- 위치 데이터 초기화

### 아두이노 기기 기능
- GPS 데이터 수신
- Wifi에 연결하여 서버 접속
- 서버에 위치 데이터 전송
- 무선 동작



## 📌 추가 자료
- [발표 PPT](https://drive.google.com/file/d/1L89ZcdV8WFMEF6BURu3PcUMDbZiz5YxG/view?usp=sharing)
- [앱 시연영상](https://youtu.be/IvRG7xDhvzM?t=48)
- [아두이노 시연영상](https://youtu.be/IvRG7xDhvzM?t=257)
- [전체순서도](https://drive.google.com/file/d/185JAaMjnB6ePfBRnIC0FWHM4KtMNDs1C/view?usp=sharing)


### 조립한 아두이노 부품
<img src="https://user-images.githubusercontent.com/40010897/122916092-79af8300-d397-11eb-8160-646ce1adf3f6.png" width="600px">

- Arduino uno & nano
- esp8266 (wifi module)
- neo-6m (gps module)
- 9v battery

### DB ERD
<img src="https://user-images.githubusercontent.com/40010897/122971908-87cac700-d3ca-11eb-9868-c980f912909a.png" width="600px">


### 앱 전체 기능
<img src="https://user-images.githubusercontent.com/40010897/122973868-a9c54900-d3cc-11eb-8323-5ef5f384f443.png" width="850px">
<img src="https://user-images.githubusercontent.com/40010897/122973858-a7fb8580-d3cc-11eb-92b6-00247bea6ec0.png" width="850px">
<img src="https://user-images.githubusercontent.com/40010897/122973852-a6ca5880-d3cc-11eb-827d-4857ee1fe972.png" width="850px">




