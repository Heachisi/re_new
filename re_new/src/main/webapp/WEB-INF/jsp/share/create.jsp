<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>글쓰기</title>
    <script src="/js/jquery-3.7.1.min.js?ver=1"></script> 
    <script src="/js/edit.js?ver=1"></script>
    <script src="/js/tinymce/tinymce.min.js?ver=1.1"></script>
    <script src="/js/common.js?ver=1.1"></script> 
    <script>
    
    $(document).ready(function () {

    	//textarea 이쁘게 꾸며주려고 쓰는거
    	let edit = editInit("content");
    	// setupFileUpload() 일반적으로 파일 업로드 기능을 설정하고 관리
    	
    	$("#createForm").submit(function (event) {
            event.preventDefault(); // 기본 제출 방지
				let title = $("#title").val().trim();
				let content = tinymce.get('content').getContent({format: 'text'});
		
				if(!validationUtil.isEmpty(title)) {
					alert("제목을 입력해주세요.")
					$("#title").focus();
					return;
				}
				if(!validationUtil.maxLength(title,100)) {
					alert("제목은 최대 100자 까지만 입력가능합니다.")
					$("#title").focus();
					return;
				}
				if(!validationUtil.isEmpty(content)) {
					alert("내용을 입력해주세요.")
					$("#content").focus();
					return;
				}
				if(!validationUtil.maxLength(content,2000)) {
					alert("내용은 최대 2000자 까지만 입력가능합니다.")
					$("#content").focus();
					return;
				}
				//FormData : 자바스크립트에서 폼 데이터와 파일을 서버로 전송할 수 있게 해주는 객체
				let formData = new FormData();
				formData.append("title",title);
				formData.append("content",tinymce.get('content').getContent());
				formData.append("createId",$("#createId").val().trim());
				formData.append("viewcount",0);

				ajaxRequestFile(
	                    "/share/create.do", // 사용자 관리 서블릿
	                 	formData,
	                    function (response) {
	                        if (response.success) {
	                            alert("게시글 생성 성공! 게시판 목록으로 이동합니다.");
	                            window.location.href ="/share/list.do";
	                        } else {
	                            alert("게시글 생성 실패: " + response.message);
	                        }
	                    }
   				); 
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
<h2>게시글 작성</h2>
<form id="createForm">
  <label for="title">제목:</label>
  <input type="text"  id="title" name="title" maxlength="100" placeholder="제목 입력" required/>
  <br/>
  <label for="content">내용:</label>
  <textarea rows="5" cols="40" id="content" name="content"></textarea>
  <br/>
  
  
  <input type="hidden" id="createId" name="createId" value="${sessionScope.user.userId}"/>
  <input type="hidden" id="viewcount" name="viewcount"value="0">
  <button type="submit" id="registerBtn">글쓰기</button>
</form>
</c:otherwise>
</c:choose>
</body>
</html>
