# ASSIGNMENT.md

# 모바일/웹서비스 프로젝트 과제 요구사항

이 문서는 강의자료와 제공된 수행 결과 보고서에서 확인되는 **과제 기본 요구사항**만 정리한다.

추가 기능 아이디어나 임의 확장 설계는 이 문서에 섞지 않는다.

---

## 1. 제출 구조

최종 제출 저장소는 다음 형태를 목표로 한다.

```text
ROOT/
├── PhotoBlogServer/     # Django Server
├── PhotoViewer/         # Android Client
└── 모바일/웹서비스 프로젝트 공통평가 01_수행 결과 보고서.docx
```

GitHub Repository URL을 제출한다.

---

## 2. 포토 블로그 REST API

Django 기반 포토 블로그 서버를 사용한다.

기본 API 진입점:

```text
/api_root/Post/
```

주요 동작:

```text
GET
→ 게시물 목록 조회
→ JSON Array

POST
→ 게시물 등록
→ Multipart
→ Text + Image
```

강의자료에서 확인 가능한 게시물 데이터 예:

```text
author
title
text
created_date
published_date
image
```

실제 프로젝트의 Model/Serializer 필드는 구현 시 반드시 확인한다.

---

## 3. API 검증

다음 항목을 검증하고 보고서에 증빙한다.

- 웹에서 게시물 작성
- 게시 완료된 웹페이지
- API GET 요청/응답
- JSON Array 확인
- JSON Viewer 등을 이용한 Message Body 검증

로컬 API 예:

```text
http://127.0.0.1:8000/api_root/Post/
```

---

## 4. Android Client

Android 앱은 Django REST API와 통신한다.

강의자료 기본 흐름:

```text
Android Client
    ↓
GET /api_root/Post/
    ↓
JSON Array
    ↓
각 게시물의 image URL
    ↓
이미지 다운로드
    ↓
RecyclerView 출력
```

Android Emulator에서 PC localhost 접속 시 기본 예:

```text
http://10.0.2.2:8000
```

실제 실행 환경에 따라 주소가 달라질 수 있다.

---

## 5. 기본 Android 화면

강의자료 기준 기본 화면 구성:

```text
동기화 버튼
새로운 이미지 게시 버튼
상태 TextView
RecyclerView
```

동기화 버튼을 누르면 서버 게시물 이미지를 불러와 RecyclerView에 표시한다.

---

## 6. Server / Client 연동 검증

보고서에서 확인할 항목:

- Socket Server가 받은 Client 요청 메시지
- Server 브라우저 출력 화면
- Client 시작 화면
- Client 동기화 완료 화면

---

## 7. Upload 구현

강의자료에서 아래 둘 중 하나를 선택하도록 되어 있다.

```text
1. Upload (Hard coding) : 중
2. Upload (Post에 따른 UI 제공) : 상
```

이 프로젝트에서는 난이도와 완성도를 고려해 **2번 UI 제공 방식**을 선택한다.

구현 목표:

```text
제목 입력
내용 입력
이미지 선택
Multipart POST
업로드 성공
서버에서 게시물 확인
Android 재동기화 후 확인
```

---

## 8. PythonAnywhere

Django Server를 PythonAnywhere에서 동작시킨다.

확인 항목:

```text
1. PythonAnywhere에서 Server 웹페이지 정상 출력
2. 클라우드 API 정상 응답
3. Android Client에서 클라우드 Server 동기화
```

---

## 9. 추가 Client 편의 기능

강의자료/보고서에서 강의 중 제공되지 않은 Client 편의 기능을 **최대 5개** 추가할 수 있다.

각 기능에 대해 보고서에 다음을 작성한다.

```text
기능 정의
기능 설명
동작 증빙 화면 캡처
```

이 프로젝트에서 선택한 기능은 `docs/ANDROID_FEATURES.md`에 별도로 정의한다.

---

## 10. 기본 과제와 추가 기능의 구분

다음은 기본 과제 기능이다.

```text
Django 포토블로그 API
API 검증
Server/Client 연동
Android 동기화
RecyclerView 출력
Upload
PythonAnywhere
```

위 기능을 추가 기능 5개에 중복 포함하지 않는다.
