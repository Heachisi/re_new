<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>내 게시판 목록</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/common.js?ver=1.2"></script>
    <link rel="stylesheet" href="/css/productlist.css?ver=1.1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/nouislider@15.7.0/dist/nouislider.min.css">
    <script src="https://cdn.jsdelivr.net/npm/nouislider@15.7.0/dist/nouislider.min.js"></script>
    <jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
    <script>
	    function url(page, sellstatus){
	    	let searchText = $("#searchText").val();
	    	let startDate = $("#startDate").val();
	    	let endDate = $("#endDate").val();
	
	    	window.location.href = "/product/mylist.do?" +
	    		"searchText=" + searchText + "&" +
	    		"startDate=" + startDate + "&" +
	    		"endDate=" + endDate + "&" +
	    		"sellstatus=" + sellstatus + "&" +
	    		"page=" + page + "&" +
	    		"size=${size}";
	    }
	    
    	$(document).ready(function() {
    		$("#sellstatus").change(function() {
    			let selectedValue = $(this).val();
    			url(1, selectedValue);
    		});
    	
  
	  		
	  		
    	});
    </script>
</head>
<body>
	
	<label hidden>시작 날짜</label>
	<input hidden type="date" id="startDate" name="startDate" value="${product.startDate}">
	<label hidden>종료 날짜</label>
	<input hidden type="date" id="endDate" name="endDate" value="${product.endDate}">

<div class="list">
	
	<select id="sellstatus" name="sellstatus">
            <option value="">카테고리 선택</option>
            <option value="00">판매중</option>
            <option value="11">판매완료</option>
    </select>
	<div id="boardlist">
			<c:forEach var="product" items="${productList}">
			<div class="index">
					<img src="/upload/${product.photo}" onclick="location.href='view.do?id=${product.productId}'" alt="업로드 이미지" style="max-width: 200px; cursor: pointer; max-height: 120px;">
					<br/>
					<div class="title">${product.title}</div>
					<div class="price">${product.price}원</div>
					<a href="/product/update.do?id=${product.productId}" class="update">수정</a>
				    <c:if test="${product.sellstatus == 1}">
						<div class="like">판매완료</div>
					</c:if>
					<c:if test="${product.sellstatus == 0}">
				        <div class="like">판매중</div>
				    </c:if>
			</div>
			</c:forEach>
	</div>
	<br/>
	<br/>
		<ul>
			<c:if test="${currentPage > 1}">
					<a href="" onclick="search(${currentPage - 1}, false)" >&laquo;</a>
			</c:if>
			<c:forEach begin="1" end="${totalPages}" var="i">
				<a href="" onclick="search(${i},false)"
				<c:if test="${i == currentPage}">style="font-weight: bold;"</c:if>
				>${i}</a>
			</c:forEach>
			<c:if test="${currentPage < totalPages}">
					<a href="" onclick="search(${currentPage + 1}, false)" >&raquo;</a>
			</c:if>
		</ul>
</div>
	
</body>
</html>