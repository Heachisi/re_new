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
    <link rel="stylesheet" href="/css/list.css?ver=1.1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/nouislider@15.7.0/dist/nouislider.min.css">
    <script src="https://cdn.jsdelivr.net/npm/nouislider@15.7.0/dist/nouislider.min.js"></script>
    <script>
	    function url(page, viewcategory){
	    	let searchText = $("#searchText").val();
			let startDate = $("#startDate").val();
			let endDate = $("#endDate").val();
			let minprice = $("#minprice").val();
			let maxprice = $("#maxprice").val();
			
        	window.location.href = "/product/list.do?"+
        	"searchText=" + searchText + "&" +
            "startDate=" + startDate + "&" +
            "endDate=" + endDate + "&" +
            "viewcategory=" + viewcategory + "&" +
            "minprice=" + minprice + "&" +
            "maxprice=" + maxprice + "&" +
            "page=" + page + "&" +
            "size=${size}";
			if(viewcategory==null){
				window.location.href = "/product/list.do?"+
				"searchText=" +searchText+"&"+
				"startDate=" +startDate+"&"+
				"endDate=" +endDate+"&"+
				"page=" +page+"&"+
				"size=${size}";
			}
	    }
	 
	    function sortByCategory(page,viewcategory) {
	        if (viewcategory === "추천순") {
	        	url(1,viewcategory);
	        } else if (viewcategory === "최신순") {
	        	url(1,viewcategory);
	        } else if (viewcategory === "낮은 가격순") {
	        	url(1,viewcategory);
	        } else if (viewcategory === "높은 가격순") {
	        	url(1,viewcategory);
	        }
	    }
	    
    	$(document).ready(function() {
    		$("#searchBtn").click(function() {
    			url(1, null);
    		});
    		$("#viewcategory").change(function() {
    	        let selectedValue = $(this).val();
    	        sortByCategory(1,selectedValue);
    	    });
    		
    		const priceSlider = document.getElementById('priceSlider');
    		
    		const minInput = document.getElementById('minprice');
	  		const maxInput = document.getElementById('maxprice');
	  		const minLabel = document.getElementById('minLabel');
	  		const maxLabel = document.getElementById('maxLabel');
  
	  		noUiSlider.create(priceSlider, {
	  		  start: [parseInt(minInput.value), parseInt(maxInput.value)],
	  		  connect: true,
	  		  step: 1000,
	  		  range: {
	  		    'min': parseInt(document.getElementById('minprice').value),
	  		    'max': parseInt(document.getElementById('maxprice').value)
	  		  },
	  		  format: {
	  		    to: value => Math.round(value),
	  		    from: value => Number(value)
	  		  }
	  		}); 
	  		
	  		priceSlider.noUiSlider.on('update', function(values, handle) {
	  		    var min = values[0];
	  		    var max = values[1];

	  		    minInput.value = min;
	  		    maxInput.value = max;

	  		    minLabel.textContent = '₩' + Number(min).toLocaleString();
	  		    maxLabel.textContent = '₩' + Number(max).toLocaleString();
	  		});
	  		
    	});
    </script>
</head>
<body>
	<input class="search" type="text" id="searchText" name="searchText" value="${product.searchText}">
	<label hidden>시작 날짜</label>
	<input hidden type="date" id="startDate" name="startDate" value="${product.startDate}">
	<label hidden>종료 날짜</label>
	<input hidden type="date" id="endDate" name="endDate" value="${product.endDate}">
	<input type="hidden" id="likecount" name="likecount"value="0">
	<button type="button" id="searchBtn" class="searchBtn">검색</button>
<div class="list">
	<div class="notice">
	<table style="border: 1px solid rgb(245,183,89); border-collapse: collapse;">
			<thead>
				<tr>
					<th class="th">공지</th>
					<th class="th2">작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="product" items="${productList}">
					<tr>
						<td class="th">${product.title}</td>
						<td class="th2">${product.createDt}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div style="width: 400px; margin: 20px auto;">
	  <div style="display: flex; justify-content: space-between; margin-bottom: 5px;">
	    <span id="minLabel"></span>
	    <span id="maxLabel"></span>
	  </div>
	  <div id="priceSlider"></div>
	  <input type="hidden" id="minprice" name="minprice" value="${product.minprice}" />
	  <input type="hidden" id="maxprice" name="maxprice" value="${product.maxprice}" />
	</div>
	<select id="viewcategory" name="viewcategory" style="height:20px;">
            <option value="">카테고리 선택</option>
            <option value="최신순">최신순</option>
            <option value="낮은 가격순">낮은 가격순</option>
            <option value="높은 가격순">높은 가격순</option>
            <option value="추천순">추천순</option>
    </select>
	<div id="boardlist">
			<c:forEach var="product" items="${productList}">
			<div class="index" onclick="location.href='view.do?id=${product.productId}'">
					<img src="/upload/${product.photo}" alt="업로드 이미지" style="max-width: 200px;max-height: 120px;">
					<br/>
					<div class="title">${product.title}</div>
					<div class="price">${product.price}원</div>
					<c:if test="${product.liked == 1}">
						<div class="like">♥ ${product.likecount}</div>
					</c:if>
					<c:if test="${product.liked == 0}">
				        <div class="like">♡ ${product.likecount}</div>
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
<style>
	.title{
	  position:absolute;
	  bottom:40px;
	  left:1px;
	  width:150px; 
  	}
  	.price{
	  position:absolute;
	  bottom:20px;
	  left:1px;
	  width:150px;
	  font-weight: bold;
  	}
  .like{
	  position:absolute;
	  bottom:40px;
	  right:25px; 
 	 }
  .index {
  position: relative; /* 기준점 역할 추가 */
  border: 1px solid rgb(245,183,89);
  border-radius: 12px;
  text-align: center; 
  cursor: pointer;
  height: 200px;
  width: 250px;
  margin:10px;
  
  align-items: center; 
	}
  #priceSlider {
    width: 300px;
    height: 8px;
    margin: 10px auto;
  }

  .noUi-target {
    height: 8px !important;
    background: #e0e0e0;
    border-radius: 5px;
  }

  .noUi-connect {
    background: #f5b759 !important;
  }

  .noUi-handle {
    width: 14px !important;
    height: 14px !important;
    top: -3px !important;
    right: -7px !important;
    border-radius: 50% !important;
    background: #fff;
    border: 1px solid #aaa;
    box-shadow: none;
  }

  .noUi-handle:before,
  .noUi-handle:after {
    display: none !important;
  }
</style>