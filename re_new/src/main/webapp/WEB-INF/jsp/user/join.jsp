<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js?ver=1.3"></script>
<link rel="stylesheet" href="/css/join.css">
<script>
   let isUserIdChecked = false; // 중복 체크 완료 여부를 저장


   $(document).ready(function () {
        	
        	$("#checkUserIdBtn").click(function () {
        		 console.log("✅ 중복 체크 버튼 클릭됨!"); // ✅ 디버깅 출력
                let userId = $("#userId").val().trim(); // 입력된 아이디 값 가져오기

                if (userId === "") {
                    alert("아이디를 입력해주세요.");
                    $("#userId").focus();
                    return;
                }
                
                console.log("입력된 아이디:", userId);
                
                ajaxRequest(
                		"/user/checkUserId.do",
                		{ userId: userId }, 
                		function (response) {
		                    console.log("서버 응답:", response);
		
		                    if (response.exists) {
		                        $("#checkResult").text("이미 사용 중인 아이디입니다.").css("color", "red");
		                        isUserIdChecked = false;
		                    } else {
		                        $("#checkResult").text("사용 가능한 아이디입니다.").css("color", "green");
		                        isUserIdChecked = true;
		                    }
		
                        });
                });
          
                
            $("#joinForm").submit(function (event) {
                event.preventDefault(); // 기본 제출 방지
                
                if (!isUserIdChecked) {  
                    alert("아이디 중복 체크를 먼저 진행해주세요!");  
                    return;  
                }
                
                let userId = $("#userId").val().trim();
                let password = $("#password").val().trim();
                let email = $("#email").val().trim();
                let birthdate = $("#birthdate").val().trim(); 
                let gender = $("#gender").val(); 
                
                console.log("회원가입 요청 데이터:", { userId, password, email, birthdate, gender });

                
                
                if(!validationUtil.isEmpty(userId)){
                	alert("아이디를 입력해주세요")
                	$("#userId").focus();
                	return;
                }
                
                if(!validationUtil.maxLength(userId, 20)){
                	alert("아이디는 최대 20자까지 입력할 수 있습니다")
                	$("#userId").focus();
                	return;
                }
                
                if(!validationUtil.isEmpty(password)){
                	alert("비밀번호를 입력해주세요")
                	$("#password").focus();
                	return;
                }
                
                if(!validationUtil.maxLength(password,20)){
                	alert("비밀번호는 최대 20자까지 입력할 수 있습니다")
                	$("#password").focus();
                	return;
                }
                
                if(!validationUtil.isStrongPassword(password)){
                	alert("영문 + 숫자 + 특수문자 포함, 최소 8자 이상 입력해주세요")
                	$("#password").focus();
                	return;
                } 
                
                if(!validationUtil.isEmpty(email)){
                	alert("이메일을 입력해주세요")
                	$("#email").focus();
                	return;
                }
                
                if(!validationUtil.maxLength(email, 100)){
                	alert("이메일은 최대 100자까지 입력할 수 있습니다.")
                	$("#email").focus();
                	return;
                }
                
                if(!validationUtil.isEmail(email)){
                	alert("올바른 이메일 주소를 입력하세요.")
                	$("#email").focus();
                	return;
                }
                
              
            	   ajaxRequest(
            		       "/user/register.do",
            		       $("#joinForm").serialize(),
            		       function (response){
            		    	   console.log("회원가입 응답:", response); // ✅ 서버 응답 디버깅
            		    	   if(response.success){
            		    		   alert("회원가입 성공하셨습니다. "
            		    				   +"로그인 페이지로 이동합니다.");
            		    		   window.location.href =
            		    			   "/user/login.do";
            		    	   } else {
            		    		   alert("회원가입 실패하셨습니다.");
            		    	   }
            		       }
            	        );
                    
                });
          
          });
    </script>
</head>
<body>
	<div id="totalSignForm">
	
	<div id="loginForm">
	<h2>가입을 환영해요!</h2>
	<h2>리뉴를 즐겨볼까요?</h2>
	<img src="C:\Users\Admin\Downloads./logo.png">
	<a href="/user/login.do">로그인</a>
	</div>
	
	<div id="signupForm">
	<h2>회원가입</h2>
	<form id="joinForm">
	     <div id="id-container">
		<label for="userId">아이디&nbsp;&nbsp;<span id="checkResult" style="font-size: 15px;"></span></label>
		<input type="text" id="userId" name="userId" maxlength="20" placeholder="아이디 입력" required />
		<button type="button" id="checkUserIdBtn">중복 체크</button> 
        </div>
		 <br />
		<label for="password">비밀번호</label>		 
		<input type="password" id="password" name="password" placeholder="비밀번호 입력" required /> <br /> 
		<label for="email">이메일</label> 
		<input type="email" id="email" name="email" maxlength="100" placeholder="이메일 입력" required /> <br />
		<label for="birthdate">생년월일 </label> 
		<input type="date" id="birthdate" name="birthdate" maxlength="15" required /> <br />
		<label type="gender">성별</label>
		<select id="gender" name="gender">
		<option value="">성별</option>
		<option value="woman">여자</option>
		<option value="man">남자</option>
		</select>
		<button type="submit" id="registerBtn">가입하기</button>
	</form>
	</div>
	    
	
	</div>
</body>
</html>