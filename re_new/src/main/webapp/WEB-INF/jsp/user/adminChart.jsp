<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<canvas id="myChart"></canvas>
<script>
const ctx = document.getElementById("myChart").getContext("2d");

const myChart = new Chart(ctx, {
    type: "bar", // 그래프 종류 (bar, line, pie 등)
    data: {
        labels: ["A", "B", "C", "D"], // X축 항목
        datasets: [{
            label: "데이터 예제",
            data: [12, 19, 3, 5], // Y축 값
            backgroundColor: ["red", "blue", "green", "yellow"],
            borderColor: ["black"],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                position: "top"
            }
        }
    }
});
</script>


</body>
</html>