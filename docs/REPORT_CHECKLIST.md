# REPORT_CHECKLIST.md

# 수행 결과 보고서 / 캡처 체크리스트

구현 중 필요한 증빙 화면을 놓치지 않기 위한 문서다.

각 Phase가 끝날 때 현재 찍을 수 있는 화면을 바로 저장하는 것을 권장한다.

> 새 최종 결과보고서가 배포되어 평가 ID가 갱신되었다. 이 문서의 섹션 번호는
> 새 결과보고서의 ID 1~14(14는 14-1~14-5 하위 번호)를 그대로 따른다.

> **스크린샷 상태 안내 (2026-10-11)**: 아래 "보고서 증빙 촬영 완료" 체크는 실제 캡처 파일이
> Repository 밖 별도 폴더에 저장되어 있고, **사용자가 직접 촬영을 완료했다고 확인한 내용을
> 그대로 반영한 것**이다. Claude Code는 그 파일의 실제 존재 여부, 화질, 화면 내용(예: 주소창에
> URL이 보이는지 등)을 직접 검증할 수 없으므로, 아래 체크는 모두 "사용자 확인 기준"이며
> Claude가 화면을 직접 확인했다는 의미가 아니다. "기능 구현 완료" 체크는 이와 별도로 실제
> 코드를 읽어 확인한 것이다.

---

# ID 1. 폴더 구조

```text
[ ] ROOT 폴더 구조 캡처
[ ] PhotoBlogServer 폴더 확인
[ ] PhotoViewer 폴더 확인
[ ] 수행 결과 보고서 파일 확인
```

사용자로부터 특정된 캡처 파일명이 없으므로 임의로 파일명을 만들지 않는다.
→ **폴더 구조 캡처 필요 여부는 사용자 확인 필요.**

---

# ID 2. GitHub Repository URL

```text
[x] GitHub Repository 연결 확인 (git remote)
[ ] GitHub Repository URL 정상 접속 (사용자 확인 필요)
```

`git remote -v` 기준 origin: `https://github.com/Moon-Eavan/Mobile-Webservice` (private/public
여부, 실제 브라우저 접속 가능 여부는 Claude Code가 확인할 수 없음).

---

# ID 3. 포토 블로그 API POST

```text
[x] 기능 구현 완료 (Django post_new/post_edit 템플릿, 웹에서 게시물 작성 가능)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 03-1_post_create.jpg, 03-2_post_published.jpg)
```

주의: 게시 완료 페이지 캡처는 주소창에 URL이 보이도록 촬영해야 한다는 요구사항이 있었다
(실제 화면 내용은 사용자 확인 기준).

---

# ID 4. 포토 블로그 API 검증 (2/3)

```text
[x] 기능 구현 완료 (/api_root/Post/ GET 정상 응답, JSON Array 반환 — tests.py로도 검증됨)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 04_api_terminal.jpg)
```

---

# ID 5. 포토 블로그 API 검증 (3/3)

```text
[x] 기능 구현 완료 (JSON Viewer로 확인 가능한 응답 필드: title/text/date/image 모두 존재)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 05_codebeautify_json.jpg)
```

---

# ID 6. 포토 블로그 API 검증 엔진 자동테스트

```text
[x] 기능 구현 완료
[x] 보고서 증빙 촬영 완료 (사용자 확인: 06_api_test_coverage.jpg)
```

구현 내용 (코드로 직접 확인):

```text
구현 파일: PhotoBlogServer/blog/tests.py (rest_framework.test.APITestCase)
coverage 설정: PhotoBlogServer/.coveragerc
테스트 데이터: setUpTestData로 생성한 전용 데이터 사용 (Django 테스트 DB,
                실제 db.sqlite3/수동 데이터와 분리됨)
테스트 항목: GET 200/JSON Array/필드 검증, POST 201/title·text·image 반영,
            author read_only 무시 확인, 필수 필드 누락 400, 상세조회 200/404
테스트 결과: Ran 13 tests ... OK (마지막 확인 당시 전체 coverage 약 69%)
```

실행 명령:

```text
cd PhotoBlogServer
venv/bin/coverage run manage.py test blog
venv/bin/coverage html
```

캡처 대상: `PhotoBlogServer/htmlcov/index.html` 화면 (파일별 Coverage % 테이블이 보이도록).

---

# ID 7. Socket Server 요청 메시지

```text
[x] 기능 구현 완료 (tools/socket_server.py, 실제 Android GET 요청 캡처됨)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 07_socket_request_vscode.jpg)
```

`tools/captured_requests/request_20261008_173639.txt`에 `GET /api_root/Post/ HTTP/1.1` 요청이
실제로 기록되어 있음을 코드/파일로 확인했다.

---

# ID 8. Server 브라우저 출력화면

```text
[x] 기능 구현 완료 (localhost에서 post_list 등 웹 페이지 정상 동작)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 08_server_browser_localhost.jpg)
```

---

# ID 9. Client 시작 화면

```text
[x] 기능 구현 완료 (MainActivity 기본 화면: 동기화/업로드 버튼, 상태 TextView, RecyclerView)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 09_client_start.png)
```

---

# ID 10. Client 동기화 완료 화면 (localhost)

```text
[x] 기능 구현 완료 (MainActivity GET 동기화 → RecyclerView 표시, ApiConfig 기준 localhost)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 10_client_sync_localhost.png)
```

---

# ID 11. Upload (MainActivity)

> "Post에 따른 UI 제공 : 상" 방식으로 제출한다 (Hard coding 방식은 제출하지 않음).

```text
[x] 기능 구현 완료 (UploadActivity: 제목/내용 입력, Android Photo Picker, Multipart POST,
    업로드 성공 후 MainActivity 자동 재동기화)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 11-1_upload_ui.png, 11-2_upload_success.png,
    11-3_upload_server_result.jpg)
```

