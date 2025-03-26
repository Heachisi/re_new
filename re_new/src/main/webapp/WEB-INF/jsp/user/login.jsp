<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 화면</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
</head>
<body>
	<form method="get" id="loginForm">
		id : <input type="text" name="id"><br/>
		비밀번호 : <input type="password" name="pass">
		<button type="submit">로그인</button>
	</form>
	<a href="/user/join.do">회원가입 페이지로 이동</a>
	
	<script>
	$(document).ready(function() {
		//로긴 폼에 섬밋이벤트시 작동
	    $("#loginForm").submit(function(event) {
	        event.preventDefault(); // 기본 폼 제출 방지

            
	        $.ajax({
	            url: '/user/loginCheck.do', // 로그인 요청 URL
	            type: 'POST',
	            data: $(this).serialize(), // 폼 데이터 직렬화
	            dataType: 'json',
	            success: function(response) {
	            	console.log(response);
	                // 응답 처리
	                if (response.success) {
	                    alert("로그인 성공하셨습니다.");
	                    window.location.href ='/user/main.do'; // 로그인 성공 후 메인 페이지로 이동
	                } else {
	                	alert("로그인에 실패하셨습니다.");
	                }
	            },
	            error: function() {
	            	alert("통신 실패");
	        
	            }
	        });
	    });
	});
    </script>
	
</body>
</html>