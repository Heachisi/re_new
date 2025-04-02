<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시판 상세</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/common.js?"></script>
</head>
<body>
	<h2>회원정보</h2>
	<form id="joinForm">
	  
	  <label for="userId">아이디:</label>
	  ${sessionScope.user.userId}
	  <br/>
	  <label for="password">비밀번호:</label>
	  ${sessionScope.user.password}
	  <br/>
	  <label for="email">이메일:</label>
	  ${sessionScope.user.email}
	  <br/>
	</form>
	<a href="/user/main.do">메인 페이지로 이동</a>
</body>
</html>