---

# ID 12. PythonAnywhere Server 브라우저 출력화면

```text
[x] 기능 구현 완료 (Cloud URL: https://hoynola.pythonanywhere.com, 웹/admin/API 정상 동작을
    curl 및 Android 연동 테스트로 확인)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 12_pythonanywhere_server.jpg)
```

---

# ID 13. PythonAnywhere Client 동기화 완료 화면

```text
[x] 기능 구현 완료 (ApiConfig.BASE_URL을 Cloud 주소로 변경 → MainActivity 동기화, 게시물 목록/
    이미지 표시, DetailActivity, Upload, PostSyncWorker 모두 Cloud 서버 기준 정상 동작 확인)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 13_client_sync_cloud.png)
```

캡처 방법: MainActivity에서 "동기화" 버튼을 눌러 `"게시물 N개를 불러왔습니다."` 문구와 함께
Cloud 서버의 게시물(이미지 포함)이 RecyclerView에 표시된 화면을 캡처한다. 클라우드 연동임을
명확히 하려면 ID 12(브라우저로 본 Cloud 서버 화면)와 함께 배치하거나, 보고서 텍스트에
`ApiConfig.BASE_URL = https://hoynola.pythonanywhere.com`로 동기화했다는 설명을 추가한다.

---

# ID 14-1. 추가 기능 1 - 상세 화면 + 이미지 확대

```text
[x] 기능 구현 완료 (DetailActivity.java, activity_detail.xml, ZoomableImageView.java)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 14-1-1_post_detail.png, 14-1-2_image_zoom.png)
```

보고서 설명 예시:

```text
RecyclerView의 게시물을 선택하면 제목, 본문, 날짜, 이미지를
확인할 수 있는 상세 화면으로 이동하도록 구현하였다.
이미지는 확대하여 확인할 수 있다.
```

---

# ID 14-2. 추가 기능 2 - 검색 + 정렬

```text
[x] 기능 구현 완료 (MainActivity: allPosts/displayedPosts 분리, applyFilterAndSort())
[x] 보고서 증빙 촬영 완료 (사용자 확인: 14-2-1_search.png, 14-2-2_sort.png)
```

---

# ID 14-3. 추가 기능 3 - 즐겨찾기

```text
[x] 기능 구현 완료 (database/FavoriteEntity.java, FavoriteDao.java, AppDatabase.java;
    postKey = created_date 기반, 앱 재실행 후에도 Room에서 복원됨)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 14-3-1_favorite.png, 14-3-2_favorite_filter.png)
```

---

# ID 14-4. 추가 기능 4 - 오프라인 캐시

```text
[x] 기능 구현 완료 (database/PostCacheEntity.java, PostCacheDao.java; GET 실패 시 Room 캐시로
    대체 표시, AppDatabase version 1→2 명시적 마이그레이션으로 즐겨찾기 데이터 보존)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 14-4-1_offline_cache.png, 14-4-2_offline_feature.png)
```

---

# ID 14-5. 추가 기능 5 - WorkManager + Notification

```text
[x] 기능 구현 완료 (worker/PostSyncWorker.java, PeriodicWorkRequest 15분 주기, 합집합 방식
    known keys 누적 저장으로 중복 알림 방지, Notification Channel + POST_NOTIFICATIONS 권한 처리)
[x] 보고서 증빙 촬영 완료 (사용자 확인: 14-5-1_background_sync.png, 14-5-2_notification.png)
```

---

# 최종 기능 검사

(아래는 특정 ID에 대응하는 항목이 아니라, 제출 전 전체 기능을 한 번 더 확인하기 위한 보조 체크리스트다.)

```text
[x] Django 웹 정상
[x] REST API GET
[x] REST API POST
[x] Android 시작
[x] Android 동기화
[x] RecyclerView
[x] Upload
[x] 상세 화면
[x] 이미지 확대
[x] 검색
[x] 정렬
[x] 즐겨찾기
[x] 즐겨찾기 유지
[x] 오프라인 캐시
[x] WorkManager
[x] Notification
[x] PythonAnywhere
[ ] GitHub (Repository URL 실제 접속은 사용자 확인 필요)
```

검색/정렬/즐겨찾기/오프라인 캐시는 로컬 서버 기준으로 구현 시점에 확인되었으며, PythonAnywhere
전환 이후 별도로 재확인하지는 않았다 (Phase 15 참고).

---

# 캡처 파일 관리 권장

Repository에 캡처 원본을 보관하고 싶다면 별도 폴더를 사용할 수 있다.

예:

```text
evidence/
├── 01_project_structure/
├── 02_api/
├── 03_client_sync/
├── 04_upload/
├── 05_pythonanywhere/
└── 06_extra_features/
```

단, 교수님이 별도 폴더 제출을 요구하지 않았다면 최종적으로 보고서에 이미지가 정상 삽입되어 있는 것이 가장 중요하다.

이 프로젝트는 실제로 캡처 원본을 Repository 밖 별도 폴더에 보관하는 방식을 선택했다.

---

# 최종 제출 전

```text
[ ] 보고서의 모든 이미지가 문서 안에 포함되어 있음
[ ] 깨진 외부 이미지 링크 없음
[ ] 학번/이름 작성
[ ] 요구사항 완료 여부 표시
[ ] 관련 증빙 위치 확인
[ ] GitHub URL 정상 접속
[ ] Repository에 민감정보 없음
[ ] 불필요한 build/venv 파일 없음
```

위 항목은 보고서 문서(.docx) 작성/제출 단계의 작업이며, Claude Code가 대신 확인할 수 없으므로
모두 사용자가 직접 점검해야 한다.
