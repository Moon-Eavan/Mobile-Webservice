# ANDROID_FEATURES.md

# 추가 Client 편의 기능 5개

이 문서는 기본 강의자료에 없는 추가 기능만 다룬다.

기본 `동기화`, `RecyclerView`, `Upload`는 추가 기능으로 계산하지 않는다.

---

# 기능 1. 게시물 상세 화면 + 이미지 확대

난이도: 중

## 목적

RecyclerView에서 게시물 이미지만 보는 대신 게시물 전체 내용을 상세 화면에서 확인한다.

## 권장 파일

```text
java/<현재패키지>/DetailActivity.java
res/layout/activity_detail.xml
```

## 표시 정보

```text
큰 이미지
제목
본문
작성 또는 게시 날짜
```

## 동작

```text
RecyclerView 게시물 선택
→ DetailActivity 이동
→ 해당 게시물 정보 표시
→ 이미지 확대 가능
```

가능하면 Pinch Zoom 또는 전체 화면 이미지 보기를 구현한다.

Bitmap 자체를 큰 Intent Extra로 직접 전달하는 것은 피한다.

가능하면:

```text
title
text
date
imageUrl
```

을 전달하거나 저장된 데이터를 사용한다.

## 증빙

```text
목록 화면
상세 화면
이미지 확대 화면
```

## 현재 상태: 완료

실제 구현 파일: `DetailActivity.java`, `res/layout/activity_detail.xml`, `ZoomableImageView.java`.
Bitmap을 Intent extra로 직접 전달하지 않고 title/text/date/imageUrl 문자열만 넘긴 뒤
DetailActivity에서 이미지를 다시 다운로드한다. Pinch Zoom은 `ZoomableImageView`가 처리.

---

# 기능 2. 게시물 검색 + 정렬

난이도: 중

## 목적

게시물이 많아졌을 때 제목/본문으로 검색하고 날짜순으로 정렬할 수 있게 한다.

## 검색 대상

```text
title
text
```

## 정렬

```text
최신순
오래된순
```

## 구현 원칙

서버를 매번 다시 호출하지 않고 현재 다운로드한 목록을 우선 사용한다.

원본 목록과 표시 목록을 분리한다.

예:

```text
allPosts
displayedPosts
```

검색어 삭제 시 전체 목록으로 복원한다.

## 증빙

```text
전체 목록
검색 결과
최신순
오래된순
```

## 현재 상태: 완료

실제 구현 위치: `MainActivity.java`. `allPosts`(서버 원본)와 `displayedPosts`(검색/정렬/즐겨찾기
필터 적용 결과)를 분리하고, `applyFilterAndSort()`에서 서버 재호출 없이 제목/본문 검색과
최신순/오래된순 정렬을 적용한다. 검색어를 지우면 `applyFilterAndSort()`가 다시 호출되어
전체 목록으로 복원된다.

---

# 기능 3. Room DB 즐겨찾기

난이도: 중상

## 목적

사용자가 원하는 게시물을 즐겨찾기로 저장하고 앱을 종료해도 유지한다.

## 권장 패키지

```text
java/<현재패키지>/database/
```

## 권장 파일

```text
AppDatabase.java
FavoriteDao.java
FavoriteEntity.java
```

## 데이터 예

```text
postKey
title
imageUrl
favorite
```

Server API에 실제 `id`가 있으면 그것을 우선 사용한다.

`id`가 없으면 임의 숫자를 서버 ID처럼 취급하지 않는다.

## UI

```text
♡ → ♥
```

필요하면:

```text
전체 | 즐겨찾기
```

필터를 제공한다.

## 완료 조건

```text
게시물 즐겨찾기
→ 앱 종료
→ 재실행
→ 즐겨찾기 유지
```

## 증빙

```text
즐겨찾기 전
즐겨찾기 후
즐겨찾기 목록
재실행 후 유지
```

## 현재 상태: 완료

