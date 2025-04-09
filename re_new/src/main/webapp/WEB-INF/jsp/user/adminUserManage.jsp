<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
</head>
<body>
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

    $.ajax({
        type: "POST",
        url: "/user/getUserInfo.do",
        data: { userId: userId },
        success: function(response) {
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
        },
        error: function() {
            alert("서버 오류 발생");
        }
    });
}

function toggleUser(delYn) {
    const userId = $("#userId").text();

    $.ajax({
        type: "POST",
        url: "/user/toggleUserDeletion.do",
        data: { userId: userId, delYn: delYn, updateId: "adminUser" },
        success: function(response) {
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
    });
}
</script>
</body>
</html>