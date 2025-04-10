<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="/css/view.css?ver=4">
 <div class="contentArea">
<c:if test="${not empty comments}">
	<c:forEach var="comment" items="${comments}">
		<c:if test="${comment.parentCommentId == param.commentId}">
			<div class="commnetDetails" >
				<p>
					<strong>${comment.createId}</strong>
					${comment.createDt}
				</p>
				<p id="commentContent_${comment.commentId}">${comment.content}</p>
				<!--댓글 수정 폼 -->
			<div id="editForm_${comment.commentId}" style="display:none">
				<textarea class="texta"  id="editContent_${comment.commentId}" rows="4">${comment.content}</textarea>
				<br/>
				<button type="button" onclick="editComment(${comment.commentId})">수정</button>
				<button type="button" onclick="toggleEditComment(${comment.commentId})">취소</button>
			</div>
				<c:if test="${not empty sessionScope.user.userId}">
					<c:if test="${sessionScope.user.userId == comment.createId}">
						<br/>
						<button type="button" onclick="toggleEditComment(${comment.commentId})">수정</button>
						<button type="button" onclick="deleteComment(${comment.commentId})">삭제</button>	
					</c:if>
					<button type="button" onclick="toggleReplyComment(${comment.commentId})">답글</button>
				</c:if>
				<!--댓글 작성 폼 -->
			<div  id="replyForm_${comment.commentId}" >
				<textarea class="texta" id="replyContent_${comment.commentId}" rows="3" placeholder="답글을 입력하세요.."></textarea>
				<br/>
				<button type="button" onclick="addComment(${comment.commentId})">답글작성</button>
				<button type="button" onclick="toggleReplyComment(${comment.commentId})">취소</button>
			</div>
			<jsp:include page="commentItem.jsp">
				<jsp:param name="commentId" value="${comment.commentId}"/>
			</jsp:include>
			</div>
		</c:if>
	</c:forEach>
</c:if>
</div>