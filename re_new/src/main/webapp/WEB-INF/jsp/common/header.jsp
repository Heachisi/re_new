<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/mainHeader.css?var=3">
<link rel="stylesheet" href="/css/common.css">


<script>
    	$(document).ready(function() {
    		$("#searchButton").click(function() {
    			let searchText = $("#searchText").val();
    			let startDate = $("#startDate").val();
    			let endDate = $("#endDate").val();
    			let minprice = $("#minprice").val();
    			let maxprice = $("#maxprice").val();
    			
            	window.location.href = "/product/list.do?"+
            	"searchText=" + searchText + "&" +
                "viewcategory=" + null + "&" +
                "page=" + 1 + "&" +
                "size=12";
    		});
    	});
    </script>
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
						<div name="id" class="showId">
							${sessionScope.user.userId}
						</div>
						<button type="submit" class="logoutBtn">로그아웃</button>
					</form>
					<div class="logoSearchContainer">
					<div class="logoContainer">
						<a href="/user/main.do"> <img src="/img/logo.png" class="logo"></a>
					</div>
					<div class="search" id="search">
						<!-- 검색창 -->
						<input class="searchText" id="searchText" 
							placeholder="검색어를 입력하세요" />
						<button class="searchButton" id="searchButton">검색</button>
					</div>
					</div>
				</div>
				<nav class="navbar">
					<!-- 메뉴바 -->
					<ul class="menu">
						<li class="menuItem"><a href="/product/list.do"">중고거래</a>
							<ul class="submenu">
								<li><a href="/product/list.do">전체</a></li>
								<li><a href="/product/list.do?category=패션">패션</a></li>
								<li><a href="/product/list.do?category=가전제품">가전제품</a></li>
								<li><a href="/product/list.do?category=전자제품">전자제품</a></li>
								<li><a href="/product/list.do?category=인테리어">인테리어</a></li>
								<li><a href="/product/list.do?category=유아동">유아동</a></li>
								<li><a href="/product/list.do?category=리빙">리빙</a></li>
								<li><a href="/product/list.do?category=기타">기타</a></li>
							</ul>
						</li>
						<li class="menuItem">나눔</li>
						<li class="menuItem">커뮤니티
							<ul class="submenu">
								<li><a href="/bulletinboard/bulletinList.do">리뉴 커뮤니티</a></li>
								<li><a href="/findboard/findList.do">구매글 게시판</a></li>
							</ul>
						</li>
						<li class="menuItem">고객센터
							<ul class="submenu">
								<li><a href="/askboard/askList.do">문의게시판</a></li>
								<li><a href="/noticeboard/noticeList.do">공지사항</a></li>
							</ul>
						</li>
						<li  class="menuItem" id="myPage">마이페이지
							<ul class="submenu">
								<li><a href="/product/create.do">제품관리</a></li>
								<li><a href="/product/mylist.do">내 물건 목록</a></li>
								<li><a href="/user/updateUser.do">회원정보 수정</a></li>
							</ul>
						</li>
						<li class="menuItem" id="adminPage">관리
							<ul class="submenu">
								<li><a href="/user/adminChart.do">통계</a></li>
								<li><a href="/user/adminUserManage.do">유저관리</a></li>
								<li><a href="/user/updateUser.do">정보수정</a></li>
							</ul>
						</li>	
				</nav>
				</div>
			</header>
			<script>
				$(document).ready(function() {
					ajaxRequest(
					        "/user/getUserRole.do",
					        {},
					        function(response) {
					            console.log("서버 응답:", response); 
					            if (response.adminYn === "Y") {
					          
                        			$("#adminPage").show();
					                $("#myPage").hide();
					            } else {
					                $("#adminPage").hide();
					                $("#myPage").show();
					            } 
					        }
				          
					   );

					//로그아웃 폼에 섬밋이벤트시 작동
					$("#logoutForm").submit(function(event) {
						event.preventDefault(); // 기본 폼 제출 방지

						$.ajax({
							url : '/user/logout.do', // 로그인 요청 URL
							type : 'POST',
							data : $(this).serialize(), // 폼 데이터 직렬화
							dataType : 'json',
							success : function(response) {
								// 응답 처리
								if (response.success) {
									alert("로그아웃에 성공하셨습니다.");
									window.location.href = '/user/login.do'; // 로그인 성공 후 메인 페이지로 이동
								} else {
									alert("로그아웃에 실패하셨습니다.");
								}
							},
						});
					});
				});
			</script>
		</c:otherwise>
	</c:choose>
</body>
</html>