# re_new 웹 애플리케이션

Java 기반의 웹 애플리케이션으로, 중고거래 웹 플랫폼 입니다.

## 🚀 프로젝트 개요

RE-New Market

다시 새롭게 쓰는 물건을 만나는 장소

이 슬로건을 바탕으로 기획된 이번 프로젝트는
단순한 중고거래를 넘어서 나눔, 물건 찾기, 
커뮤니티 기반 거래 등 다양한 방식으로 사용하지 않는 
물건과 그것을 필요로 하는 사람을 연결하는 웹 플랫폼을 만들고자 기획되었습니다.

필요하지만 쉽게 찾기 어려운 물건을 찾고,
쓸모가 다한 물건도 누군가에게 가치를 줄 수 있는
따뜻한 연결의 공간을 제공하는 것이 
이 프로젝트의 목표입니다

## 대상 사용자

- **필요한 물건을 더 합리적인 가격으로 구하고자 하는 사람**
- **사용하지 않는 물건을 판매하여 자원을 회수하고 싶은 사람**
- **판매 중단된 물건을 구하고 싶은 사람**
- **가게를 폐업하며 재고를 빠르게 처리하고 싶은 소상공인**
- **커뮤니티를 통해 가을철 낙엽들을 모아 비료가 필요한 농가에 전달하는 등 상호 이익을 얻을 수 있는 연결망**

**다양한 사용자의 상황에 맞춰 이로운 거래 환경을 제공합니다.**


## 🛠️ 기술 스택

### Backend
- **Java 17**
- **Servlet API** (Jakarta Servlet 5.0.0)
- **MyBatis** 3.5.10 (SQL 매퍼)
- **Oracle Database** (JDBC 19.8.0.0)

### Frontend
- **JSP/JSTL**
- **JavaScript**
- **TinyMCE** (리치 텍스트 에디터)
- **jQuery 3.7.1**

### 빌드 도구
- **Gradle** (build.gradle 기반)

### 로깅
- **Log4J2**
- **log4jdbc-remix** (SQL 로깅)

## 📁 프로젝트 구조

```
re_new/
├── src/main/
│   ├── java/
│   │   └── controller/
│   │       ├── board/
│   │       │   ├── AskBoardController.java      # 문의사항 게시판
│   │       │   ├── BulletinBoardController.java # 커뮤니티 게시판
│   │       │   ├── FindBoardController.java     # 구매글 게시판
│   │       │   └── NoticeBoardController.java   # 공지사항 게시판
│   │       ├── product/
│   │       │   ├── ProductController.java         # 중고거래 게시판
│   │       ├── share/
│   │       │   ├── ShareController.java         # 나눔 게시판
│   │       └── file/           # 파일 처리 컨트롤러
│   │           └── FileController.java
│   └── webapp/
│       ├── WEB-INF/
│       │   └── jsp/            # JSP 뷰 파일들
│       │       ├── askBoard/   # 문의사항 게시판 페이지
│       │       ├── bulletin/   # 커뮤니티 게시판 페이지
│       │       ├── find/       # 구매글 페이지
│       │       ├── product/   #중고거래 게시판 페이지
│       │       ├── share/       # 나눔글 페이지
│       │       └── notice/     # 공지사항 페이지
│       └── js/                 # JavaScript 라이브러리
│           ├── common.js
│           ├── edit.js
│           ├── jquery-3.7.1.min.js
│           └── tinymce/        # 텍스트 에디터
├── build.gradle
└── .gitignore
```

## 🎯 주요 기능

### 게시판 기능
- **중고거래 게시판** (Product Board): 전체 상품 목록과 검색 기능
- **커뮤니티 게시판** (Bulletin Board): 일반 커뮤니티 게시판
- **구매글 게시판** (Find Board): 구매하기 원하는 물건에 대해 등록하는 게시판
- **나눔글 게시판** (Share Board): 나눔하고자 하는 물건을 등록하는 게시판
- **질문 게시판** (Ask Board): 질문과 답변 기능
- **공지사항** (Notice Board): 관리자 공지사항


### 파일 관리
- 파일 업로드/다운로드
- 게시판별 파일 첨부 기능
- Commons FileUpload 라이브러리 사용

### 에디터 기능
- TinyMCE 리치 텍스트 에디터
- 다양한 플러그인 지원 (코드, 이모티콘, 표 등)

## 🔧 설치 및 실행

### 사전 요구사항
- Java 17 이상
- Oracle Database
- Gradle

### 실행 방법

1. **프로젝트 클론**
   ```bash
   cd re_new-main/re_new
   ```

2. **데이터베이스 설정**
   - Oracle Database 연결 정보 설정
   - 테이블 생성

3. **빌드**
   ```bash
   ./gradlew build
   ```

4. **WAR 파일 배포**
   ```bash
   # build/dist/MyWebApp.war 파일이 생성됩니다
   ```

5. **웹 서버에 배포**
   - Tomcat 등의 서블릿 컨테이너에 WAR 파일 배포

---
