# PROJECT_STRUCTURE.md

# 프로젝트 파일/폴더 구조

이 문서는 파일을 어느 폴더에 만들어야 하는지 정리한다.

문서에 나온 이름은 **권장 구조**이며, 실제 프로젝트가 이미 다른 구조를 사용하면 기존 구조와 일관되게 배치한다.

---

# 1. Repository Root

```text
MobileWebService/
├── CLAUDE.md
├── .gitignore
│
├── docs/
│   ├── ASSIGNMENT.md
│   ├── IMPLEMENTATION_PLAN.md
│   ├── ANDROID_FEATURES.md
│   ├── PROJECT_STRUCTURE.md
│   ├── REPORT_CHECKLIST.md
│   └── ch05.md
│
├── PhotoBlogServer/
├── PhotoViewer/
└── 모바일/웹서비스 프로젝트 공통평가 01_수행 결과 보고서.docx
```

`CLAUDE.md`는 Repository Root에 둔다.

상세 문서는 모두 `docs/`에 둔다.

---

# 2. Django Server

위치:

```text
MobileWebService/PhotoBlogServer/
```

새 Django 프로젝트를 만드는 경우 예:

```text
PhotoBlogServer/
├── manage.py
├── requirements.txt
├── mysite/                 # 예시. 실제 이름 우선
│   ├── settings.py
│   ├── urls.py
│   ├── wsgi.py
│   └── ...
│
└── blog/
    ├── models.py
    ├── serializers.py
    ├── views.py
    ├── urls.py
    └── ...
```

## 중요

`mysite`, `blog`는 강의자료 예시 이름이다.

실제 프로젝트 이름이 다르면 강제로 변경하지 않는다.

---

# 3. Android Client

위치:

```text
MobileWebService/PhotoViewer/
```

강의자료가 Java 기준이므로 새 프로젝트를 생성할 경우 **Java + Views 기반** 구성을 권장한다.

기본 구조 예:

```text
PhotoViewer/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/<실제패키지>/
│       └── res/
├── gradle/
├── gradlew
├── settings.gradle 또는 settings.gradle.kts
└── ...
```

---

# 4. Android 기본 파일

## Manifest

```text
PhotoViewer/app/src/main/AndroidManifest.xml
```

역할:

- INTERNET 권한
- Activity 등록
- network security 설정
- 필요 시 알림 권한

---

## Network Security

```text
PhotoViewer/app/src/main/res/xml/network_security_config.xml
```

로컬 HTTP 테스트용.

---

## Main Layout

```text
PhotoViewer/app/src/main/res/layout/activity_main.xml
```

기본 UI:

```text
동기화
새로운 이미지 게시
상태 TextView
RecyclerView
```

---

## RecyclerView Item

```text
PhotoViewer/app/src/main/res/layout/item_image.xml
```

강의자료 기본 버전은 ImageView 중심.

추가 기능 이후:

```text
ImageView
제목
날짜
즐겨찾기 버튼
```

등으로 확장 가능.

---

# 5. Java 권장 구조

실제 package name을 먼저 확인한다.

예:

```text
PhotoViewer/app/src/main/java/<현재패키지>/
├── MainActivity.java
├── UploadActivity.java
├── DetailActivity.java
│
├── adapter/
│   └── ImageAdapter.java
│
├── model/
│   └── PostItem.java
│
├── database/
│   ├── AppDatabase.java
│   ├── FavoriteDao.java
│   ├── FavoriteEntity.java
│   ├── PostCacheDao.java
│   └── PostCacheEntity.java
│
└── worker/
    └── PostSyncWorker.java
```

기존 프로젝트에 `ImageAdapter.java`가 MainActivity와 같은 package에 있다면 기본 기능이 성공하기 전에는 굳이 이동하지 않는다.

구조 정리는 Phase 8 이후 필요할 때 진행한다.

---

# 6. Upload 파일

권장:

```text
java/<현재패키지>/UploadActivity.java
res/layout/activity_upload.xml
```

화면 구성:

```text
제목 EditText
본문 EditText
ImageView
이미지 선택
업로드
취소
ProgressBar
```

---

# 7. Detail 파일

권장:

```text
java/<현재패키지>/DetailActivity.java
res/layout/activity_detail.xml
```

---

# 8. Room 파일

권장:

```text
java/<현재패키지>/database/
```

Room 관련 Entity, DAO, Database를 배치한다.

---

# 9. WorkManager

권장:

```text
java/<현재패키지>/worker/PostSyncWorker.java
```

---

# 10. Server URL 관리

Server URL을 여러 Activity/Worker에 문자열로 반복해서 작성하지 않는다.

가능하면 한 곳에서 관리한다.

예:

```text
ApiConfig.java
```

또는 기존 프로젝트의 적절한 상수 구조 사용.

환경 예:

```text
Local Emulator:
http://10.0.2.2:8000

Local Browser:
http://127.0.0.1:8000

Cloud:
https://<실제계정>.pythonanywhere.com
```

---

# 11. Git 구조

최종 제출을 하나의 GitHub Repository로 할 경우 이상적인 형태:

```text
MobileWebService/.git/
MobileWebService/PhotoBlogServer/
MobileWebService/PhotoViewer/
```

즉 하위 프로젝트 안에 별도 `.git`이 있으면 중첩 Repository가 될 수 있다.

하지만 `.git`은 임의로 삭제하지 말고 먼저 상태를 확인한다.

확인 명령 예:

```bash
find . -name ".git" -type d
```

---

# 12. .gitignore 기본 예시

Android:

```gitignore
.gradle/
.idea/
local.properties
build/
**/build/
*.iml
```

Python:

```gitignore
venv/
.venv/
__pycache__/
*.pyc
.env
```

주의:

```text
db.sqlite3
media/
```

는 수업 제출 방식에 따라 필요할 수 있으므로 임의로 제외하지 않는다.

---

# 13. 강의자료 파일

강의자료 `ch05.md`를 Repository에서 Claude Code가 참고하도록 둘 경우:

```text
MobileWebService/docs/ch05.md
```

에 둔다.

Claude Code는 필요한 Phase에서만 해당 섹션을 읽는다.
