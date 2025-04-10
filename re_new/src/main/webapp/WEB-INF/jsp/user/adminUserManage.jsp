<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	<h2>공지사항</h2>
	<div class="searchOption">
	<div class="startDateContainer">
	<label>시작 날짜</label>
	<input type="date" id="startDate" name="startDate" value="${board.startDate}">
	</div>
	<div class="endDateContainer">
	<label>종료 날짜</label>
	<input type="date" id="endDate" name="endDate" value="${board.endDate}">
	</div>
	<div class="searchContainer">
	<label></label>
	<input type="text" id="searchText" name="searchText" value="${board.searchText}">
	</div>
	<div class="searchBtnContainer">
	<button type="button" id="searchBtn">검색</button>
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

        <a href="noticeList.do?page=${currentPage - 1}&searchText=${board.searchText}&startDate=${board.startDate}&endDate=${board.endDate}" 
        	class="preArrow"onclick="search(${currentPage - 1}, false)">&laquo;</a>

	</c:if>
	<c:forEach begin="1" end="${totalPages}" var="i">
		<a href="noticeList.do?page=${i}&searchText=${board.searchText}&startDate=${board.startDate}&endDate=${board.endDate}" 
			class="pagination" onclick="search(${i}, false)"
        <c:if test="${i == currentPage}"> style="font-weight: bold;" </c:if>
        >${i}</a>
 
	</c:forEach>
	<c:if test="${currentPage < totalPages}">

        <a href="noticeList.do?page=${currentPage + 1}&searchText=${board.searchText}&startDate=${board.startDate}&endDate=${board.endDate}" 
        class="postArrow" onclick="search(${currentPage + 1}, false)">&raquo;</a>

	</c:if>
	</ul>
	</div>
	<div class="createBtnContainer">
	<div class="createBtn">
	
    <a href="/noticeboard/noticeCreate.do">글쓰기</a>
    

	</div>
	</div>
	</div>

	
	
	<a href="/user/login.do">메인으로 이동</a><br/>
	<a href="/noticeboard/noticeCreate.do">게시글 생성 이동</a><br/>

	>
<h2>회원 검색</h2>
<input type="text" id="searchUserId" placeholder="회원 ID 입력">
<button onclick="searchUser()">검색</button>

<table id="userTable" border="1" style="display: none;">
    <tr>
        <th>회원 ID</th>
        <th>회원가입 날짜</th>
        <th>가입자 ID</th>
        <th>회원 상태</th>
        <th>삭제</th>
        <th>복구</th>
    </tr>
    <tr id="userRow">
        <td id="userId"></td>
        <td id="createDt"></td>
        <td id="createId"></td>
        <td id="status"></td>
        <td><button onclick="toggleUser('Y')">삭제</button></td>
        <td><button onclick="toggleUser('N')">복구</button></td>
    </tr>
</table>

<script>
function searchUser() {
	
    const userId = $("#searchUserId").val();

	ajaxRequest(
        "/user/adminUserManage.do",
        { userId: userId },
        function(response) {
            if (response.success) {
                $("#userId").text(response.userId);
                $("#createDt").text(response.createDt);
                $("#createId").text(response.createId);
                $("#status").text(response.delYn === "Y" ? "탈퇴됨" : "활성화");
                $("#userTable").show();
            } else {
                alert(response.message);
                $("#userTable").hide();
            }
        }
    );
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
        },
        error: function() {
            alert("서버 오류 발생");
        }
    );
}
</script>
</body>
</html>