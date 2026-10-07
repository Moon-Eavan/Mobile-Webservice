# REPORT_CHECKLIST.md

# 수행 결과 보고서 / 캡처 체크리스트

구현 중 필요한 증빙 화면을 놓치지 않기 위한 문서다.

각 Phase가 끝날 때 현재 찍을 수 있는 화면을 바로 저장하는 것을 권장한다.

---

# 1. 제출 구조

```text
[ ] ROOT 폴더 구조 캡처
[ ] PhotoBlogServer 폴더 확인
[ ] PhotoViewer 폴더 확인
[ ] 수행 결과 보고서 파일 확인
[ ] GitHub Repository URL
```

---

# 2. 포토 블로그 / REST API

```text
[ ] 웹페이지 게시물 작성 화면
[ ] 게시 완료된 웹페이지
[ ] /api_root/Post/ 정상 응답
[ ] 터미널/API 요청 화면
[ ] JSON Viewer 검증 화면
```

JSON Viewer에서는 가능한 한 다음 데이터가 보이게 한다.

```text
title
text
date
image
```

---

# 3. Server / Client 연동

```text
[ ] Socket Server가 받은 Client Request
[ ] Server 브라우저 출력 화면
[ ] Android Client 시작 화면
[ ] 동기화 전 화면
[ ] Client 동기화 완료 화면
```

---

# 4. Upload

```text
[ ] Upload 입력 UI
[ ] 제목/내용 입력
[ ] 이미지 선택
[ ] 이미지 미리보기
[ ] 업로드 진행/성공 화면
[ ] Server 웹에서 새 게시물 확인
[ ] Android 재동기화 후 새 게시물 확인
```

---

# 5. PythonAnywhere

```text
[ ] PythonAnywhere Server 웹 화면
[ ] Cloud /api_root/Post/ 확인
[ ] Android Cloud Server 동기화
[ ] Cloud 이미지 정상 표시
```

가능하면 Upload도 클라우드 대상으로 테스트한다.

---

# 6. 추가 기능 1 - 상세 화면 + 이미지 확대

```text
[ ] 목록 화면
[ ] 게시물 선택
[ ] 상세 화면
[ ] 이미지 확대 화면
```

보고서 설명 예시:

```text
RecyclerView의 게시물을 선택하면 제목, 본문, 날짜, 이미지를
확인할 수 있는 상세 화면으로 이동하도록 구현하였다.
이미지는 확대하여 확인할 수 있다.
```

---

# 7. 추가 기능 2 - 검색 + 정렬

```text
[ ] 전체 목록
[ ] 검색어 입력
[ ] 검색 결과
[ ] 최신순
[ ] 오래된순
```

---

# 8. 추가 기능 3 - 즐겨찾기

```text
[ ] 즐겨찾기 전
[ ] 즐겨찾기 등록
[ ] 즐겨찾기 목록
[ ] 앱 종료 후 재실행
[ ] 즐겨찾기 유지 확인
```

---

# 9. 추가 기능 4 - 오프라인 캐시

```text
[ ] 온라인 정상 동기화
[ ] 네트워크 비활성화
[ ] 오프라인 안내 메시지
[ ] 저장된 게시물 표시
```

---

# 10. 추가 기능 5 - WorkManager + Notification

```text
[ ] 자동 동기화 테스트 실행
[ ] Server에 새로운 게시물 등록
[ ] Android Notification
[ ] Notification 제목/내용
[ ] Notification 클릭 후 앱 또는 상세 화면
```

---

# 11. 최종 기능 검사

```text
[ ] Django 웹 정상
[ ] REST API GET
[ ] REST API POST
[ ] Android 시작
[ ] Android 동기화
[ ] RecyclerView
[ ] Upload
[ ] 상세 화면
[ ] 이미지 확대
[ ] 검색
[ ] 정렬
[ ] 즐겨찾기
[ ] 즐겨찾기 유지
[ ] 오프라인 캐시
[ ] WorkManager
[ ] Notification
[ ] PythonAnywhere
[ ] GitHub
```

---

# 12. 캡처 파일 관리 권장

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

---

# 13. 최종 제출 전

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
