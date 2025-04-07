<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="/css/mainHeader.css">
<link rel="stylesheet" href="/css/common.css">
</head>
<body>
	<c:choose>
		<c:when test="${empty sessionScope.user}">
			<c:redirect url="/user/login.do" />
		</c:when>
		<c:otherwise>
			<header class="topSection">
				<div class="buttonContainer">
					<form method="post" id="logoutForm" class="logoutForm">
						<input type="hidden" name="id" class="showId"
							value="${sessionScope.user.userId}">
						<button type="submit" class="logoutBtn">로그아웃</button>
					</form>
					<div class="logoContainer">
					<a href="#"> <img src="#" class="logo"></a>	
					</div>
					<div class="search" id="search"><!-- 검색창 -->
						<input class="searchInput" id="seacrhInput" placeholder="검색어를 입력하세요" />
						<button class="searchButton" id="searchButton">검색</button>
					</div>
				</div>
				<nav class="navbar"><!-- 메뉴바 -->
					<ul class="menu">
						<li class="menuItem">중고거래
							<ul class="submenu">
								<li>전체</li>
								<li>패션</li>
								<li>가전제품</li>
								<li>전자제품</li>
								<li>인테리어</li>
								<li>유아동</li>
								<li>리빙</li>
								<li>기타</li>
							</ul>
						</li>
						<li class="menuItem">나눔</li>
						<li class="menuItem">커뮤니티
							<ul class="submenu">
								<li><a href="/bulletinboard/bulletinList.do">리뉴 커뮤니티</a></li>
								<li>구매글 게시판</li>
							</ul>
						</li>
						<li class="menuItem">고객센터
							<ul class="submenu">
								<li>문의게시판</li>
								<li><a href="/noticeBoard/noticeList.do">공지사항</a></li>
							</ul>
						</li>
						<li class="menuItem">마이페이지
							<ul class="submenu">
								<li>제품관리</li>
								<li>내 물건 목록</li>
								<li><a href="/user/updateUser.do">회원정보 수정</a></li>
							</ul>
						</li>
					</ul>
				</nav>
				</div>
			</header>
			<script>
		        $(document).ready(function() {
		        	  
		        	//로그아웃 폼에 섬밋이벤트시 작동
		            $("#logoutForm").submit(function(event) {
		                event.preventDefault(); // 기본 폼 제출 방지
		
		                $.ajax({
		                    url: '/user/logout.do', // 로그인 요청 URL
		                    type: 'POST',
		                    data: $(this).serialize(), // 폼 데이터 직렬화
		                    dataType: 'json',
		                    success: function(response) {
		                        // 응답 처리
		                        if (response.success) {
		                            alert("로그아웃에 성공하셨습니다.");
		                            window.location.href = '/user/login.do'; // 로그인 성공 후 메인 페이지로 이동
		                        } else {
		                        	alert("로그아웃에 실패하셨습니다.");
		                        }
		                    },
		                    error: function() {
		                    	alert("통신 실패");
		                
		                    }
		                });
		            });
		        });
		    </script>
		</c:otherwise>
	</c:choose>
</body>
</html>