실제 구현 파일: `database/FavoriteEntity.java`, `database/FavoriteDao.java`,
`database/AppDatabase.java`. Server API에 `id`가 없으므로 `created_date` 기반
`PostItem.getPostKey()`를 즐겨찾기 식별 키로 사용한다. 앱 실행 시 `LoadFavoritesTask`로
Room에서 즐겨찾기 키를 불러와 복원하므로 앱을 종료했다 다시 켜도 유지된다.
전체/즐겨찾기 필터 버튼(`btnFilterAll`, `btnFilterFavorites`) 제공.

---

# 기능 4. Room 기반 오프라인 캐시

난이도: 상

## 목적

네트워크 연결이 없어도 마지막으로 성공적으로 받아온 게시물 정보를 표시한다.

## 권장 파일

```text
PostCacheEntity.java
PostCacheDao.java
AppDatabase.java
```

## 온라인 동작

```text
GET 성공
→ RecyclerView 표시
→ Room에 최신 데이터 저장
```

## 오프라인/네트워크 실패

```text
GET 실패
→ Room 조회
→ 마지막 저장 게시물 표시
→ 오프라인 상태 안내
```

예:

```text
네트워크에 연결할 수 없습니다.
저장된 게시물을 표시합니다.
```

## 주의

이미지를 DB BLOB으로 무조건 저장하지 않는다.

먼저 게시물 메타데이터 캐시를 구현하고, 이미지 캐싱이 필요한 경우 별도 전략을 사용한다.

네트워크 오류와 JSON 파싱 오류를 가능한 한 구분한다.

## 증빙

```text
온라인 정상 화면
네트워크 OFF
오프라인 안내
캐시 목록 출력
```

## 현재 상태: 완료

실제 구현 파일: `database/PostCacheEntity.java`, `database/PostCacheDao.java`. Favorite
테이블과는 별도로 관리하며, `AppDatabase`를 version 1 → 2로 올리면서 기존 즐겨찾기 데이터를
보존하는 명시적 `MIGRATION_1_2`(post_cache 테이블만 새로 생성)를 사용했다
(`fallbackToDestructiveMigration()`은 사용하지 않음, 즐겨찾기 데이터 유실 방지 목적).
GET 성공 시 Room에 최신 목록을 저장(`replaceAll`)하고, GET 실패 시 Room에서 마지막 목록을
읽어와 "오프라인 캐시" 안내 문구와 함께 표시한다. 이미지는 BLOB으로 저장하지 않고
메타데이터(author/title/text/createdDate/publishedDate/imageUrl)만 캐시한다.

---

# 기능 5. WorkManager 자동 동기화 + Notification

난이도: 상

## 목적

앱 사용자가 수동으로 동기화 버튼을 누르지 않아도 주기적으로 새 게시물 여부를 확인하고 알림을 제공한다.

## 권장 파일

```text
java/<현재패키지>/worker/PostSyncWorker.java
```

## 동작

```text
WorkManager
→ GET /api_root/Post/
→ 기존 로컬 데이터와 비교
→ 새 게시물 발견
→ 로컬 DB 업데이트
→ Notification
```

## 알림 예

```text
PhotoViewer
새로운 게시물이 등록되었습니다.
"게시물 제목"
```

가능하면 알림을 눌렀을 때 해당 게시물 상세 화면으로 이동한다.

## Android 제약

PeriodicWorkRequest는 시스템의 최소 반복 간격 제한을 따른다.

보고서 테스트를 위해 필요하면:

```text
OneTimeWorkRequest
```

또는 테스트용 1회 실행 UI를 사용할 수 있다.

비정상적으로 짧은 반복 실행으로 시스템 제한을 우회하지 않는다.

## Notification

현재 target SDK에 맞게:

- Notification Channel
- 알림 권한

등을 처리한다.

## 완료 조건

