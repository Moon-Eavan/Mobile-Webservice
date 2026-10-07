# IMPLEMENTATION_PLAN.md

# 전체 구현 계획

이 문서는 프로젝트를 어떤 순서로 구현할지 정의한다.

**한 번에 전체 Phase를 구현하지 않는다.**
각 Phase를 완료하고 테스트한 뒤 다음 단계로 이동한다.

---

# Phase 0. 프로젝트 준비 및 분석

## 목표

현재 Repository 구조와 생성된 기본 프로젝트를 분석한다.

## 확인 항목

```text
PhotoBlogServer/
PhotoViewer/
docs/
CLAUDE.md
```

확인:

- Django 프로젝트가 생성되어 있는지
- Android 프로젝트가 생성되어 있는지
- 실제 Django project/app 이름
- 실제 Android package name
- 중첩 `.git` 존재 여부
- `.gitignore`
- 강의자료 `docs/ch05.md` 존재 여부

## 이 단계에서는

가능하면 코드를 수정하지 않고 먼저 구조를 보고한다.

---

# Phase 1. Django Server 기본 동작

## 목표

Django 포토블로그 서버가 localhost에서 정상 동작하도록 한다.

## 주요 작업

- Django project/app 설정 확인
- Post Model 확인/구현
- Serializer 확인/구현
- DRF ViewSet 구현
- URL Router 설정
- 이미지 업로드용 media 설정
- 필요한 migration 수행

## 기본 API

```text
/api_root/Post/
```

## 완료 조건

```text
[ ] python manage.py runserver 성공
[ ] 웹 페이지 접속 가능
[ ] /api_root/Post/ 접속 가능
[ ] GET이 JSON Array 반환
[ ] 이미지 URL 확인
```

---

# Phase 2. REST API 검증

## 목표

강의자료의 API 검증 요구사항을 확인한다.

## 확인

```text
GET /api_root/Post/
POST /api_root/Post/
```

## 보고서 증빙

- 웹 게시물 작성
- 게시 완료
- API 응답
- JSON Viewer

## 완료 조건

GET 응답 구조와 필요한 필드를 확인한다.

---

# Phase 3. Android 기본 설정

## 목표

Android 앱이 네트워크 요청을 할 수 있도록 기본 설정한다.

## 주요 파일

```text
PhotoViewer/app/src/main/AndroidManifest.xml
PhotoViewer/app/src/main/res/xml/network_security_config.xml
PhotoViewer/app/src/main/res/layout/activity_main.xml
PhotoViewer/app/src/main/res/layout/item_image.xml
```

## 작업

- INTERNET permission
- 로컬 HTTP 테스트 설정
- 동기화 버튼
- 새로운 이미지 게시 버튼
- TextView
- RecyclerView

## 완료 조건

앱이 컴파일되고 기본 화면이 정상 출력된다.

---

# Phase 4. GET 동기화

## 목표

Android에서 Django Server의 게시물 목록을 가져온다.

## 흐름

```text
동기화 버튼
→ GET /api_root/Post/
→ 응답 문자열 읽기
→ JSONArray 파싱
→ image URL 추출
```

인증이 필요한 경우 실제 Server 설정을 확인한다.

## 완료 조건

Android Log 또는 디버깅 결과에서 서버 JSON 수신 성공.

---

# Phase 5. RecyclerView 이미지 출력

## 목표

API 응답의 이미지 URL을 다운로드하여 목록에 표시한다.

## 주요 파일

```text
MainActivity.java
ImageAdapter.java
item_image.xml
```

## 완료 조건

```text
[ ] 여러 게시물 이미지 표시
[ ] 서버 데이터와 Client 결과 일치
[ ] 빈 데이터 처리
[ ] 기본 오류 처리
```

## 보고서 캡처

- Client 시작 화면
- Client 동기화 완료 화면

---

# Phase 6. Socket Server 요청 검증

## 목표

Android Client가 보내는 실제 HTTP Request를 검증한다.

## 확인 대상

```text
Request Line
Authorization Header (사용하는 경우)
Host
Connection 등
```

기존 수업 Socket Server가 있으면 활용한다.

## 보고서 캡처

Socket Server가 받은 요청 메시지.

---

# Phase 7. Upload UI + Multipart POST

## 목표

Hard Coding이 아니라 사용자 입력 기반 Upload를 구현한다.

## 신규 파일 권장

```text
UploadActivity.java
activity_upload.xml
```

## UI

```text
제목 입력
내용 입력
이미지 선택
이미지 미리보기
업로드
취소
ProgressBar
```

## 동작

```text
이미지 선택
→ Uri 획득
→ Multipart 구성
→ POST /api_root/Post/
→ 성공
→ MainActivity 복귀
→ 재동기화
```

## 완료 조건

```text
[ ] 입력 가능
[ ] 이미지 선택 가능
[ ] POST 성공
[ ] Server에서 새 게시물 확인
[ ] Android 동기화 후 확인
```

---

# Phase 8. PostItem 데이터 구조 개선

## 목표

기존 `List<Bitmap>` 중심 구조를 게시물 객체 중심으로 개선한다.

## 신규 파일

```text
model/PostItem.java
```

## 후보 필드

```text
id (API에 실제 존재하는 경우)
author
title
text
createdDate
publishedDate
imageUrl
bitmap
```

API에 없는 `id`를 임의로 서버 ID처럼 만들지 않는다.

## 완료 조건

RecyclerView가 `PostItem` 목록을 기반으로 동작하면서 기존 기능이 그대로 유지된다.

---

# Phase 9. 추가 기능 1

`docs/ANDROID_FEATURES.md`의:

```text
게시물 상세 화면 + 이미지 확대
```

구현.

---

# Phase 10. 추가 기능 2

`docs/ANDROID_FEATURES.md`의:

```text
게시물 검색 + 정렬
```

구현.

---

# Phase 11. 추가 기능 3

`docs/ANDROID_FEATURES.md`의:

```text
Room 즐겨찾기
```

구현.

---

# Phase 12. 추가 기능 4

`docs/ANDROID_FEATURES.md`의:

```text
Room 오프라인 캐시
```

구현.

---

# Phase 13. 추가 기능 5

`docs/ANDROID_FEATURES.md`의:

```text
WorkManager 자동 동기화 + Notification
```

구현.

---

# Phase 14. PythonAnywhere

## 목표

Django Server를 PythonAnywhere에서 동작시키고 Android Client를 클라우드 서버에 연결한다.

## 작업

- 기존 PythonAnywhere 계정/프로젝트 확인
- Django 배포
- static/media 확인
- API 확인
- Android Server URL을 클라우드 주소로 변경
- GET 테스트
- 가능하면 Upload도 확인

## 완료 조건

```text
[ ] Cloud Server 브라우저 정상
[ ] Cloud API 정상
[ ] Android GET 정상
[ ] 이미지 표시 정상
```

## 보고서 캡처

- PythonAnywhere Server
- Android Cloud 동기화

---

# Phase 15. 전체 회귀 테스트

모든 기능을 처음부터 다시 확인한다.

```text
[ ] Server 실행
[ ] GET
[ ] POST
[ ] Android 동기화
[ ] Upload
[ ] 상세 보기
[ ] 이미지 확대
[ ] 검색
[ ] 정렬
[ ] 즐겨찾기
[ ] 오프라인 캐시
[ ] WorkManager
[ ] Notification
[ ] PythonAnywhere
```

---

# Phase 16. 보고서 작성 준비

`docs/REPORT_CHECKLIST.md`를 기준으로 필요한 화면을 모두 캡처한다.

누락된 증빙이 있으면 기능을 다시 실행하여 캡처한다.
