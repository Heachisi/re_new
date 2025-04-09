<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/updateUser.css">
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<script>
        $(document).ready(function () {
            $("#updateForm").submit(function (event) {
                event.preventDefault(); // 기본 제출 방지
                
                let userId = $("#userId").val().trim();
                let password = $("#password").val().trim();
                let email = $("#email").val().trim();
                let birthdate = $("#birthdate").val().trim();
                let gender = $("#gender").val().trim();
                
                if (!validationUtil.maxLength(password, 20)) {
                    alert("비밀번호는 최대 20자까지 입력할 수 있습니다.");
                    $("#password").focus();
                    return;
                } 
                
                if (!validationUtil.isEmpty(email)) {
           			alert("이메일을 입력해주세요.")
                	$("#email").focus();
           			return;
                }
                
                if (!validationUtil.maxLength(email, 100)) {
                    alert("이메일은 최대 100자까지 입력할 수 있습니다.");
                    $("#email").focus();
                    return;
                }
                
             	// 이메일 형식 체크
                if (!validationUtil.isEmail(email)) {
                    alert("올바른 이메일 주소를 입력하세요.");
                    $("#email").focus();
                    return;
                }
                console.log("UserId:", userId);
                console.log("Password:", password);
                console.log("Email:", email);
                console.log("Birthdate:", birthdate);
                console.log("Gender:", gender);

                console.log("Serialized Data:", $("#updateForm").serialize());

             	
                ajaxRequest(
                	"/user/updateUser.do", 
                	$("#updateForm").serialize(), 
                	function (response) {
                		if (response.success) {
                            alert("회원정보 수정에 성공하셨습니다.");
                            window.location.href = 
                            	"/user/main.do";
                        } else {
                            alert("회원정보 수정에 실패하셨습니다.");
                        }
                	}
                );
                 
            });
            
            $("#deleteBtn").click(function (event) {
                event.preventDefault(); // 기본 제출 방지
                
                ajaxRequest(
                	"/user/delete.do", 
                	$("#updateForm").serialize(), 
                	function (response) {
                		if (response.success) {
                            alert("회원탈퇴에 성공하셨습니다.");
                            window.location.href = 
                            	"/user/login.do";
                        } else {
                            alert("회원탈퇴에 실패하셨습니다.");
                        }
                	}
                );
                 
            });
            
        });
    </script>
</head>
<body>
	<div class="bottomSection">
		<form id="updateForm" class="updateForm">
			<h2>회원정보 수정</h2>
			<div class="userId">
			<label for="userId">아이디</label> ${sessionScope.user.userId}
			</div>
			<div class="password">
			<label for="password">비밀번호 <span class="required">*</span></label> 
			<input type="password" id="password" name="password" maxlength="20"
				placeholder="비밀번호 3~12자리" value="" /> <br /> 
			</div>
			<div class="email">
			<label for="email">이메일 <span class="required">*</span></label>
			<input type="email" id="email" name="email" maxlength="100"
				placeholder="이메일 입력" value="${sessionScope.user.email}" /> <br />
				</div>
			<div class="birthGenderContainer">
				<div class="birtContainer">
				<label for="birthdate">생년월일</label>
				<input type="date" id="birthdate"  class="birthdate" name="birthdate" maxlength="15"
					value="${fn:substring(sessionScope.user.birthdate, 0, 10)}" /> <br />
				</div>
				<div class="genderContainer">
				<label for="gender" >성별</label>
				<select id="gender" class="gender" name="gender">
					<option value="성별"
						${sessionScope.user.gender == '' ? 'selected' : ''}>성별</option>
					<option value="woman"
						${sessionScope.user.gender == 'woman' ? 'selected' : ''}>여자</option>
					<option value="man"
						${sessionScope.user.gender == 'man' ? 'selected' : ''}>남자</option>
				</select>
				</div>
			</div>
		
			<button type="submit" id="updateBtn" class="updateBtn">저장</button>
			</br>
			<button type="button" id="deleteBtn" class="deleteBtn">회원탈퇴</button>
		</form>
	</div>
</body>
</html>

