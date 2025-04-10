<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" href="/css/bulletinCommentItem.css?ver=1">

<c:if test="${not empty comments}">
	<c:forEach var="comment" items="${comments}">
		<c:if test="${comment.parentCommentId == param.commentId}">
			<div id="commentList" class="commentList">
				<div id="commentTitle" class="commentTitle">
					<strong>${comment.createId}</strong>
				</div>
				<div id="commentDateArea" class="commentDateArea">
				<div id="commentDate" class="commentDate">
				<!-- 날짜만 나오게 -->
					${fn:substring(comment.createDt, 0, 10)}
				</div>
				<c:if test="${not empty sessionScope.user.userId}">
				<c:if test="${sessionScope.user.userId == comment.createId}">
				<div id="commentUpdateArea" class="commentUpdateArea">
				<a  id="commentUpdate" class="commentUpdate"onclick="toggleEditComment(${comment.commentId})">수정</a>
				</div>
				<div id="commentDeleteArea" class="commentDeleteArea">
				<a id="commentDelete" class="commentDelete" onclick="deleteComment(${comment.commentId})">삭제</a> 
				</div>
				</c:if>
				</c:if>
				<c:if test="${not empty sessionScope.user.userId}">
				<c:if test="${sessionScope.user.userId != comment.createId}">
				<div id="replyCommentBtnArea" class="replyCommentBtnArea">
				<a id="replycommentBtn" class="replycommentBtn" onclick="toggleReplyComment(${comment.commentId})">답글쓰기</a>
				</div>
				
				</c:if>
				</c:if>
				</div>
				<div id="commentContent" class="commentContent">
					<p id="commentcontent_${comment.commentId}">${comment.content}</p>
				</div>
				
				<!--  댓글 수정 폼 -->
				<div id="editForm_${comment.commentId}" style="display:none;">
				<textarea class="updateComent" id="editContent_${comment.commentId}" rows="4">${comment.content}</textarea>
				<div id="updateCommentArea" class="updateCommentArea">
				<button type="button" onclick="editComment(${comment.commentId})">수정</button>
				<button type="button" onclick="toggleEditComment(${comment.commentId})">취소</button>
				</div>
				</div>
			
			<!-- 대댓글 작성 폼 -->
			<div id="replyForm">
				<div id="replyForm_${comment.commentId}" style="display:none; margin-left: 20px;">
					<textarea class="replyComment" id="replyContent_${comment.commentId}" rows="3" placeholder="답글을 입력하세요"></textarea>
					<div id="replyCommentArea" class="replyCommentArea">
					<button type="button" onclick="addComment(${comment.commentId})">작성</button>
					<button type="button" onclick="toggleReplyComment(${comment.commentId})">취소</button>
					</div>
				</div>
			</div>
			
				<jsp:include page="bulletinCommentItem.jsp">
					<jsp:param name="commentId" value="${comment.commentId}"/>
				</jsp:include>
			</div>
		</c:if>
	</c:forEach>
</c:if>
