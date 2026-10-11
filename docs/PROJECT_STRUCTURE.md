# PROJECT_STRUCTURE.md

# 프로젝트 파일/폴더 구조

이 문서는 파일을 어느 폴더에 만들어야 하는지 정리한다.

문서에 나온 이름은 **권장 구조**이며, 실제 프로젝트가 이미 다른 구조를 사용하면 기존 구조와 일관되게 배치한다.

---

# 1. Repository Root

> 아래는 2026-10-11 기준 실제 repository 구조다 (venv/, __pycache__/, build/, .idea/ 등
> gitignore 대상과 미디어/빌드 산출물은 생략).

```text
Mobile-Webservice/
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
├── tools/
│   ├── socket_server.py            # Phase 6: Socket Server 요청 검증용
│   └── captured_requests/
│       └── request_20261008_173639.txt
│
├── PhotoBlogServer/
└── PhotoViewer/
```

`CLAUDE.md`는 Repository Root에 둔다.

상세 문서는 모두 `docs/`에 둔다. 수행 결과 보고서(.docx)는 제출 시 별도로 준비하며
Repository 구조 자체에는 포함하지 않았다.

---

# 2. Django Server

위치:

```text
MobileWebService/PhotoBlogServer/
```

실제 구조 (이 프로젝트는 강의자료 예시 이름 `mysite`/`blog`를 실제로도 그대로 사용한다):

```text
PhotoBlogServer/
├── manage.py
├── requirements.txt         # coverage 포함
├── .coveragerc              # ID 6 Code Coverage 측정 범위(blog 앱만) 설정
│
├── mysite/
│   ├── settings.py           # STATIC_ROOT 등 PythonAnywhere 배포용 설정 포함
│   ├── urls.py
│   ├── wsgi.py
│   └── asgi.py
│
└── blog/
    ├── models.py
    ├── serializers.py
    ├── views.py
    ├── urls.py
    ├── admin.py
    ├── forms.py
    ├── tests.py              # ID 6: APITestCase 기반 자동 테스트 13개
    ├── migrations/
    └── templates/blog/
```

## 중요

`mysite`, `blog`는 강의자료 예시 이름이지만, 이 프로젝트의 실제 Django project/app 이름이기도
하다. 즉 예시 이름을 그대로 실사용 이름으로 채택한 경우이며, 이름이 우연히 같다고 해서 코드를
확인하지 않고 예시처럼 가정해서는 안 된다 — 실제로는 매번 코드(`settings.py`의 `INSTALLED_APPS`
등)로 확인했다.

## gitignore 대상 (소스 구조와 구분)

다음은 Repository에는 없고 로컬/배포 환경에만 생성되는 파일이다.

```text
PhotoBlogServer/db.sqlite3        # 로컬 DB (클라우드는 별도로 migrate+createsuperuser 필요)
PhotoBlogServer/media/            # 업로드된 이미지
PhotoBlogServer/.coverage         # coverage 실행 결과
PhotoBlogServer/htmlcov/          # coverage html 리포트 (ID 6 캡처 대상)
PhotoBlogServer/staticfiles/      # collectstatic 결과물 (PythonAnywhere 배포용)
```

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

실제 package name을 먼저 확인한다. 실제 package: `com.example.photoviewer`.

실제 구조 (2026-10-11 기준):

```text
PhotoViewer/app/src/main/java/com/example/photoviewer/
├── MainActivity.java
├── UploadActivity.java
├── DetailActivity.java
├── ApiConfig.java            # 서버 주소(BASE_URL) 단일 관리
├── ImageAdapter.java         # RecyclerView Adapter (기존 구조 유지, adapter/ 하위로 옮기지 않음)
├── ZoomableImageView.java    # 기능 1: 이미지 Pinch Zoom
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

`ImageAdapter.java`는 MainActivity와 같은 package에 그대로 두었다 (기존에 정상 동작하던 구조를
이유 없이 바꾸지 않는다는 원칙에 따라 `adapter/` 하위로 옮기지 않음).

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

한 곳에서 관리한다: `PhotoViewer/app/src/main/java/com/example/photoviewer/ApiConfig.java`의
`BASE_URL` 상수 하나만 MainActivity/UploadActivity/PostSyncWorker가 공통으로 참조한다.

환경:

```text
Local Emulator:
http://10.0.2.2:8000

Local Browser:
http://127.0.0.1:8000

Cloud (현재 설정값):
https://hoynola.pythonanywhere.com
```

상세 내용은 `docs/ANDROID_FEATURES.md`의 "ApiConfig / 서버 주소 관리" 절 참고.

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

는 수업 제출 방식에 따라 필요할 수 있으므로 임의로 제외하지 않는다. 이 프로젝트에서는 실제로
두 항목 모두 `.gitignore`에 포함되어 있다 (업로드 이미지/로컬 DB를 Repository에 올리지 않는
방식을 선택함). 따라서 PythonAnywhere 등 새 환경에서는 `migrate` + `createsuperuser`를 별도로
수행해야 하며, 로컬에서 만든 테스트 데이터를 클라우드로 옮길 필요는 없다.

---

# 13. 강의자료 파일

강의자료 `ch05.md`를 Repository에서 Claude Code가 참고하도록 둘 경우:

```text
MobileWebService/docs/ch05.md
```

에 둔다.

Claude Code는 필요한 Phase에서만 해당 섹션을 읽는다.
