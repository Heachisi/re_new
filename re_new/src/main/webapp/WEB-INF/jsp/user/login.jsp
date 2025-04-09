<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 화면</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/login.css? var=2">
</head>
<body>
	<div class=totalSignForm>
	<div class=loginContainer>
		<form class="loginForm" id="loginForm">
			<h2>로그인</h2>
			<div class="idContainer">
			<label for="userId">아이디</label> 
			<input type="text" id="userId" name="userId" maxlength="20" placeholder="아이디 입력" required />
			</div>
			<div class="passwordContainer">
			<label for="password">비밀번호</label>		 
			<input type="password" id="password" name="password" placeholder="비밀번호 입력" required /> <br />
			</div>
			<button type="submit" id="loginBtn" class="loginBtn">로그인</button>
		</form>
	</div>
	<div class=signupForm>
		<h2>환영해요!</h2>
		<h2>리뉴는 처음이신가요?</h2>
		
		<div class="welcomeImgContainer">
		<img src="/img/welcome.gif" class="welcomeImg">
		</div>
		<a href="/user/join.do" class="signUpBtn">회원가입 페이지로 이동</a>
	</div>

</div>
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