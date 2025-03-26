<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>회원정보</title>
    
    <head> 
    <meta charset="UTF-8">
    <title>게시판 상세</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
	<script src="/js/common.js"></script>
	
</head>
<body>
	<h2>게시판</h2>
	<form id="boardForm">
	  
	  <label for="boardId">작성자:</label>
	  ${board.boardId}
	  <br/>
	  <label for="boardname">작성일:</label>
	  ${board.createDt}
	  <br/>
	  <label for="password">제목:</label>
	  ${board.title}
	  <br/>
	  <label for="email">내용: </label>
	  ${board.content}
	  <br/>
	</form>
	<c:if test="${not empty board.postFiles}">
	      <ul>
	         <c:forEach var="file" items="${board.postFiles}">
	             <li>
	                ${file.fileName}
	                <a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
	             </li>
	         </c:forEach>
	      </ul>
	</c:if>
	<!-- 댓글 작성 폼 -->
	<c:if test="${not empty sessionScope.user.userId}">
	    <h4>댓글 작성</h4>
	     <textarea id="commentContent" rows="4" placeholder="댓글을 입력하세요"></textarea>
	     <button type="button" id="commentCreateBtn" onclick="addComment()" >댓글 작성</button>
	</c:if>     
    <c:if test="${not empty board.comments}">
	    <h4>댓글 목록</h4><br/>
	    <c:set var="comments" value="${board.comments}" scope="request" />
	    <jsp:include page="commentItem.jsp">
	        <jsp:param name="commentId" value="0" />
	    </jsp:include>
    </c:if>

	
	<a href="/board/main.do">메인 페이지로 이동</a>
	<script>
	//댓글작성
	 function addComment(parentId) {
	    var content = parentId && parentId != 0 ? 
	        $('#replyContent_' + parentId).val().trim() : 
	        $('#commentContent').val().trim();

	    if (!content) {
	        alert("댓글 내용을 입력해 주세요.");
	        return;
	    }

	    if (content.length > 500) {
	        alert("댓글은 500자 이하로 작성해 주세요.");
	        return;
	    }

	    var boardId = '${board.boardId}';
	    var createId = '${sessionScope.user.userId}';
	    
	    ajaxRequest(
	    	    '/board/comment/create.do',
	    	    {
	    	        content: content,
	    	        boardId: boardId,
	    	        createId: createId,
	    	        parentCommentId: parentId || 0
	    	    },
	    	    function(response) {
	    	        if (response.success) {
	    	            alert(response.message);
	    	            location.reload();
	    	        } else {
	    	            alert(response.message);
	    	        }
	    	    }
	    	);
	     }
	
	// 댓글 수정 폼 토글
	 function toggleEditComment(commentId) {
	     let editForm = $('#editForm_' + commentId);
	     let content = $('#commentContent_' + commentId).text().trim();

	     if (editForm.is(':visible')) {
	         editForm.hide();
	     } else {
	         editForm.show();
	         $('#editContent_' + commentId).val(content);
	     }
	 }
	
	
	//댓글 수정
	 function editComment(commentId) {
		    let content = $('#editContent_' + commentId).val().trim();

		    if (!content) {
		        alert("댓글 내용을 입력해 주세요.");
		        return;
		    }

		    if (content.length > 500) {
		        alert("댓글을 500자 이내로 작성해 주세요.");
		        return;
		    }

		    let boardId = ${board.boardId};
		    let updateId = '${sessionScope.user.userId}';

		    ajaxRequest(
		        '/board/comment/update.do',
		        {
		            commentId: commentId,
		            content: content,
		            boardId: boardId,
		            updateId: updateId
		        },
		        function(response) {
		            if (response.success) {
		                alert(response.message);
		                location.reload();
		            } else {
		                alert(response.message);
		            }
		        }
		    );
		}
	
	// 댓글 삭제
	 function deleteComment(commentId) {
	     if (confirm('이 댓글을 삭제하시겠습니까?')) {
	         let updateId = '${sessionScope.user.userId}';
	         ajaxRequest(
	             '/board/comment/delete.do',
	             {
	                 commentId: commentId, 
	                 updateId: updateId  
	             },
	             function(response) {
	                 if (response.success) {
	                     alert(response.message); // 성공 메시지 표시
	                     location.reload();       // 페이지 새로고침
	                 } else {
	                     alert(response.message); // 실패 메시지 표시
	                 }
	             }
	         );
	     }
	 }
	
	// 대댓글 폼 토글
	 function toggleReplyComment(commentId) {
	     let replyForm = $('#replyForm_' + commentId);
	     if (replyForm.is(':visible')) {
	         replyForm.hide(); // 폼이 보이는 경우 숨김
	     } else {
	         replyForm.show(); // 폼이 숨겨진 경우 표시
	     }
	 }



	</script>

</body>
</html>
