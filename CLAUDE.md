# Mobile/WebService Project Instructions

이 프로젝트는 모바일/웹서비스 중간고사 대체 과제이다.

## 프로젝트 구성

- `PhotoBlogServer/`: Django Server
- `PhotoViewer/`: Android Client
- `docs/`: 과제 요구사항, 구현 계획, 기능 설계, 보고서 체크리스트

## 현재 상태 (2026-10-11 기준)

기본 과제 기능 + 추가 기능 5개(ID 14-1 검색, 14-2 정렬, 14-3 즐겨찾기, 14-4 오프라인 캐시, 14-5 백그라운드 동기화 및 알림) 구현과 PythonAnywhere 클라우드 배포까지 완료된 상태이며,
현재는 새 기능 개발 단계가 아니라 **최종 보고서 작성 및 제출 정리 단계**다.

- Cloud Server URL: `https://hoynola.pythonanywhere.com`
- Android `PhotoViewer/app/src/main/java/com/example/photoviewer/ApiConfig.java`의
  `BASE_URL`이 현재 위 Cloud 주소를 가리키고 있다 (로컬 테스트로 되돌리려면 이 상수 한 줄만
  `http://10.0.2.2:8000`으로 바꾸면 된다. MainActivity/UploadActivity/PostSyncWorker가 모두
  이 상수 하나만 참조하므로 다른 파일을 수정할 필요는 없다).
- Phase별 상세 구현 이력과 현재 완료 상태는 `docs/IMPLEMENTATION_PLAN.md`,
  남은 보고서/캡처 작업은 `docs/REPORT_CHECKLIST.md`를 참고한다.
- 이 단계 이후 작업은 주로 보고서 캡처 확인, 문서 정리, 회귀 테스트 수준이며,
  요청받지 않은 새 기능을 임의로 추가하지 않는다.

## 작업 전에 필요한 문서만 읽기

작업 목적에 따라 아래 문서만 읽는다.

- 과제 요구사항 확인: `docs/ASSIGNMENT.md`
- 전체 구현 순서 및 Phase: `docs/IMPLEMENTATION_PLAN.md`
- Android 추가 기능 5개: `docs/ANDROID_FEATURES.md`
- 프로젝트 파일/폴더 구조: `docs/PROJECT_STRUCTURE.md`
- 보고서 캡처 및 제출 확인: `docs/REPORT_CHECKLIST.md`
- 강의자료 원본/정리본이 있으면: `docs/ch05.md`

## 작업 원칙

1. 기존 프로젝트 구조와 코드를 먼저 분석한다.
2. 기존에 정상 동작하는 코드를 불필요하게 전면 수정하지 않는다.
3. 실제 Android package name, Django project/app name을 확인하고 작업한다.
4. 문서의 예시 이름을 실제 프로젝트 이름처럼 가정하지 않는다.
5. 강의자료에 있는 코드와 Claude가 새로 보완하는 코드를 구분한다.
6. 한 번에 전체 프로젝트를 구현하지 말고 Phase 단위로 진행한다.
7. 각 Phase가 끝나면 빌드/실행/동작 여부를 확인한다.
8. 현재 Phase에 오류가 있으면 다음 Phase로 넘어가지 않는다.
9. Token, 비밀번호, API Key 등 민감한 값은 GitHub에 커밋하지 않는다.
10. 기본 과제 기능을 먼저 완성한 뒤 추가 기능 5개를 구현한다.
11. 새 기능 때문에 기존 GET/POST/RecyclerView 기능이 깨지지 않도록 한다.
12. 작업 완료 시 수정/생성한 파일, 테스트 결과, 사용자가 직접 확인할 항목을 보고한다.
13. 보고서용 캡처가 필요한 시점이면 `docs/REPORT_CHECKLIST.md`를 확인하고 알려준다.

## Phase 작업 방법

사용자가 특정 Phase를 요청하면:

1. `docs/IMPLEMENTATION_PLAN.md`에서 해당 Phase만 확인한다.
2. 필요 시 `docs/PROJECT_STRUCTURE.md`를 확인한다.
3. 추가 기능 Phase라면 `docs/ANDROID_FEATURES.md`의 해당 기능만 추가로 읽는다.
4. 과제 요구사항 확인이 필요하면 `docs/ASSIGNMENT.md`를 읽는다.
5. 강의자료 코드가 필요하면 `docs/ch05.md`에서 해당 부분을 확인한다.

## 각 Phase 종료 보고 형식

```text
완료한 작업:
- ...

수정/생성한 파일:
- ...

테스트 결과:
- ...

사용자가 직접 확인할 것:
- ...

보고서용 캡처:
- ...

다음 단계:
- ...
```

## 하지 말아야 할 것

- 기존 프로젝트를 이유 없이 새 프로젝트로 다시 생성하지 않는다.
- package name 또는 Django project/app 이름을 임의로 바꾸지 않는다.
- 강의자료에 없는 값을 사실처럼 가정하지 않는다.
- 예시 Token을 실제 Token처럼 사용하지 않는다.
- 컴파일/실행 오류가 있는 상태에서 다음 기능을 계속 추가하지 않는다.
- 서버 주소를 여러 파일에 중복 하드코딩하지 않는다.
- 기본 기능과 추가 기능을 중복 계산하지 않는다.
- 사용자의 기존 파일을 임의로 삭제하지 않는다.
- `.git`, `db.sqlite3`, `media/` 등을 이유 없이 삭제하거나 ignore 처리하지 않는다.
