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

---

# 최종 추가 기능 목록

```text
1. 게시물 상세 화면 및 이미지 확대
2. 게시물 검색 및 정렬
3. Room 기반 즐겨찾기
4. Room 기반 오프라인 캐시
5. WorkManager 자동 동기화 및 새 게시물 Notification
```
