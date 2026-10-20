# IMPLEMENTATION_PLAN.md

# 전체 구현 계획

이 문서는 프로젝트를 어떤 순서로 구현할지 정의한다.

**한 번에 전체 Phase를 구현하지 않는다.**
각 Phase를 완료하고 테스트한 뒤 다음 단계로 이동한다.

> 새 최종 결과보고서가 배포되어 평가 ID가 갱신되었다. 아래 Phase 번호 자체는 그대로 두고,
> 각 Phase의 "보고서 증빙/캡처" 항목에만 새 ID(docs/REPORT_CHECKLIST.md 기준)를 표시해 두었다.

> **진행 현황 (2026-10-11)**: Phase 0~14(기본 기능 + 추가 기능 5개 + PythonAnywhere 배포)는
> 모두 구현 완료. 남은 작업은 Phase 15(전체 회귀 재확인)와 Phase 16(보고서 캡처/제출 정리)이며,
> 이 두 Phase는 새 기능 개발이 아니라 제출 준비 작업이다.

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

**상태: 완료.** Django project `mysite` / app `blog`, Android package `com.example.photoviewer`
확인. 중첩 `.git` 없음, `docs/ch05.md` 존재.

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
[x] python manage.py runserver 성공
[x] 웹 페이지 접속 가능
[x] /api_root/Post/ 접속 가능
[x] GET이 JSON Array 반환
[x] 이미지 URL 확인
```

**상태: 완료.** Post Model/Serializer/ModelViewSet 구현 완료, 서버 측 author 자동 할당 적용,
localhost 동작 확인 후 Phase 14에서 PythonAnywhere에도 동일 코드로 배포 완료.

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

> 새 최종 결과보고서 ID: 웹 게시물 작성/게시 완료 = ID 3, API 응답 = ID 4, JSON Viewer = ID 5

- 웹 게시물 작성 (ID 3)
- 게시 완료 (ID 3)
- API 응답 (ID 4)
- JSON Viewer (ID 5)

## 완료 조건

GET 응답 구조와 필요한 필드를 확인한다.

**상태: 완료.** GET/POST 응답 필드(author/title/text/created_date/published_date/image) 확인,
Code Coverage 자동 테스트(ID 6, `blog/tests.py`)까지 추가로 구현 완료 (13 tests 통과).

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

**상태: 완료.** INTERNET 권한, network_security_config.xml(로컬 10.0.2.2/127.0.0.1만
cleartext 허용, 그 외 도메인은 HTTPS 기본 정책 적용), 동기화/업로드 버튼, RecyclerView 구성 완료.

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

**상태: 완료.** `MainActivity.FetchPostsTask`(AsyncTask)가 GET → JSONArray 파싱 →
PostItem 리스트 생성까지 수행. Phase 14 이후에는 동일 로직이 Cloud ApiConfig 기준으로도 확인됨.

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
[x] 여러 게시물 이미지 표시
[x] 서버 데이터와 Client 결과 일치
[x] 빈 데이터 처리
[x] 기본 오류 처리
```

**상태: 완료.** `ImageAdapter` + `item_image.xml`로 목록 표시. 이미지 다운로드 실패 시에도
게시물 자체는 목록에서 제외하지 않고 Bitmap만 null로 두는 방식으로 처리.

## 보고서 캡처

- Client 시작 화면 (새 최종 결과보고서 ID 9)
- Client 동기화 완료 화면 (ID 10)

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

Socket Server가 받은 요청 메시지. (새 최종 결과보고서 ID 7 — 수신 파일을 VSCode로 연 화면으로 캡처)

**상태: 완료.** `tools/socket_server.py`로 Android Client의 실제 GET 요청을 캡처,
`tools/captured_requests/request_20261008_173639.txt`에 `GET /api_root/Post/ HTTP/1.1` 요청이
실제로 기록되어 있음을 확인함.

---

# Phase 7. Upload UI + Multipart POST

