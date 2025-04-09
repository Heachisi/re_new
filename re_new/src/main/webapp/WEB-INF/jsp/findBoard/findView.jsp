<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/bulletinView.css?ver=1">
<link rel="stylesheet" href="/css/modal.css?ver=1">
	<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

</head>
<body>
<c:choose>
	<c:when test="${empty sessionScope.user}">
		<script>
			alert("로그인하셈");
			location.href = "/user/login.do";
		</script>
	</c:when>
	<c:otherwise>
<div class="boardArea">
	<form id="boardForm">
		<div class="title">
		<label for="title"></label> 
		${board.title} 
		<input type="hidden" id="boardId" value="${board.boardId}">
		</div>
		<div class="btnArea">
		<div class="writer"> 
		<label for="createId"></label> 
		${board.createId}
		<input type="hidden" id="updateId" name="updateId" value="${sessionScope.user.userId}" />
		</div> 
		<c:if test="${sessionScope.user.userId == board.createId}">
		<div class="update">
		<a href="/findboard/findUpdate.do?id=${board.boardId}" class="updatae">수정</a>
		</div>
		<div id="delete">
		<a href="#" id="deleteBtn">삭제</a>
		</div>
		</c:if>
		</div>
		<div id="date">
		</div>
		<div id="createDate">
		<label for="createDt"></label>
		${fn:substring(board.createDt, 0,10)} 
		</div>
		<div class="content">
		<label for="content"></label>
		${board.content} <br />
		</div>
	</form>
	<c:if test="${not empty board.postFiles}">
		<ul>
			<c:forEach var="file" items="${board.postFiles}">
				<li>
					${file.fileName}
					<a href="/findfile/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	<div id="comment">
	<c:if test="${not empty sessionScope.user.userId}">
		<h4>댓글</h4>
		<textarea class="commentContent"id="commentContent" rows="4" placeholder="댓글을 입력하세요"></textarea>
		<br/>
		<div class="commentCreate">
		<button type="button" class="commentCreateBtn"id="commentCreateBtn" onclick="addComment()">댓글 작성</button>
		<button type="button" class="attachProductBtn" id="attachProductBtn">상품 첨부</button>
		<div class="modal">
	        <div class="modalPopup">
	        	<div class="modalTitle">
	            <h3>상품 페이지 첨부</h3>
	            </div>
	            <form>
	            <div id="product" class="product">
	            </div>
	            </form> 
	            <br/>
	            <div class="modalBtnArea">
	            <button type="button" class="closeBtn">닫기</button>
	            <button type="button" class="attachBtn">첨부</button>
	            </div>
	        </div>
    	</div>
    
		
		</div>
	</c:if>
	
	<c:if test="${not empty board.comments}">
		
		<c:set var="comments" value="${board.comments}" scope="request"/>
		<!-- 댓글을 오래된 순으로 정렬 -->
		<c:set var="sortedComments" value="${comments}" />
    <c:forEach var="i" begin="0" end="${fn:length(sortedComments) - 2 >= 0 ? fn:length(sortedComments) - 2 : 0}">
    <!-- sortedComments: 댓글 리스트(배열) 
    	fn:lentgh(sortedComments) : 배열의 길이(개수) 반환 함수
    	-2한 값이 0보다 크면 그대로 사용 0보다 작으면 0으로 반환해 에러 방지
    	-->
        <c:forEach var="j" begin="${i + 1}" end="${fn:length(sortedComments) - 1}">
            <c:if test="${sortedComments[i].createDt > sortedComments[j].createDt}">
                <c:set var="temp" value="${sortedComments[i]}" />
                <c:set var="sortedComments[i]" value="${sortedComments[j]}" />
                <c:set var="sortedComments[j]" value="${temp}" />
            </c:if>
        </c:forEach>
    </c:forEach>
	<jsp:include page="findCommentItem.jsp">
			<jsp:param value="0" name="commentId"/>
		</jsp:include>
	</c:if>
	
	</div>
	</div>
	<br/>
	<br/><br/>
	<a href="/user/login.do">로그인</a>
	<a href="/findboard/findList.do">게시글 목록으로 이동</a><br/>
	</c:otherwise>
	</c:choose>
	<script>
	//댓글 생성
	function addComment(parentId){
		let content = parentId && parentId !=0 ?
				$('#replyContent_'+parentId).val().trim()
				:$('#commentContent').val().trim();
		
		if(!content){
			alert("댓글 내용을 입력해주세요.");
			return;
		}
		
		if(content.length > 500){
			alert("댓글 500자 이하로 작성해주세요.");
			return;
		}
		
		let boardId= '${board.boardId}';
		let createId = '${sessionScope.user.userId}';
		
		ajaxRequest(
			'/findboard/comment/create.do',
			{
				content: content,
				boardId: boardId,
				createId: createId,
				parentCommentId: parentId || 0
			},
			function(response){
				if (response.success) { 
					alert(response.message);
					console.log("parentId:", parentId);
					location.reload();
				}else{
					alert(response.message);
				}
			}
		);
		
	}			

	//댓글 수정 폼 토글
	function toggleEditComment(commentId){
		let editForm = $('#editForm_'+commentId);
		let content= $('#commentContent_'+commentId).text().trim();
		
		if(editForm.is(":visible")){
			editForm.hide();
			
		}else{
			editForm.show();
			$('#editContent_'+commentId).val(content);
		}
	}
	//댓글 수정
	function editComment(commentId){
		let content = $('#editContent_'+commentId).val().trim();
		
		if(!content){
			alert("댓글 내용을 입력해주세요.");
			return;
		}
		
		if(content.length > 500){
			alert("댓글 500자 이하로 작성해주세요.");
			return;
		}
		
		let boardId= '${board.boardId}';
		let updateId = '${sessionScope.user.userId}';
		
		ajaxRequest(
			'/findboard/comment/update.do',
			{
				commentId: commentId,
				content: content,
				boardId: boardId,
				updateId: updateId,
			},
			function(response){
				if (response.success) { 
					alert(response.message);
					location.reload();
				}else{
					alert(response.message);
				}
			}
		);
	}			
	
	//댓글 삭제
	function deleteComment(commentId){
		if(confirm('이 댓글을 삭제하시겠습니까?')){
			let updateId = '${sessionScope.user.userId}';
			ajaxRequest(
					'/findboard/comment/delete.do',
					{
						commentId: commentId,
						updateId: updateId,
					},
					function(response){
						if (response.success) { 
							alert(response.message);
							location.reload();
						}else{
							alert(response.message);
						}
					}
				);
		}
	}
	//대댓글 폼 토글
	function toggleReplyComment(commentId){
		
		let replyForm = $('#replyForm_'+commentId);
		if(replyForm.is(":visible")){
			replyForm.hide();
			
		}else{
			replyForm.show();
		}
	}
	$(document).ready(function(){
	$("#deleteBtn").click(function (event) {
		event.preventDefault(); // 기본 제출 방지
		ajaxRequest(
				"/findboard/findDelete.do",
				{ 
					boardId: $("#boardId").val(),
				 	updateId: $("#updateId").val()
				},
				function(response){
       				if(response.success){
    					alert("게시물을 삭제하였습니다. 게시판 목록으로 이동합니다.");
       					window.location.href="/findboard/findList.do";
       				}else{
       					alert("게시글 삭제를 실패하였습니다."+response.message);
       				}
				}
			);
	});
	})
	
	
	const modal = document.querySelector('.modal');
    const modalOpen = document.querySelector('.attachProductBtn');
    const modalClose = document.querySelector('.closeBtn');

    //열기 버튼을 눌렀을 때 모달팝업이 열림
    modalOpen.addEventListener('click',function(){
       //'on' class 추가
       modal.classList.add('on');
       document.body.classList.add('modal-open'); // 스크롤 막기
     ajaxRequest("/findboard/product/list.do", {}, function(response) {
           if (response.success && response.products.length > 0) {
        	   
               var html = "";
               var products = response.products;
               for (var i = 0; i < products.length; i++) {
                   var product = products[i];
                   html += "<div class='productItem'>" +
                           "<input type='checkbox' class='productCheckbox' value='" + product.productId + "' data-name='" + product.title + "'>" +
                           product.title +
                           "</div>";
               }
               document.getElementById("product").innerHTML = html;
           } else {
               document.getElementById("product").innerHTML = "<p>상품이 없습니다.</p>";
           }
       });
    });
    
    const attachBtn= document.querySelector('.attachBtn');

    attachBtn.addEventListener('click', function() {
        var checkboxes = document.getElementsByClassName('productCheckbox');
        var linkText = "";

        // 선택된 상품마다 링크 생성
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                var name = checkboxes[i].getAttribute('data-name');
                var id = checkboxes[i].getAttribute('value');
                linkText += "<a href='/product/productBoard.do?id=" + id + " target='_blank'>" + name + "</a><br/>";
            }
        }

        // 댓글 textarea에 추가
        var textarea = document.getElementById("commentContent");
        textarea.value += "\n" + linkText;

        // 모달 닫기
        modal.classList.remove('on');
        document.body.classList.remove('modal-open');
    });
    
    //닫기 버튼을 눌렀을 때 모달팝업이 닫힘
    modalClose.addEventListener('click',function(){
       	//'on' class 제거
     	modal.classList.remove('on');
     	document.body.classList.remove('modal-open'); // 스크롤 막기
    });
	</script>
</body>
</html>