```text
Worker 실행
→ Server 조회
→ 새 게시물 비교
→ 새 게시물이 있을 때만 알림
→ 중복 알림 최소화
```

## 증빙

```text
자동 동기화 테스트
Server에 새 게시물 생성
Notification 표시
Notification 클릭 후 앱/상세 화면
```

## 현재 상태: 완료

실제 구현 파일: `worker/PostSyncWorker.java`. `PeriodicWorkRequest`를 시스템 최소 주기인
15분으로 등록(`NetworkType.CONNECTED` 제약, `enqueueUniquePeriodicWork` +
`ExistingPeriodicWorkPolicy.KEEP`으로 중복 등록 방지)하며, 보고서 검증용으로 즉시 1회
실행하는 `OneTimeWorkRequest` 버튼(`btnTestBackgroundSync`)을 별도로 둔다.

Worker는 Room/UI를 직접 건드리지 않고 독립적인 `SharedPreferences`에 "이미 알고 있는 게시물"
키 집합을 저장한다. API 응답에 `id`가 없으므로 `created_date`를 식별 키로 사용한다. 최초
실행은 baseline만 저장하고 알림을 생략하며, 이후 실행부터는 `기존 known keys ∪ 이번 응답`
방식(합집합)으로 누적 저장한다 — 특정 실행에서 일부 게시물이 응답에 일시적으로 빠졌다가
나중에 다시 나타나도 "새 게시물"로 잘못 재판정되어 중복 알림이 발생하지 않도록 하기 위함이다.

Notification Channel을 멱등하게 생성하고, Android 13+에서는 `POST_NOTIFICATIONS` 런타임
권한을 요청하되 거부되어도 동기화 자체(baseline/중복 방지 갱신)는 계속 정상 동작하고 알림만
생략한다. Notification을 누르면 MainActivity로 이동한다 (`FLAG_ACTIVITY_NEW_TASK |
FLAG_ACTIVITY_CLEAR_TOP`). Cloud 전환(Phase 14) 이후에도 `ApiConfig.BASE_URL`만 참조하므로
별도 코드 변경 없이 동일하게 동작함을 확인했다.

---

# ApiConfig / 서버 주소 관리

Server 주소는 `PhotoViewer/app/src/main/java/com/example/photoviewer/ApiConfig.java`
한 곳에서만 관리한다 (`public static final String BASE_URL`).

```text
MainActivity        → ApiConfig.BASE_URL + "/api_root/Post/"  (GET)
UploadActivity       → ApiConfig.BASE_URL + "/api_root/Post/"  (POST)
PostSyncWorker        → ApiConfig.BASE_URL + "/api_root/Post/"  (GET, 백그라운드)
DetailActivity        → 서버가 내려준 절대 image URL을 그대로 사용 (BASE_URL을 직접 조립하지 않음)
```

현재 값은 PythonAnywhere Cloud 주소로 설정되어 있다.

```text
BASE_URL = "https://hoynola.pythonanywhere.com"
```

로컬 Django 서버로 되돌리려면 이 상수 한 줄만 `"http://10.0.2.2:8000"`으로 바꾸면 되고,
다른 파일을 수정할 필요는 없다. `network_security_config.xml`은 `10.0.2.2`/`127.0.0.1`
도메인에 한해서만 cleartext(HTTP)를 허용하며, HTTPS인 PythonAnywhere 주소는 이 설정과
무관하게 Android 기본 보안 정책(시스템 인증서 신뢰)을 그대로 따른다.

---

# 최종 추가 기능 목록

```text
1. 게시물 상세 화면 및 이미지 확대       — 완료 (ID 14-1)
2. 게시물 검색 및 정렬                  — 완료 (ID 14-2)
3. Room 기반 즐겨찾기                   — 완료 (ID 14-3)
4. Room 기반 오프라인 캐시              — 완료 (ID 14-4)
5. WorkManager 자동 동기화 및 새 게시물 Notification — 완료 (ID 14-5)
```
