<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>수정</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/edit.js"></script>
    <script src="/js/tinymce/tinymce.min.js?ver=1"></script>
    <script src="/js/common.js?ver=1.2"></script>
    <link rel="stylesheet" href="/css/create.css">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
    <script>
    
        $(document).ready(function () {
    
        	let photoUploader = photoUpload({
			    dropZone: '#photoDropZone',
			    fileInput: '#photoInput',
			    previewContainer: '#photoPreview'
			});
        	
        	$("#deleteBtn").click(function (event) {
   					ajaxRequest(
   	                    "/product/delete.do", // 사용자 관리 서블릿
   	                 	{ productId: $("#productId").val(),
  						  updateId: $("#updateId").val()		
  						},
   	                    function (response) {
   	                        if (response.success) {
   	                            alert("게시글 삭제 성공! 게시판 목록으로 이동합니다.");
   	                         window.location.href ="/product/list.do";
   	                        } else {
   	                            alert("게시글 삭제 실패: " + response.message);
   	                        }
   	                    }
	   				);
            });
        	$("#updateBtn").click(function (event) {
                event.preventDefault(); // 기본 제출 방지
   				let title = $("#title").val().trim(); 
   				let content = $("#content").val();
   				let price = $("#price").val().trim();
   				let category = $("#category").val();
   				let sellstatus = $("#sellstatus").val();
   		
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
   				
   				let formData = new FormData();
   				formData.append("productId",$("#productId").val());
   				formData.append("title",title);
   				formData.append("content",content);
   				formData.append("updateId",$("#updateId").val().trim());
   				formData.append("viewCount",0);
   				formData.append("price",price);
   				formData.append("category",category);
   				formData.append("sellstatus",sellstatus);
   				
   				
   				let photoFile = photoUploader.getImageFile();
   				  
   				if (photoFile) {
   				    formData.append("photo", photoFile);
   				}
   				
   				ajaxRequestFile(
                    "/product/update.do", // 사용자 관리 서블릿
                    formData,
                    function (response) {
                        if (response.success) {
                            alert("게시글 수정 성공! 게시판 목록으로 이동합니다.");
                            window.location.href ="/product/mylist.do";
                        } else {
                            alert("게시글 수정 실패: " + response.message);
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
	<div class="create">
		<form id="updateForm">
			<div id="photoDropZone">프로필 이미지를 드래그하거나 클릭하세요.</div>
			<input type="file" id="photoInput" accept="image/*" style="display: none;">
			<div id="photoPreview"></div>
			<label for="title">상품명</label>
			<input class="title" type="text"  id="title" name="title" maxlength="20" value="${product.title}" required/>
			<br/>
			<label for="price"><h3>금액</h3></label>
			<input class="price" type="text"  id="price" name="price" maxlength="20" value="${product.price}" placeholder="금액 입력" required/>
			<br/>
			<label for="category"><h3>카테고리 선택</h3></label>
				<select id="category" name="category">
			            <option value="">전체</option>
			            <option value="패션">패션</option>
			            <option value="가전제품">가전제품</option>
			            <option value="전자제품">전자제품</option>
			            <option value="인테리어">인테리어</option>
			            <option value="유아동">유아동</option>
			            <option value="리빙">리빙</option>
			            <option value="기타">기타</option>
			    </select> 
			    <select id="sellstatus" name="sellstatus" style="height:20px;">
			            <option value="">판매상태 선택</option>
			            <option value="0">판매중</option>
			            <option value="1">판매완료</option>
			    </select>
			<br/>
			<label for="content"><h3>상품설명</h3></label>
			<textarea class="content" rows="5" cols="40" id="content" name="content">${product.content}</textarea>
			<br/>
			<input type="hidden" id="remainingFileIds" value=""/>
			<input type="hidden" id="updateId" name="updateId" value="${sessionScope.user.userId}"/>
			<input type="hidden" id="viewCount" name="viewCount"value="${product.viewCount}"/>
			<input type="hidden" id="productId" name="productId"value="${product.productId}"/>
	
		</form>
	</div>
	<script>
		function validateForm() {
		  const category = document.getElementById("category").value;
		  if (!category) {
		    alert("카테고리를 선택해주세요.");
		    return false;
		  }
		  return true;
		}
	</script>
	<c:if test="${not empty product.postFiles}">
		<ul id="existingFileList">
			<c:forEach var="file" items="${product.postFiles}">
				<li>
					${file.fileName}
					<button name="removeBtn" data-file="${file.fileId}"}>제거</button>
					<a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	  <button type="button" id="updateBtn">게시글 수정</button>
	  <button type="button" id="deleteBtn">게시글 삭제</button>
	 
</c:otherwise>
</c:choose>
	
</body>
</html>
