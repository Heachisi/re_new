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
    <script src="/js/common.js?ver=1.1"></script>
    <link rel="stylesheet" href="/css/create.css">
    
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
    <script>
    
        $(document).ready(function () {

        	let photoUploader = photoUpload({
				    dropZone: '#photoDropZone',
				    fileInput: '#photoInput',
				    previewContainer: '#photoPreview'
				});
        	
        	$("#createForm").submit(function (event) {
                event.preventDefault(); // 기본 제출 방지
   				let title = $("#title").val().trim();
   				let content = $("#content").val();
   				let price = $("#price").val().trim();
   				let category = $("#category").val();
   		
   				
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
   				if (!validationUtil.isEmpty(price)) {
   				    alert("금액을 입력해주세요.");
   				    $("#price").focus();
   				    return;
   				}
   				if (!validationUtil.isNumeric(price)) {
   				    alert("금액은 숫자만 입력 가능합니다.");
   				    $("#price").focus();
   				    return;
   				}
   				if (parseInt(price) <= 0) {
   				    alert("금액은 0보다 큰 숫자여야 합니다.");
   				    $("#price").focus();
   				    return;
   				}
   				if (category === "") {
   				    alert("카테고리를 선택해주세요.");
   				    $("#category").focus();
   				    return;
   				}
   				//FormData : 자바스크립트에서 폼 데이터와 파일을 서버로 전송할 수 있게 해주는 객체
   				let formData = new FormData();
   				formData.append("title",title);
   				formData.append("content",content);
   				formData.append("createId",$("#createId").val().trim());
   				formData.append("viewCount",0);
   				formData.append("price",price);
   				formData.append("category",category);
   				
   				let photoFile = photoUploader.getImageFile();
   			    
   				if (photoFile) {
   				    formData.append("photo", photoFile);
   				}
   				
   				ajaxRequestFile(
   	                    "/product/create.do", // 사용자 관리 서블릿
   	                 	formData,
   	                    function (response) {
   	                        if (response.success) {
   	                            alert("게시글 생성 성공! 게시판 목록으로 이동합니다.");
   	                            window.location.href ="/product/list.do";
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
	<div class="create">
	<form id="createForm"> 
	<div id="photoDropZone">프로필 이미지를 드래그하거나 클릭하세요.</div>
	<input type="file" id="photoInput" accept="image/*" style="display: none;">
	<div id="photoPreview"></div>
	<label for="title"><h3>상품명</h3></label>
	<input class="title" type="text"  id="title" name="title" maxlength="20" placeholder="제목 입력" required/>
	<br/>
	<label for="price"><h3>금액</h3></label>
	<input class="price" type="text"  id="price" name="price" maxlength="20" placeholder="금액 입력" required/>
	<br/>
	<label for="category"><h3>카테고리 선택</h3></label>
	<select id="category" class="category" name="category">
            <option value="">전체</option>
            <option value="전자제품">패션</option>
            <option value="의류">가전제품</option>
            <option value="의류">전자제품</option>
            <option value="의류">인테리어</option>
            <option value="의류">유아동</option>
            <option value="의류">리빙</option>
            <option value="의류">기타</option>
    </select>
	<br/>
	<label for="content"><h3>상품설명</h3></label>
	<textarea class="content" rows="5" cols="40" id="content" name="content"></textarea>
	<br/>
	  
	  <input type="hidden" id="createId" name="createId" value="${sessionScope.user.userId}"/>
	  <input type="hidden" id="viewCount" name="viewCount"value="0">
	  <div class="btnContianer">
	  <button type="submit" id="registerBtn" class="registerBtn">글쓰기</button>
	  </div>
	</form>
	</div>
	
</body>
</html>
