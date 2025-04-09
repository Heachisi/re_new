<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/common.js?ver=1.2"></script>
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
    <link rel="stylesheet" href="/css/sharelist.css?ver=1">
    <style>
	table{
    border-collapse:collapse; 
	border:1px solid black;
	text-align: center;
	width:80%;
	}
	</style>
    <script>
    	$(document).ready(function() {
    		
    		$("#searchBtn").click(function() {
    			alert($("#searchText").val())
    			let searchText = $("#searchText").val();
    			let startDate = $("#startDate").val();
    			let endDate = $("#endDate").val();
    			
    			window.location.href = "/share/list.do?"+
				"searchText=" +searchText+"&"+
				"startDate=" +startDate+"&"+
				"endDate=" +endDate+"&"+
				"page=" +1+"&"+
				"size=${size}";
    		});
    		
					//ajaxRequest(
					//	"/share/updateviewcount.do",
					//  	{
					//  	shareId: $("#shareId").val(),
					//		updateId: $("#updateId").val(),
					//		viewcount: viewcount + 1,
					//		},
					//	function (response) {
					//  	if (response.success) {
					//    		alert("좋아요");
					//				} else {
					//			alert("통신실패");
					//				}
					//			}
					//		);
			});
    
    </script>
</head>
<body>
<div class="bottomSection">
	<h2>나눔</h2>
	<div class="searchOption">
	<div class="startDateContainer">
	<label>시작 날짜</label>
	<input type="date" id="startDate" name="startDate" value="${share.startDate}">
	</div>
	<div class="endDateContainer">
	<label>종료 날짜</label>
	<input type="date" id="endDate" name="endDate" value="${share.endDate}">
	</div>
	<div class="searchContainer">
	<label></label>
	<input type="text" id="searchText" name="searchText" value="${share.searchText}">
	</div>
	<div class="searchBtnContainer">
	<button type="button" id="searchBtn">검색</button>
	</div>
<div class="listContainer">
	<table border="1" class="boardList" id="boardList">
			<thead>
				<tr>
					<th class="title">제목</th>
					<th class="writer">작성자</th>
					<th class="createDate">작성일</th>
			  		<th class="viewCount">조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${shareList}">
						<tr>
							<td><a href="view.do?id=${item.shareId}">${item.title}</a></td>
							<td>${item.createId}</td>
							<td>${item.createDt}</td>
							<td>${item.viewcount}</td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
	<div class="pageContainer">
	<ul>
	 <c:if test="${currentPage > 1}">

        <a href="bulletinList.do?page=${currentPage - 1}&searchText=${share.searchText}&startDate=${share.startDate}&endDate=${share.endDate}" 
        	class="preArrow"onclick="search(${currentPage - 1}, false)">&laquo;</a>

	</c:if>
	<c:forEach begin="1" end="${totalPages}" var="i">
		<a href="bulletinList.do?page=${i}&searchText=${share.searchText}&startDate=${share.startDate}&endDate=${share.endDate}" 
			class="pagination" onclick="search(${i}, false)"
        <c:if test="${i == currentPage}"> style="font-weight: bold;" </c:if>
        >${i}</a>
 
	</c:forEach>
	<c:if test="${currentPage < totalPages}">

        <a href="bulletinList.do?page=${currentPage + 1}&searchText=${share.searchText}&startDate=${share.startDate}&endDate=${share.endDate}" 
        class="postArrow" onclick="search(${currentPage + 1}, false)">&raquo;</a>

	</c:if>
	</ul>
	</div>
	<div class="createBtnContainer">
	<div class="createBtn">
	<a href="/share/create.do">글쓰기</a>
	</div>
	</div>
</div>
	
</body>
</html>