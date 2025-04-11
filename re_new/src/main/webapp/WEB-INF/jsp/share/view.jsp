<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시판 상세</title>
    <script src="/js/jquery-3.7.1.min.js?ver=1"></script>
    <script src="/js/common.js?ver=1.1"></script>
    <link rel="stylesheet" href="/css/shareview.css?ver=2">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
</head>
<body>
<div class="boardArea">
<form id="boardForm">
<div class="title">
	  ${share.title} 
	  </div>
	  <div class="btnArea">
	  <div class="writer">
	  ${share.createId}
	  </div>
					<div class="update">
						<a href="/share/update.do?id=${share.shareId}
							class="updatae">수정</a>
					</div>
					<div id="delete">
						<a href="#" id="delete">삭제</a>
					</div>
	   </div>
	  <div id="createDate">
	  ${fn:substring(share.createDt, 0,10)}
	  </div> 
	  <input type="hidden" id="shareId" value="${share.shareId}" />
	  <input type="hidden" id="updateId" value="${sessionScope.user.userId}" />
	  <div class="content">
	${share.content}
	</div>
	 
	 
	<c:if test="${not empty sessionScope.user.userId}">
		<div class="h5">
			<h4>댓글</h4>
				<textarea class="commentContent" id="commentContent" rows="4"
					placeholder="댓글을 입력하세요"></textarea>
				<br />
				<div class="commentCreate">
					<button type="button" class="commentCreateBtn"
						id="commentCreateBtn" onclick="addComment()">댓글 작성</button>
				</div>
		</div>
		
	</c:if>
	<c:if test="${not empty share.comments}">
		<div class="h4">
			<!-- ${share.comments} 값을 comments라는 이름으로 request에 저장해 JSP에서 사용할 수 있게 해줌 -->
			<c:set var="comments" value="${share.comments}" scope="request"/>
			<jsp:include page="commentItem.jsp">
				<jsp:param name="commentId" value="0" /> 
			</jsp:include>
		</div>
	</c:if>


	</div>
	<script>
	function addComment(parentId) {
		let content = parentId && parentId != 0 ? 
				$('#replyContent_'+parentId).val().trim():
				$('#commentContent').val().trim();
				
			if (!content) {
		        alert("댓글 내용을 입력해 주세요.");
		        return;
		    }	
			
		if (content.length > 500) {
			alert("댓글은 500자 이하로 작성해 주세요.")
			return;
		}
		
		let shareId = ${share.shareId};
		let createId = "${sessionScope.user.userId}";
		
		ajaxRequest(
			'/share/comment/create.do',
			{
				content: content,
				shareId: shareId,
				createId: createId,
				parentCommentId: parentId || 0
			},
			function(response) {
				if(response.success) {
					alert(response.message);
					location.reload();
				} else {
					alert(response.message);
				}
			}
		);
	}
	
	//댓글 수정 폼 토글
	function toggleEditComment(commentId) {
		let editForm = $('#editForm_'+commentId);
		let content = $('#commentContent_'+commentId).text().trim();
		
		if (editForm.is(':visible')) {
	         editForm.hide();
	    	} else {
			editForm.show();
			$('#editContent_'+commentId).val(content);
		}
	}
	function editComment(commentId) {
		
		let content = $('#editContent_'+commentId).val().trim();
		
		if (!content) {
			alert("댓글은 내용을 입력해 주세요.");
			return;
		}
		if (content.length > 500) {
			alert("댓글은 500자 이하로 작성해 주세요.");
			return;
		}
		
		let shareId = ${share.shareId};
		let updateId = "${sessionScope.user.userId}";
		
		ajaxRequest(
				'/share/comment/update.do',
				{
					commentId: commentId,
					content: content,
					shareId: shareId,
					updateId: updateId
				},
				function(response) {
					if(response.success) {
						alert(response.message);
						location.reload();
					} else {
						alert(response.message);
					}
				}
			);
	}
	function deleteComment(commentId) {
		if (confirm('이 댓글을 삭제하시겠습니까?')) {
			let updateId = "${sessionScope.user.userId}";
			ajaxRequest(
					'/share/comment/delete.do',
					{
						commentId: commentId,
						updateId: updateId
					},
					function(response) {
						if(response.success) {
							alert(response.message);
							location.reload();
						} else {
							alert(response.message);
						}
					}
				);
		} 
	}
	function toggleReplyComment(commentId) {
		
		let replyForm = $('#replyForm_'+commentId); 
		if (replyForm.is(":visible")) {
			replyForm.hide(); 
		} else {
			replyForm.show();
		}
		
	}
	$(document).ready(function(){
		$("#delete").click(function (event) {
			event.preventDefault(); // 기본 제출 방지
			ajaxRequest(
					"/share/delete.do",
					{ 
						boardId: $("#shareId").val(),
					 	updateId: $("#updateId").val()
					},
					function(response){
	       				if(response.success){
	    					alert("게시물을 삭제하였습니다. 게시판 목록으로 이동합니다.");
	       					window.location.href="/share/list.do";
	       				}else{
	       					alert("게시글 삭제를 실패하였습니다."+response.message);
	       				}
					}
				);
		});
		
	});
    </script>
</body>
</html>
