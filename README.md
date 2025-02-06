# 🍽 오늘 뭐 먹지? (Tasteful-AI)

---

## **AI 메뉴 추천 & 맛집 지도 검색 & 오픈 채팅**

> **사용자 취향 기반 AI 메뉴 추천 서비스**
>
> "점심, 저녁 무엇을 먹을 지 매번 고민하지 마세요!"
>
> 바쁜 현대인을 위한 AI 기반 메뉴 추천 애플리케이션입니다. 추천 받은 메뉴를 지도에서 검색하여 맛집을 찾을 수 있으며, 추천 받은 메뉴와 맛집 정보를 오픈 톡방에서 함께 나눌 수 있습니다.

---

## 🍙 KEY SUMMARY

### 🔍 **단일 서버 vs. 이중 서버, 성능 차이는 얼마나 날까?**

<details>
<summary>🔹 테스트 개요</summary>

- Redis 기반 캐싱을 적용한 상태에서 단일 서버와 이중 서버 환경에서의 성능 차이를 측정
- 부하 테스트(Load Test)를 수행하여 트랜잭션 처리 속도를 비교하고, 서버 확장성에 따른 성능 변화를 분석
</details>

<details>
<summary>🔹 테스트 환경</summary>

- JMeter를 사용하여 TPS(Transactions Per Second) 측정
- 테스트 조건
  - User(Concurrent Users) : 200명
  - Duration(Seconds) : 20초
  - Loop Count: 3000
</details>

<details>
<summary>🔹 문제 상황 및 원인 분석</summary>

1. 단일 서버 환경에서 일정 TPS를 유지하다가 특정 시점 이후 성능이 급격히 감소하며 요청 실패
2. CPU 및 메모리 사용량 급증 → 단일 서버의 리소스 한계
3. Redis 캐싱 적용했음에도 서버 부하 증가로 인한 처리 한계
</details>

<details>
<summary>🔹 해결 방법</summary>

- **서버 확장(Scaling Out)** : 기존 단일 서버에서 2개의 서버(이중 인스턴스)로 확장하여 부하 분산
- **로드 밸런서 적용** : 요청을 두 개의 서버에 분산 처리
- 동일한 테스트 조건을 유지하며 TPS 비교 분석
</details>

<details>
<summary>🔹 성능 비교 결과</summary>

| 환경 | 평균 TPS | 최대 TPS | 요청 실패율 |
| --- | --- | --- | --- |
| 단일 서버(1개 인스턴스) | 약 280 TPS | 320 TPS | 66.7% |
| 이중 서버(2개 인스턴스) | 약 540 TPS | 580 TPS | 요청 실패 없음 |
</details>

---

## 🍙 **인프라 설계도**

<details>
<summary>🔧 최적의 인프라 설계</summary>

- **배포 및 CI/CD**
  - GitHub Actions를 활용한 자동 빌드 및 배포
  - Docker 컨테이너 패키징 및 배포

- **프론트엔드**
  - React 기반 프론트엔드 → Amazon CloudFront를 통해 배포
  - Route 53을 이용한 도메인 관리

- **백엔드**
  - Spring Boot 기반 애플리케이션 → AWS EC2에서 실행
  - ALB (Application Load Balancer)로 요청 라우팅

- **데이터베이스 및 캐시**
  - MySQL : 주요 데이터 저장
  - Redis : 캐싱을 통한 성능 최적화

- **클라우드 스토리지**
  - S3 : 이미지 저장 및 관리

- **외부 API**
  - OpenAI GPT-3.5 Turbo : AI 메뉴 추천
  - Kakao Map API : 맛집 검색 기능
</details>

---

## 🍙 **주요 기능 MVP**

### 🔐 **인증/인가 (Spring Security & JWT)**

<details>
<summary>🔹 JWT 기반 인증</summary>

- Access Token 및 Refresh Token 발급
- Redis를 활용한 Refresh Token 저장 및 만료 관리
- 로그아웃 시 Redis 블랙리스트 등록
</details>

<details>
<summary>🔹 사용자 관리 기능</summary>

- 회원가입 및 비밀번호 암호화 저장
- 비밀번호 변경 및 탈퇴 기능 제공 (Soft Delete 방식)
</details>

<details>
<summary>🔹 보안 강화</summary>

- CORS 정책 적용
- Custom Exception Handling을 통한 보안 강화
- JWT 기반 무상태 인증
</details>

---

### 🪪 **역할 및 권한 구조**

<details>
<summary>🔹 역할 및 권한 구분</summary>

- **관리자(Admin)** : 회원 관리, 채팅방 관리 가능
- **일반 회원(Member)** : AI 추천, 맛집 검색, 채팅 참여 가능
</details>

<details>
<summary>🔹 Spring Security 기반 접근 제어</summary>

- JWT를 활용하여 사용자 권한 검증
- 관리자는 추가적인 권한 체크 적용
</details>

---

### 🎆 **사용자 프로필 이미지 관리 (AWS S3)**

<details>
<summary>🔹 주요 기능</summary>

- S3에 이미지 저장, 다운로드 및 삭제 지원
- Presigned URL을 활용한 보안 강화
</details>

<details>
<summary>🔹 최적화된 데이터 관리</summary>

- CDN 활용하여 빠른 이미지 로딩
- S3 Intelligent-Tiering 적용으로 스토리지 비용 최적화
</details>

---

### 🍱 **개인 취향을 반영한 맞춤형 메뉴 추천**

<details>
<summary>🔹 취향 데이터 저장 방식</summary>

- 사용자 취향 데이터를 5개 카테고리로 관리
- MySQL & Redis 활용하여 빠른 조회 성능 제공
</details>

<details>
<summary>🔹 AI 연동 방식</summary>

- 취향 데이터를 OpenAI 프롬프트에 포함하여 맞춤형 추천
- AI 응답을 JSON으로 변환 및 Redis 캐싱 적용
</details>

---

### 🍽 **AI 메뉴 추천 시스템 (Spring AI)**

<details>
<summary>🔹 AI 추천 요청 및 응답 흐름</summary>

- OpenAI API 호출 및 JSON 형식 데이터 반환
- Redis를 활용한 요청 제한 (하루 10회)
</details>

<details>
<summary>🔹 데이터 저장 및 최적화</summary>

- MySQL에 추천 내역 저장 후 Redis 캐싱
</details>

---

### 🗺 **카카오 지도 API 연동**

<details>
<summary>🔹 검색 기능</summary>

- 현재 위치 기반 맛집 검색
- 키워드 기반 식당명 검색
</details>

---

### 📨 **오픈 톡방 (WebSocket & STOMP & Redis Pub/Sub)**

<details>
<summary>🔹 다중 채팅방 지원</summary>

- 관리자(Admin) : 채팅방 생성 및 삭제 가능
- 일반 회원(Member) : 채팅방 참여 가능
</details>

<details>
<summary>🔹 Redis Pub/Sub 활용</summary>

- 다중 서버 환경에서도 원활한 메시지 처리
- 최신 메시지 50개 Redis 캐싱
</details>

---
