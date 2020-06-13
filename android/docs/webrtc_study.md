# WebRTC (Web Real-Time Communication)

## 정의

- 웹 브라우저 간에 플러그인 도움 없이 상호 통신할 수 있도록 설계된 JavaScript API
- 음성 통화, 영상 통화, p2p 파일 공유 등으로 활용 가능

## 특징

- No Plugin
- No Install
- Bye NPAPI, 
- Any OS
- Simple Connection

## 주요 API

### GetUserMedia

사용자의 카메라와 마이크 접근을 담당

- 사용자 단말기의 미디어 장치를 액세스할 수 있는 방법을 제공.
  - 미디어 장치 : 마이크, 웹캠
- 미디어 장치를 액세스 하면 미디어 스트림 객체를 얻음
- 미디어 객체를 PeerConnection 에 전달하여 미디어 스트림을 전송

### RTCPeerConnection

Peer 간의 연결을 생성하고, 오디오와 비디오 통신에 사용

생성시 STUN 서버 요청

- Peer 간의 화상과 음성 등을 교환하기 위한 거의 모든 작업을 수행
- 기본적인 기능
  - Singal Processing
  - Security
  - video encode/decode
  - NAT Traveral associated network
  - Packet send/receive
  - bandwidth estimation

### RTCDataChannel

peer 간의 Data를 주고 받을 수 있는 Tunnel API

(WebSocket과 유사하지만, P2P라 속도가 보다 빠름)

- Peer 간에 텍스트나 파일을 주고 받을 수 있는 메시징 API
- 설정에 따라 SCTP 또는 RTP 로 전송 가능
- WebSocket 과 같은 수준의 API 를 제공, Row Level API

## P2P 기반 NAT 및 방화벽 통과 기법

### STUN (Session Traversal Utilities for NAT) 서버

사내망 환경에서 NAT를 통해 공인 IP, Port 를 알아내는 역할

- 네트워크 장비의 일환
- 서로 연결하고자 하는 Peer 들이 NAT 나 방화벽 뒤에 존재하는지 검사하고,
  이들의 공인 IP 주소를 전달하는 역할 수행
- server / client 모델
  - STUN Client
    - NAT 나 방화벽 뒤에 존재
    - STUN 서버에게 나의 공인 IP 주소는 무엇인가 질의
  - STUN Server
    - 공인 IP 망에 존재
    - client 의 질의에 응답
  - server, client 통신으로 찾아진 공인 IP를 통해 peer 간 통신 설정

![image-20190716135351575](/Users/hanhaein/Library/Application Support/typora-user-images/image-20190716135351575.png)

### TURN (Traveral Using Relays arount NAT) 서버

P2P 연결이 안되는 환경일 때 트래픽을 중계하는데 사용함

- STUN을 통해 통신 설정을 시도 했지만 실패하고 Peer가 결국 서로를 찾지 못 했을 경우,
  TURN 서버가 Peer 간의 모든 정보를 중계해 줌
- Peer 간에 발생하는 모든 미디어에 대한 일종의 미디어 proxy 서버
- 사용 순서
  - PeerConnection 객체는 UDP 로 통신 설정을 시도
  - UDP 실패 시 TCP 로 시도
  - TCP 마저 실패 시 모든 정보는 TURN 서버에 의해 릴레이

![image-20190716135406036](/Users/hanhaein/Library/Application Support/typora-user-images/image-20190716135406036.png)

### SDP (Session Description Protocol)

- PeerConnection 객체 생성 시 PeerConnection 객체에서 offer SDP, answer SDP 를 얻을 수 있음
- SDP 에 텍스트 형태로 명시된 것
  - 미디어에 대한 메타 데이터로 사용할 수 있는 코덱의 종류
  - 사용되는 프로토콜
  - 비트레이트 값
  - bandhwidth 값
- Offer SDP
  - 연결을 먼저 맺기를 원하는 Peer의 SDP
  - 해당 Peer 는 자신의 Offer SDP 를 생성하여 다른 Peer 에게 전달하고
    전달받은 Peer 는 Answer SDP 를 생성하여 Offer 에게 전달

### ICE (Interactive Connectivity Establishment)

P2P 간 다이렉트로 통신을 위해 STUN, TURN 등의 기술을 종합 활용하여 라우팅 경로를 찾아내는 기술로 UDP hole punching (P2P 간 공인 IP가 아니더라도 최대한 연결 가능하도록 하는 기법)을 지원

- 각 Peer 가 서로 상대방을 찾기 위해 최적의 네트워크 경로를 찾을 수 있도록 도와 주는 프레임워크
- STUN 과 TURN 을 활용하여 Candidate 를 검출하고 이들 중 하나를 선택하여 Peer 간 연결을 수행

## Signaling

- WebRTC는 P2P 연결을 통해 직접 통신하지만, 이를 중계해주는 과정이 필요
- WebRTC 명세에는 들어가 있지 않고
- 표준화된 방법이 존재하지 않음
- 어떤 언어로 개발하는 무방
- 시그널 서버
  - 개발하고자 하는 서비스의 성격에 맞게 SIP나 XMPP 등의 기존의 시그널링 프로토콜 사용 가능
  - Ajax long polling 이나 websocket 등의 적절한 쌍방향 통신 채널로 구현
- 핵심
  - 비동기적으로 발생하는 Peer 들의 정보 (SDP, Candidate)를 교환하는 일
- 전이중 통신을 지원하는 websocket 으로 이를 구현하는 것이 가장 적합

## WebRTC P2P 연결 플로우

- WebRTC 어플리케이션은 P2P 연결을 위해 signaling 과정을 반드시 거쳐야 함
  - 이는 Peer 간의 서로를 식별하기 위한 과정으로 서버가 반드시 중계해야 함
- 시그널링 서버 (아래 구름 모양)
  - 채팅방과 같은 형태로 연결하고자 하는 Peer 들을 논리적으로 묶고 서로 간에 SDP 와 Candidte 를 교환

![image-20190716141620304](/Users/hanhaein/Library/Application Support/typora-user-images/image-20190716141620304.png)

## 통신 절차

### #1 DISCOVER REERS VIA SERVER

### #2 DIRECTLY CONNEC T PEERS

## 다자간 WebRTC

### Mesh 형태의 연결구조

- PeerConnection 을 이용한 보편적인 여결 방식
  - 장점
    - 서버가 없는 완전한 P2P 구조
    - 빠른 속도
  - 단점
    - Client 장비에 큰 부하
    - 회선에 따른 성능 Dependency가 큼
  - 소규모 Video Conferencing 에 적합함

### Multipoint Control Unit 을 이용한 연결 구조

- PeerConnection 의 MultiStream 이 지원되면서 가능해진 방식
- 장점
  - Client 부하가 적음
  - 대규모 연결 가능
  - 균일한 속도 보장
- 단점
  - MCU 부하/비용
  - Delay 발생
  - MCU 가 죽으면 x
- Broadcasting 방식의 Conferencing 에 적합함

### Cluster Mesh 방식의 연결 구조

-  Source MediaStream 을 Forward 하는 방식
- 장점
  - 서버 비용이 없음
  - 빠른 속도
- 단점
  - Parent 의 Bandwidth 에 Dependency 가 큼
  - 불아한 연결
  - 높은 복잡도
- 적은 부하로 대규모 연결이 가능 