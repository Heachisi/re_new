<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <link rel="stylesheet" href="${contextPath}/css/mainHeader.css">
 <link rel="stylesheet" href="${contextPath}/css/common.css">
</head>
<body>
	<header id="topSection">
        <div>
            <div id="buttonContainer">
                <div id="authButtons">
                    <button id="loginButton"><a href="#">로그인</a></button>
                    <button id="signupButton"><a href="#">회원가입</a></button>
                    <!-- <button id="logoutBuuton">로그아웃</button> -->
                </div>
                <a href="#">
                    <img src="#" id="logo">
                </a>
            
            <!-- 검색창 -->
            <div id="search">
                <input id="seacrhInput" placeholder="검색어를 입력하세요"/>
                <button id="searchButton">검색</button>
            </div>
        </div>
            <!-- 메뉴바 -->
            <nav id="navbar">
                <div><a href="#">중고거래</a></div>
                <div><a href="#">&nbsp;&nbsp;&nbsp;나눔&nbsp;</a></div>
                <div><a href="#">커뮤니티</a></div>
                <div><a href="#">고객센터</a></div>
                <div><a href="#">마이페이지</a></div>
            </nav>
        </div>
	</header>
</body>
</html>