> 새 최종 결과보고서 ID 11 (MainActivity Upload 구현) — "Post에 따른 UI 제공 : 상" 방식 유지

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
[x] 입력 가능
[x] 이미지 선택 가능
[x] POST 성공
[x] Server에서 새 게시물 확인
[x] Android 동기화 후 확인
```

**상태: 완료.** Android Photo Picker(`ActivityResultContracts.PickVisualMedia`) + 수동
Multipart POST(`HttpURLConnection`)로 구현. 업로드 성공 후 MainActivity 복귀 시 자동 재동기화.
Phase 14 이후 Cloud 서버 대상 Upload도 `curl`로 실제 반영을 재확인함.

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

**상태: 완료.** `model/PostItem.java`에 author/title/text/createdDate/publishedDate/imageUrl +
favorite 상태를 보관. API에 없는 `id`를 임의로 만들지 않고, `created_date`를 `getPostKey()`의
식별 키로 사용 (Room 즐겨찾기/캐시, WorkManager 신규 게시물 판별에 공통으로 재사용됨).

---

# Phase 9. 게시물 상세 화면 + 이미지 확대 (보조 UI, 추가 기능 개수 미포함)

`docs/ANDROID_FEATURES.md`의 "참고: 게시물 상세 화면 + 이미지 확대" 구현.

> 처음에는 추가 기능 1(ID 14-1)로 계획했으나, 단순한 UI 보조 기능이라 최종 보고서의 추가 기능
> 5개에서는 제외했다. 코드는 그대로 유지한다. 대신 검색/정렬을 ID 14-1/14-2로 분리했다 (Phase 10).

**상태: 완료.** `DetailActivity.java` + `activity_detail.xml` + `ZoomableImageView.java`로 구현.
Bitmap을 Intent로 직접 전달하지 않고 title/text/date/imageUrl 문자열만 전달한 뒤 상세 화면에서
이미지를 다시 다운로드하며, Pinch Zoom은 `ZoomableImageView`에서 처리.

---

# Phase 10. 추가 기능 1·2 (새 최종 결과보고서 ID 14-1 검색, ID 14-2 정렬)

`docs/ANDROID_FEATURES.md`의:

```text
기능 1. 게시물 검색 (ID 14-1)
기능 2. 게시물 정렬 (ID 14-2)
```

구현.

**상태: 완료.** `MainActivity`에 `allPosts`(원본)/`displayedPosts`(표시용) 분리 구조와
`applyFilterAndSort()`를 추가해 서버 재호출 없이 제목/본문 검색(ID 14-1)과 최신순/오래된순
정렬(ID 14-2)을 처리. 두 기능은 같은 메서드에서 처리되지만 보고서에서는 별도 기능으로 기술한다.

---

# Phase 11. 추가 기능 3 (새 최종 결과보고서 ID 14-3)

`docs/ANDROID_FEATURES.md`의:

```text
Room 즐겨찾기
```

구현.

**상태: 완료.** `database/FavoriteEntity.java`, `FavoriteDao.java`, `AppDatabase.java`로 구현.
`postKey`(= created_date)를 기준으로 즐겨찾기를 저장하며, 앱 재실행 시 `LoadFavoritesTask`로
Room에서 불러와 상태를 복원. 전체/즐겨찾기 필터 버튼 제공.

---

# Phase 12. 추가 기능 4 (새 최종 결과보고서 ID 14-4)

`docs/ANDROID_FEATURES.md`의:

```text
Room 오프라인 캐시
```

구현.

**상태: 완료.** `database/PostCacheEntity.java`, `PostCacheDao.java`로 구현 (Favorite 테이블과
별도 관리). `AppDatabase`를 version 1 → 2로 올리며 기존 즐겨찾기 데이터를 보존하는 명시적
`MIGRATION_1_2`를 사용 (`fallbackToDestructiveMigration()` 미사용). GET 성공 시 Room에 최신
목록을 저장하고, GET 실패 시 캐시를 불러와 "오프라인 캐시" 안내와 함께 표시.

---

# Phase 13. 추가 기능 5 (새 최종 결과보고서 ID 14-5)

`docs/ANDROID_FEATURES.md`의:

```text
WorkManager 백그라운드 동기화 + 알림
```

구현.

**상태: 완료.** `worker/PostSyncWorker.java` + `PeriodicWorkRequest`(15분 최소 주기,
`NetworkType.CONNECTED`, `enqueueUniquePeriodicWork` + `ExistingPeriodicWorkPolicy.KEEP`)로 구현.
최초 실행은 baseline만 저장하고 알림 생략, 이후에는 `SharedPreferences`에 known post key를
`기존 ∪ 이번 응답` 방식(합집합)으로 누적 저장해 게시물이 응답에서 일시적으로 빠졌다가 다시
나타나도 중복 알림이 발생하지 않도록 함. Notification Channel + `POST_NOTIFICATIONS` 런타임
권한(거부 시에도 동기화 자체는 정상 동작) 처리. 즉시 테스트용 `OneTimeWorkRequest` 버튼 별도 제공.
Worker는 Room/UI를 직접 건드리지 않고 `ApiConfig.BASE_URL`만 참조 (MainActivity/UploadActivity와
동일한 단일 소스).

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
[x] Cloud Server 브라우저 정상
[x] Cloud API 정상
[x] Android GET 정상
[x] 이미지 표시 정상
```

