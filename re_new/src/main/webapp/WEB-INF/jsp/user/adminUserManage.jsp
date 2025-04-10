<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
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

<div class="bottomSection">
	
<h2>회원 검색</h2>

<div class="startDateContainer">
	<label>시작 날짜</label>
	<input type="date" id="userStartDate" name="userStartDate" value="${user.startDate}">
	</div>
	<div class="endDateContainer">
	<label>종료 날짜</label>
	<input type="date" id="userEndDate" name="userEndDate" value="${user.endDate}">
	</div>
<input type="text" id="userSearchText" name="userSearchText" value="${user.searchText}" placeholder="회원 ID 입력">
<button type="button" onclick="searchUser(1,true);">검색</button>
 
<table id="userTable" border="1" >
    <tr>
        <th>회원 ID</th>
        <th>회원가입 날짜</th>
        <th>상태</th>
    </tr>
     
    <c:forEach var="user" items="${userList}">
    <tr>
   
        <td id="userId">${user.userId}</td>
		<td id="createId">${user.createDt}</td>
        <td></td>

       
    </tr>
    </c:forEach>
</table>
</div>

<div class="pageContainer">
	<ul>
	
	
	 <c:if test="${currentPage > 1}">

        <button type="button" class="preArrow" onclick="searchUser(${currentPage - 1}, false)">&laquo;</button>

	</c:if>
	<c:forEach begin="1" end="${totalPages}" var="i">
		<button type="button" 
			class="pagination" onclick="searchUser(${i}, false)" 
            <c:if test="${i == currentPage}"> style="font-weight: bold;" </c:if>
        >${i}</button>
 
	</c:forEach>
	<c:if test="${currentPage < totalPages}">

        <button type="button" class="postArrow" onclick="searchUser(${currentPage + 1}, false)">&raquo;</button>

	</c:if>
	</ul>
	</div>
<script>
function searchUser(page,checkNow) {
	if(checkNow) {
		let searchText = $("#userSearchText").val();
		let startDate = $("#userStartDate").val();
		let endDate = $("#userEndDate").val();
		window.location.href = "/user/adminUserManage.do?"
							  +"searchText="+searchText+"&"
							  +"startDate="+startDate+"&"
							  +"endDate="+endDate+"&"
							  +"page="+page+"&"
							  +"size=${size}";
	} else {
		let searchText = '${user.searchText}';
		let startDate = '${user.startDate}';
		let endDate = '${user.endDate}';
		window.location.href = "/user/adminUserManage.do?"
							  +"searchText="+searchText+"&"
							  +"startDate="+startDate+"&"
							  +"endDate="+endDate+"&"
							  +"page="+page+"&"
							  +"size=${size}";
		
	}
   
}

function toggleUser(delYn) {
	
    const userId = $("#userId").text();

    ajaxRequest(
        
        "/user/toggleUserDeletion.do",
        { userId: userId, delYn: delYn, updateId: "adminUser" },
        function(response) {
            if (response.success) {
                alert(response.message);
                searchUser(); // 상태 변경 후 다시 검색
            } else {
                alert("회원 상태 변경 실패!");
            }
        }
    );
}
</script>
</body>
</html>