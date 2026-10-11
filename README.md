# Mobile-Webservice Photo Blog

Django REST Framework 기반의 Photo Blog Server와 Android PhotoViewer Client를 연동한 모바일 웹서비스 프로젝트입니다.

## Project Structure

```text
Mobile-Webservice/
├── PhotoBlogServer/   # Django + Django REST Framework Server
├── PhotoViewer/       # Android Client
├── tools/             # Socket Server 및 테스트 도구
├── docs/              # 프로젝트 문서
├── CLAUDE.md
└── README.md
```

## Server

### Tech Stack

- Python 3.10
- Django 4.2
- Django REST Framework
- Pillow
- SQLite
- PythonAnywhere

### Main Features

- 게시물 목록 및 상세 조회
- 게시물 작성 및 수정
- 이미지 업로드
- REST API 제공
- Android Client의 Multipart POST 처리
- 서버 측 작성자 자동 할당
- Localhost 및 PythonAnywhere 배포 환경 지원

### API

```text
GET  /api_root/Post/
POST /api_root/Post/
GET  /api_root/Post/<id>/
```

### Cloud Server

```text
https://hoynola.pythonanywhere.com
```

---

## Android Client

Android PhotoViewer는 Django REST API를 통해 게시물과 이미지를 동기화합니다.

### Main Features

- Django Server 게시물 동기화
- RecyclerView 기반 게시물 목록
- 서버 이미지 다운로드 및 표시
- 게시물 상세보기
- 이미지 Pinch Zoom
- Android에서 새 게시물 업로드
- 제목 및 본문 검색
- 최신순 / 오래된순 정렬
- Room 기반 즐겨찾기
- Room 기반 오프라인 캐시
- WorkManager 기반 백그라운드 동기화
- 신규 게시물 Notification

현재 Android Client의 기본 Server URL은 PythonAnywhere Cloud Server로 설정되어 있습니다.

```text
https://hoynola.pythonanywhere.com
```

---

## Additional Android Features

### 1. Post Detail & Image Zoom

게시물을 선택하면 DetailActivity에서 다음 정보를 확인할 수 있습니다.

- 제목
- 본문
- 작성 날짜
- 게시물 이미지

이미지는 Pinch Zoom을 지원하며 확대 및 축소할 수 있습니다.

### 2. Search & Sort

게시물 제목과 본문을 기준으로 실시간 검색할 수 있습니다.

정렬 기능:

- 최신순
- 오래된순

검색과 정렬 기능을 함께 사용할 수 있습니다.

### 3. Favorites

Room Database를 이용하여 게시물 즐겨찾기 기능을 제공합니다.

- 게시물별 즐겨찾기 설정
- 즐겨찾기 게시물만 필터링
- 앱을 종료한 후 다시 실행해도 즐겨찾기 상태 유지

### 4. Offline Cache

서버와 통신할 수 없는 상황에서도 마지막으로 동기화한 게시물을 확인할 수 있도록 Room 기반 캐시를 사용합니다.

서버 요청에 실패하면 저장된 게시물 데이터를 불러오며, 오프라인 상태에서도 검색, 정렬, 즐겨찾기 및 상세보기 기능을 사용할 수 있습니다.

### 5. Background Sync & Notification

WorkManager를 이용하여 서버의 신규 게시물을 주기적으로 확인합니다.

- PeriodicWorkRequest 사용
- NetworkType.CONNECTED 조건 적용
- 신규 게시물 감지
- Notification 표시
- 테스트를 위한 즉시 실행 OneTimeWorkRequest 지원

---

## Django Server Setup

### 1. Move to Server Directory

```bash
cd PhotoBlogServer
```

### 2. Create Virtual Environment

```bash
python3 -m venv venv
source venv/bin/activate
```

### 3. Install Dependencies

```bash
pip install -r requirements.txt
```

### 4. Apply Migrations

```bash
python manage.py migrate
```

### 5. Run Server

