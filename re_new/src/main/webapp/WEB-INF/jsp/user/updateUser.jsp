<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>회원정보 수정</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/common.js"></script>
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
             	
                ajaxRequest(
                	"/user/update.do", 
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
	<h2>회원정보</h2>
	<form id="updateForm">
	  <label for="userId">아이디:</label>
	  ${sessionScope.user.userId}
	  <input type="hidden" id="userId" name="userId" maxlength="20" value="${sessionScope.user.userId}" />
	  <input type="hidden" id="updateId" name="updateId" maxlength="20" value="${sessionScope.user.userId}" />
	  <br/>
	  <label for="password">비밀번호:</label>
	  <input type="password"  id="password" name="password" maxlength="20" placeholder="비밀번호 입력"  value="" />
	  <br/>
	  <label for="email">이메일:</label>
	  <input type="email"  id="email" name="email" maxlength="100" placeholder="이메일 입력"  value="${sessionScope.user.email}" />
	  <br/>
	  <label for="birthdate">생년월일:</label>
	  <input type="date"  id="birthdate" name="birthdate" maxlength="15" value="${sessionScope.user.birthdate}" />
	  <br/>
	  <label for="gender">성별:</label>
	  <select id="gender" name="gender">
		    <option value="" ${sessionScope.user.gender == '' ? 'selected' : ''}>성별</option>
		    <option value="woman" ${sessionScope.user.gender == 'woman' ? 'selected' : ''}>여자</option>
		    <option value="man" ${sessionScope.user.gender == 'man' ? 'selected' : ''}>남자</option>
	  </select>

	  <button type="submit" id="updateBtn">수정하기</button>
	</form>
	  <button type="button" id="deleteBtn">회원탈퇴</button>
	<a href="/user/login.do">로그인 페이지로 이동</a>
</body>
</html>