**상태: 완료.** Cloud URL: `https://hoynola.pythonanywhere.com` (GitHub 연동 배포, Manual
configuration 웹앱). `ApiConfig.BASE_URL`을 이 값으로 변경한 것만으로 MainActivity 동기화/
RecyclerView 표시/DetailActivity/UploadActivity/PostSyncWorker가 모두 동일하게 Cloud 서버를
사용하도록 전환됨 (서버 주소 중복 하드코딩 없음). `curl`로 Cloud 서버의 실제 JSON 응답과
media 이미지 URL까지 직접 확인함. Django 서버 코드/Room schema는 이 과정에서 수정하지 않음.

## 보고서 캡처

- PythonAnywhere Server (새 최종 결과보고서 ID 12)
- Android Cloud 동기화 (ID 13)

---

# Phase 15. 전체 회귀 테스트

모든 기능을 처음부터 다시 확인한다.

```text
[x] Server 실행
[x] GET
[x] POST
[x] Android 동기화
[x] Upload
[x] 상세 보기
[x] 이미지 확대 (보조 UI, 추가 기능 미포함)
[x] 검색
[x] 정렬
[x] 즐겨찾기
[x] 오프라인 캐시
[x] WorkManager
[x] Notification
[x] PythonAnywhere
```

각 기능은 해당 Phase(9~13) 구현 시 개별적으로 동작 확인을 마쳤다. PythonAnywhere 전환
(Phase 14) 이후에는 동기화/게시물 목록/이미지 표시/DetailActivity/Upload/PostSyncWorker를
Cloud 서버 기준으로 다시 확인했다. 검색·정렬·즐겨찾기·오프라인 캐시는 로컬 서버 기준으로는
확인되었으나, Cloud 서버로 전환한 뒤 별도로 재확인하지는 않았다 — 서버 주소만 바뀌는
변경이라 동작에 영향이 없을 것으로 보이지만, 보고서 캡처 전 한 번 더 눈으로 확인하는 것을 권장한다.

---

# Phase 16. 보고서 작성 준비

`docs/REPORT_CHECKLIST.md`를 기준으로 필요한 화면을 모두 캡처한다.

누락된 증빙이 있으면 기능을 다시 실행하여 캡처한다.

**상태: 사용자 확인 기준 촬영 완료.** `docs/REPORT_CHECKLIST.md`의 ID 1~14(14-1~14-5) 캡처
목록은 사용자가 직접 촬영을 완료했다고 확인한 상태다 (실제 파일 존재 여부/화면 내용은 Claude
Code가 검증할 수 없음). 남은 작업은 보고서 문서(.docx)에 각 이미지를 실제로 삽입하고
학번/이름 등 제출 정보를 채우는 것으로, 개발 작업이 아니라 문서 제출 작업이다.
