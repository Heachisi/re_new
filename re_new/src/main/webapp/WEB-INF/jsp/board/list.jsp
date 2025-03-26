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
	</style>
</head>
<body>
	<h2>게시판 상세</h2>
	<table border="1">
		<thread>
			<tr>
			  <th>번호</th>
			  <th>제목</th>
			  <th>작성자</th>
			  <th>조회수</th>
			  <th>작성일</th>
			  <th>상세보기</th>
			</tr>
		</thread>
		<tbody>
		   <c:forEach var="board" items="${boardList}">
		      <tr>
		         <td>${board.rn}</td>
		         <td>${board.title}</td>
		         <td>${board.createId}</td>
		         <td>${board.viewCount}</td>
		         <td>${board.createDt}</td>
		         <td><a href="view.do?id=${board.boardId}">상세</a></td>
		      </tr>
		   </c:forEach>
		</tbody>
	</table>
	<ul>
	 <c:if test="${currentPage > 1}">

        <a href="list.do?page=${currentPage - 1}&size=${size}">&laquo;</a>

</c:if>
<c:forEach begin="1" end="${totalPages}" var="i">
<a 
        <c:if test="${i == currentPage}"> style="font-weight: bold;" </c:if>
        href="list.do?page=${i}&size=${size}">${i}</a>
 
</c:forEach>
<c:if test="${currentPage < totalPages}">

        <a href="list.do?page=${currentPage + 1}&size=${size}">&raquo;</a>

</c:if>

	<a href="/board/main.do">목록으로 이동</a>
	

</body>
</html>
