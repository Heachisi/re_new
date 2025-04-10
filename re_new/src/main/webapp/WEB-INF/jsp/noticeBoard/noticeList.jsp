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
	<script src="/js/common.js"></script>
	<style>
	table{
    border-collapse:collapse; 
	border:1px solid black;
	text-align: center;
	width:80%;
	}
	.createBtn{
    display: none; /* 기본적으로 숨김 */
    }
	</style>
	<link rel="stylesheet" href="/css/bulletinList.css?ver=1.2">
	<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
	<script>
		function search(page, checkNow) {
			
			if(checkNow) {
				let searchText = $("#noticeSearchText").val();
				let startDate = $("#noticeStartDate").val();
				let endDate = $("#noticeEndDate").val();
				window.location.href = "/noticeboard/noticeList.do?"
									  +"noticeSearchText="+searchText+"&"
									  +"noticeStartDate="+startDate+"&"
									  +"noticeEndDate="+endDate+"&"
									  +"page="+page+"&"
									  +"size=${size}";
			} else {
				let searchText = '${board.searchText}';
				let startDate = '${board.startDate}';
				let endDate = '${board.endDate}';
				window.location.href = "/noticeboard/noticeList.do?"
									  +"noticeSearchText="+searchText+"&"
									  +"noticeStartDate="+startDate+"&"
									  +"noticeEndDate="+endDate+"&"
									  +"page="+page+"&"
									  +"size=${size}";
				
			}
		}
		
		$(document).ready(function () {
			 ajaxRequest("/user/getUserRole.do", {}, function(response) {
			        console.log("서버 응답:", response);

			        if (response.adminYn === "Y") {
			            $(".createBtn").show();
			        } else {
			            $(".createBtn").hide();
			        }
			    });

			$("#noticeSearchBtn").click(function () {
				search(1,true);
			});
			
		});
	
	</script>
</head>
<body>
<div class="bottomSection">
	<h2>공지사항</h2>
	<div class="searchOption">
	<div class="startDateContainer">
	<label>시작 날짜</label>
	<input type="date" id="noticeStartDate" name="startDate" value="${board.startDate}">
	</div>
	<div class="endDateContainer">
	<label>종료 날짜</label>
	<input type="date" id="noticeEndDate" name="endDate" value="${board.endDate}">
	</div>
	<div class="searchContainer">
	<label></label>
	<input type="text" id="noticeSearchText" name="searchText" value="${board.searchText}">
	</div>
	<div class="searchBtnContainer">
	<button type="button" id="bulletinSearchBtn">검색</button>
	</div>
	</div>
	
	<div class="listContainer">
	<table border="1" class="boardList" id="boardList">
		<thead>
			<tr>
				<th >번호</th>
			  	<th class="title">제목</th>
			  	<th class="writer">작성자</th>
			  	<th class="createDate">작성일</th>
			  	<th class="viewCount">조회수</th>
			</tr>
		</thead>
		<tbody>
		   <c:forEach var="board" items="${boardList}">
		      <tr>
		      <td>${board.rn}</td>
		         <td><a href="noticeView.do?id=${board.boardId}" style="text-decoration: none; color:black;">${board.title}</a></td>
		         <td>${board.createId}</td>
		         <td>${board.createDt}</td>
				<td>${board.viewCount}</td>
		      </tr>
		   </c:forEach>
		</tbody>
	</table>
	</div>
	<div class="pageContainer">
	<ul>
	 <c:if test="${currentPage > 1}">

        <a href="noticeList.do?page=${currentPage - 1}&noticeSearchText=${board.searchText}&noticeStartDate=${board.startDate}&noticeEndDate=${board.endDate}" 
        	class="preArrow"onclick="search(${currentPage - 1}, false)">&laquo;</a>

	</c:if>
	<c:forEach begin="1" end="${totalPages}" var="i">
		<a href="noticeList.do?page=${i}&noticeSearchText=${board.searchText}&noticeStartDate=${board.startDate}&noticeEndDate=${board.endDate}" 
			class="pagination" onclick="search(${i}, false)"
        <c:if test="${i == currentPage}"> style="font-weight: bold;" </c:if>
        >${i}</a>
 
	</c:forEach>
	<c:if test="${currentPage < totalPages}">

        <a href="noticeList.do?page=${currentPage + 1}&noticeSearchText=${board.searchText}&noticeStartDate=${board.startDate}&noticeEndDate=${board.endDate}" 
        class="postArrow" onclick="search(${currentPage + 1}, false)">&raquo;</a>

	</c:if>
	</ul>
	</div>
	<div class="createBtnContainer">
	<div class="createBtn">
	
    <a href="/noticeboard/noticeCreate.do">글쓰기</a>

</body>
</html>
