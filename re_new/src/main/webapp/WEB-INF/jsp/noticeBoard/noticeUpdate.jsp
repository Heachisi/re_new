<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <script src="/js/jquery-3.7.1.min.js?ver=1"></script>
    <script src="/js/tinymce/tinymce.min.js?ver=1"></script>
    <script src="/js/edit.js?ver=1"></script>
    <script src="/js/common.js?ver=1.12"></script>
    <link rel="stylesheet" href="/css/bulletinUpdate.css?ver=1">
	<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
    <script>
        $(document).ready(function () {
        	/* tinymce.init({
                selector: '#content',
                height: 500,
                width:800,
                menubar: false,
                plugins: 'lists image',
                toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | image'
            });  */
        	
        	let edit = editInit("content",500,800);
        	
        	let fileObj = setupFileUploadUpdate({
                dropZone: '#dropZone',
                fileInput: '#files',
                newFileList: '#newFileList',
                existingFileList: '#existingFileList',
                remainingFileIds: '#remainingFileIds',
                maxFileSize: 10 * 1024 * 1024 // 10MB 제한
            });
        	
			
            $("#updateBtn").click(function (event) {
                event.preventDefault(); // 기본 제출 방지

               	let title =$("#title").val().trim();
               	let content = tinymce.get('content').getContent({format: 'text'});
               
               	
               	if(!validationUtil.isEmpty(title)){
               		alert("제목을 입력해주세요.");
               		$("#title").focus();	
               		return;
               	}
            	if(!validationUtil.maxLength(title, 100)){
               		alert("제목은 최대 100자까지 입력할 수 있습니다.");
               		$("#title").focus();	
               		return;
               	}
            	if(!validationUtil.isEmpty(content)){
               		alert("내용을 입력해주세요.");
               		$("#content").focus();	
               		return;
               	}
            	if(!validationUtil.maxLength(content, 2000)){
               		alert("내용은 최대 2000자까지 입력할 수 있습니다.");
               		$("#content").focus();	
               		return;
               	}                           	
  				
            	let formData= new FormData();
            	formData.append("boardId",$("#boardId").val());
            	formData.append("title", title);
            	formData.append("content", tinymce.get('content').getContent());
            	formData.append("updateId", $("#updateId").val().trim());
            	formData.append("viewCount",0);
            	
            	let uploadedFiles = fileObj.getUploadedFiles();
            	
            	for(let i = 0; i < uploadedFiles.length; i++){
            		formData.append("files", uploadedFiles[i]);
            	}
            	
            	fileObj.updateRemainingFileIds();
            	formData.append("remainingFileIds", $('#remainingFileIds').val());
            	
           		ajaxRequestFile(
           			"/noticeboard/noticeUpdate.do",
           			formData,
           			/* $("#boardUpdateForm").serialize(), */
           			function(response){
           				if(response.success){
        					alert("게시물을 수정하였습니다. 게시판 목록으로 이동합니다.");
           					window.location.href="/noticeboard/noticeList.do";
           				}else{
           					alert("게시글 수정에 실패하였습니다."+response.message);
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
	<div class="updateForm">
	<form id="boardUpdateForm">
		<div class="titleArea">
		<label for="title"></label> 
		<input type="text" class="title" id="title" name="title" maxlength="100" placeholder="제목 입력"  value="${board.title}" required /> 
		</div>
		<div class="contentContainer">
		<label for="content"></label>
		<textarea class="content" id="content" name="content">${board.content}</textarea>
		</div>
		<input type="hidden" id="remainingFileIds" name="remainingFileIds" value="">
		<input type="hidden" id="updateId" name="updateId" value="${sessionScope.user.userId}" /> <br /> 
		<input type="hidden" id="viewCount" name="viewCount" value="${board.viewCount}" />
		<input type="hidden" id="boardId" name="boardId" value="${board.boardId}" />
	</form>
	<!--이미 업로드 된 파일 목록-->
	<%-- <c:if test="${not empty board.postFiles}">
		<ul id="existingFileList">
		<c:forEach var="file" items="${board.postFiles}">
			<li>
			${file.fileName}
			<button name="removeBtn" data-file="${file.fileId}">제거</button>
			<a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
			</li>
			</c:forEach>
		</ul>
		</c:if>
		
		<!-- 드래그 앤 드롭 파일 업로드 영역 -->
		<div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
		<input type="file" id="files" name="files" multiple style="display: none;">
		<!-- 새로 업로드된 파일 목록 -->
		<ul id="newFileList"></ul> --%>
		
	</div>
	<div class="btnArea">
		<button type="button" class="updateBtn" id="updateBtn">수정</button>
	</div>
	</c:otherwise>
	</c:choose>
</body>
</html>