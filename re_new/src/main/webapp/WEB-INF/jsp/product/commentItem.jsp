<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="/css/view.css?ver=2">
 <div class="contentArea">
<c:if test="${not empty comments}">
	<c:forEach var="comment" items="${comments}">
		<c:if test="${comment.parentCommentId == param.commentId}">
			<div id="commentList" class="commentList">
				<div id="commentTitle" class="commentTitle">
					<strong>${comment.createId}</strong>
				</div>
					<div id="commentDateArea" class="commentDateArea">
				<div id="commentDate" class="commentDate">
				${comment.createDt}
				</div>
				
				<c:if test="${not empty sessionScope.user.userId}">
                    <c:if test="${sessionScope.user.userId == comment.createId}">
                    <div class="updateDeleteArea">
                        <div id="commentUpdateArea" class="commentUpdateArea">
                        	<button type="button" class="commentUpdate" onclick="toggleEditComment(${comment.commentId})">수정</button>
                        </div>
                        <div id="commentDeleteArea" class="commentDeleteArea">
                        	<button type="button" class="commentDelete" onclick="deleteComment(${comment.commentId})">삭제</button>
                        </div>
                        </div>
                    </c:if>
               	</c:if>
                <c:if test="${not empty sessionScope.user.userId}">
					<c:if test="${sessionScope.user.userId != comment.createId}">
						<div id="replyCommentBtnArea" class="replyCommentBtnArea">
                    		<button type="button" class="replycommentBtn" onclick="toggleReplyComment(${comment.commentId})">답글</button>
                    	</div>
                	</c:if>
                </c:if>
                </div>
               	
				 <div id="commentContent" class="commentContent">
				<p id="commentContent_${comment.commentId}">${comment.content}</p>
				</div>
				
				<!--댓글 수정 폼 -->
			<div id="editForm_${comment.commentId}" style="display:none">
				<textarea class="texta"  id="editContent_${comment.commentId}" rows="4">${comment.content}</textarea>
				<div id="updateCommentArea" class="updateCommentArea">
				<button type="button" onclick="editComment(${comment.commentId})">수정</button>
				<button type="button" onclick="toggleEditComment(${comment.commentId})">취소</button>
				</div>
			</div>
			
				<!--대댓글 작성 폼 -->
			<div  id="replyForm_${comment.commentId}" style="display:none; margin-left: 10px;">

				<textarea class="replComment" id="replyContent_${comment.commentId}" rows="3" placeholder="답글을 입력하세요..."></textarea>
						<div id="replyCommentArea" class="replyCommentArea">
							<button type="button" onclick="addComment(${comment.commentId})">답글 작성</button>
							<button type="button" onclick="toggleReplyComment(${comment.commentId})">취소</button>
						</div>
					</div>
				</div>
			<jsp:include page="commentItem.jsp">
				<jsp:param name="commentId" value="${comment.commentId}"/>
			</jsp:include>
			</div>
		</c:if>
	</c:forEach>
</c:if>
</div>