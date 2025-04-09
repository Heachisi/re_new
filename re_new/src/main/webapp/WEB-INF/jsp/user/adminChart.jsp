<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
</head>
<body>
<canvas id="ageChart"></canvas>
<canvas id="genderChart"></canvas>
<script>
$(document).ready(function () {
    // 연령대 데이터 가져오기
    ajaxRequest(
        "/user/age-group.do",
        {},
        function (response) {
            if (response.success) {
                renderAgeChart(response.data);
            } else {
                console.error("연령대 데이터 가져오기 실패");
            }
        }
    );

    // 성별 데이터 가져오기
    ajaxRequest(
        "/user/gender-stats.do",
        {},
        function (response) {
            if (response.success) {
                renderGenderChart(response.data);
            } else {
                console.error("성별 데이터 가져오기 실패");
            }
        }
        
      );
    

// 연령대 막대 그래프 렌더링
function renderAgeChart(ageData) {
    const labels = ageData.map(item => item.age_group);
    const counts = ageData.map(item => item.count);
    console.log(labels);
    const ctxAge = document.getElementById('ageChart').getContext('2d');
    new Chart(ctxAge, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '연령별 인원 수',
                data: counts,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// 성별 원 그래프 렌더링
function renderGenderChart(genderData) {
    const labels = genderData.map(item => item.GENDER);
    const counts = genderData.map(item => item.count);

    const ctxGender = document.getElementById('genderChart').getContext('2d');
    new Chart(ctxGender, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: '성별 비율',
                data: counts,
                backgroundColor: ['#36A2EB', '#FF6384']
            }]
        },
        options: {
            responsive: true
        }
    });
}

});

</script>
</body>
</html>