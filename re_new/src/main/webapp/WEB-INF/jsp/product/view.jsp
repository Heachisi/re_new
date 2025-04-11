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
    <link rel="stylesheet" href="/css/view.css?ver=3">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
</head>
<body>
	<div class="summery">
	  <img src="/upload/${product.photo}" alt="업로드 이미지" style="max-width: 400px; max-height: 300px;">
	  <span class="stitle">${product.title}</span> 
	  <span class="sprice">${product.price}원</span> <br/>
	  <span class="sid">${product.createId}</span> 
	  <c:if test="${product.liked == 1}">
        <span class="like"><button id="lbtn" class="likebtn">♥</button></span>
	  </c:if>
	  <c:if test="${product.liked == 0}">
        <span class="like"><button id="lbtn" class="likebtn">♡</button></span>
	  </c:if>
	  <span class="lik" id="likecount">${product.likecount}</span> 
	  <span id="liked" style="display:none;">${product.liked}</span>
	  <input type="hidden" id="productId" value="${product.productId}" />
		<input type="hidden" id="updateId" value="${sessionScope.user.userId}" />
	  <br/>
	  
	  <br/>
	</div>
	<div class="detail">
	<label for="content"><h3>상세설명</h3></label>
	<br/>
	  ${product.content}
	</div>
	<c:if test="${not empty product.postFiles}">
		<ul>
			<c:forEach var="file" items="${product.postFiles}">
				<li>
					${file.fileName}
					<a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty sessionScope.user.userId}">
	   <div class="h5">
	    <h4>댓글</h4>
			<div class="insertComment">
				<textarea class="texta" id="commentContent" rows="4" placeholder="댓글을 입력하세요..."></textarea>
				<button type="button" id="commentCreateBtn" onclick="addComment()">댓글 작성</button>
			</div>
		</div>
	</c:if>
	
	<c:if test="${not empty product.comments}">
		<div class="h4">
			<!-- ${product.comments} 값을 comments라는 이름으로 request에 저장해 JSP에서 사용할 수 있게 해줌 -->
			<c:set var="comments" value="${product.comments}" scope="request"/>
			<jsp:include page="commentItem.jsp">
				<jsp:param name="commentId" value="0" /> 
			</jsp:include>
		</div>
	</c:if>
	<script>
		$(document).ready(function() {
			
			$("#lbtn").click(function(event){
				let likecount = parseInt($("#likecount").text());
				let liked = parseInt($("#liked").text());
				let productId = $("#productId").val();
			    let updateId = $("#updateId").val();
			    if("${sessionScope.user.userId}"){
			    	if(liked==0){
						ajaxRequest(
								  "/product/updatelike.do",
								  {
								    productId: $("#productId").val(),
								    updateId: $("#updateId").val(),
								    likecount: likecount + 1,
								    liked: liked + 1
								  },
								  function (response) {
									  if (response.success) {
										    alert("좋아요");
										    $("#likecount").text(likecount + 1);
										    $("#liked").text("1");
										    $("#lbtn").text("♥");
										} else {
									      alert("통신실패");
									    }
								  }
								);
					} else if(liked==1){
						ajaxRequest(
								  "/product/updatelike.do",
								  {
								    productId: $("#productId").val(),
								    updateId: $("#updateId").val(),
								    likecount: likecount - 1,
								    liked: liked - 1
								  },
								  function (response) {
									  if (response.success) {
										    alert("안 좋아요");
										    $("#likecount").text(likecount - 1);
										    $("#liked").text("0");
										    $("#lbtn").text("♡");
										} else {
								      alert("통신실패");
								    }
								  }
								);
					}	
			    } else {
			    	alert("로그인을 해주세요")
			    	window.location.href = "/user/login.do"
			    }
				
				
				});
			
		});
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
			
			let productId = ${product.productId};
			let createId = "${sessionScope.user.userId}";
			
			ajaxRequest(
				'/product/comment/create.do',
				{
					content: content,
					productId: productId,
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
    		
    		let productId = ${product.productId};
			let updateId = "${sessionScope.user.userId}";
			
			ajaxRequest(
					'/product/comment/update.do',
					{
						commentId: commentId,
						content: content,
						productId: productId,
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
    					'/product/comment/delete.do',
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
    </script>
</body>
</html>