```bash
python manage.py runserver
```

Local Server:

```text
http://127.0.0.1:8000/
```

REST API:

```text
http://127.0.0.1:8000/api_root/Post/
```

Android Emulator에서 Local Server에 연결할 경우 Server 주소는 다음과 같습니다.

```text
http://10.0.2.2:8000
```

---

## Automated Regression Tests

Django API 및 Web View에 대한 자동 회귀 테스트를 제공합니다.

현재 API, Web View, Model의 주요 동작을 자동으로 검증합니다.

### Run Tests

```bash
cd PhotoBlogServer

coverage erase
coverage run manage.py test blog
coverage report -m
coverage html
```

### Current Test Result

```text
Ran 28 tests
OK
```

### Code Coverage

```text
TOTAL: 100%
96 statements
0 missing
```

Coverage 측정 범위는 Django `blog` 앱입니다.

HTML Coverage Report는 다음 명령으로 생성됩니다.

```bash
coverage html
```

생성 위치:

```text
PhotoBlogServer/htmlcov/index.html
```

---

## Regression Test Coverage

자동 테스트는 다음과 같은 주요 동작을 검증합니다.

### REST API

- 게시물 목록 조회
- JSON 응답 구조 검증
- 게시물 상세 조회
- 게시물 생성
- 이미지 업로드
- 서버 측 작성자 자동 지정
- 로그인 사용자의 작성자 지정
- 사용자 미존재 상황의 기본 작성자 생성
- 필수 필드 Validation
- 존재하지 않는 게시물 요청에 대한 404 처리

### Web Views

- 게시물 목록 페이지
- 게시물 상세 페이지
- 존재하지 않는 게시물 404 처리
- 로그인하지 않은 사용자의 작성/수정 접근 제한
- 로그인 사용자의 게시물 작성
- 이미지 포함 게시물 작성
- 게시물 수정

### Model

- `Post.publish()`
- `Post.__str__()`

---

## Socket Request Test

Android Client가 서버로 보내는 실제 HTTP Request를 확인하기 위한 Python Socket Server가 포함되어 있습니다.

```text
tools/socket_server.py
```

Android Client의 요청을 Socket Server에서 직접 수신하여 다음과 같은 실제 HTTP GET Request를 확인할 수 있습니다.

```http
GET /api_root/Post/ HTTP/1.1
Host: 10.0.2.2:9000
Connection: Keep-Alive
Accept-Encoding: gzip
```

---

## Cloud Deployment

Django Server는 PythonAnywhere에 배포되어 있습니다.

```text
https://hoynola.pythonanywhere.com
```

Cloud 환경에서 다음 기능의 정상 동작을 확인했습니다.

- Django Web Page
- Django REST API
- Android 게시물 동기화
- Cloud 이미지 다운로드
- 게시물 상세보기
- Android Multipart Upload
- 업로드 후 자동 재동기화
- WorkManager 백그라운드 동기화
- 신규 게시물 Notification

---

## Main Components

### Django

```text
PhotoBlogServer/
├── blog/
│   ├── models.py
│   ├── views.py
│   ├── serializers.py
│   ├── forms.py
│   ├── urls.py
│   └── tests.py
├── mysite/
├── templates/
├── manage.py
├── requirements.txt
└── .coveragerc
```

### Android

주요 Android 구성 요소:

```text
MainActivity
UploadActivity
DetailActivity
ImageAdapter
PostItem
ApiConfig
ZoomableImageView
FavoriteEntity
FavoriteDao
PostCacheEntity
PostCacheDao
AppDatabase
PostSyncWorker
```

---

## Repository Notes

다음 파일 및 디렉터리는 Git Repository에 포함하지 않습니다.

```text
venv/
db.sqlite3
media/
build/
.gradle/
local.properties
.coverage
htmlcov/
staticfiles/
```

Django Server와 Android Client는 하나의 Repository에서 함께 관리합니다.