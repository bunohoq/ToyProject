<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
body {
	padding-bottom: 500px;
}
</style>
</head>
<body>
	<!-- log.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp"%>

	<div id="main">
		<h1>
			관리자 <small>로그</small>
		</h1>

		<!--  
			일정(달력) + 시간순 발생 데이터 통계 자료
			모든 유저 > 선택
		-->

		<div class="message content" style="padding-left: 1rem;">
			유저: <select id="selUser">
				<option disabled selected>선택하세요.</option>
				<c:forEach items="${list}" var="dto">
					<option value="${dto.id}">${dto.name}(${dto.id})</option>
				</c:forEach>
			</select>
		</div>

		<h2 id="titleCalendar">
			<span> <span>활동 내역</span> <span>2025.09</span>
			</span> <span> <span class="material-symbols-outlined" id="btnPrev"
				title="이전달">skip_previous</span> <span
				class="material-symbols-outlined" id="btnNow" title="이번달">today</span>
				<span class="material-symbols-outlined" id="btnNext" title="다음달">skip_next</span>
			</span>
		</h2>

		<table id="tblCalendar">
			<thead>
				<tr>
					<th>SUN</th>
					<th>MON</th>
					<th>TUE</th>
					<th>WED</th>
					<th>THU</th>
					<th>FRI</th>
					<th>SAT</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>

		<hr>

		<h2>
			접속 통계 <small>페이지</small>
		</h2>

		<div id="chart_div"></div>



	</div>

	<!-- 
	<script src="https://code.jquery.com/ui/1.14.1/jquery-ui.js" integrity="sha256-9zljDKpE/mQxmaR4V2cGVaQ7arF3CcXxarvgr7Sj8Uc=" crossorigin="anonymous"></script> 
	-->
	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>
	<script>
	
		let year = 0;
		let month = 0;
		let id = '';
		
		//이번달
		let now = new Date();
		year = now.getFullYear();
		month = now.getMonth();
		
		function create(year, month) {
			
			//alert(year + ':' + month);
			$('#titleCalendar span:first-child span:last-child').text(year + '.' + String(month + 1).padStart(2, '0'));
			
			//1. 해당월의 마지막 날
			//2. 해당월의 1일의 요일
			
			let lastDate = (new Date(year, month + 1, 0)).getDate();
			//alert(lastDate);
			
			let firstDay = (new Date(year, month, 1)).getDay();
			//alert(firstDay);
			
			let temp = '';
			
			//한주 <tr>
			temp += '<tr>';
			
			//1일의 앞부분을 비워두기(요일에 따라)
			for (let i=0; i<firstDay; i++) {
				temp += '<td></td>';			
			}
			
			//날짜 <td>
			for (let i=1; i<=lastDate; i++) {
				temp += '<td>';
				temp += `<span class="no" date="\${i}">\${i}</span>`;
				
				temp += '<div>';
				//temp += '<span class="lcnt">5</span>'; //로그인 횟수
				//temp += '<span class="bcnt">2</span>'; //게시물 작성 수
				//temp += '<span class="ccnt">3</span>'; //댓글 작성 수
				temp += '</div>';
				
				temp += '</td>';
				
				//매주 토요일 > 개행(</tr><tr>)
				if ((i + firstDay) % 7 == 0) {
					temp += '</tr><tr>';
				}
				
			}
			
			
			
			let seed = 7;
			if (7 - (lastDate % 7 + firstDay) < 0) {
				seed = 14;
			}
			
			for (let i=0; i<(seed - (lastDate % 7 + firstDay)); i++) {
				temp += '<td></td>';
			}
			
			
			
			temp += '</tr>';
			
			$('#tblCalendar tbody').html(temp);
			
		}
		
		create(year, month);
		
		
		
		$('#btnPrev').click(() => {
			now.setMonth(now.getMonth() - 1);
			year = now.getFullYear();
			month = now.getMonth();
			create(year, month);
			loadLog(year, month, id);
		});
		
		$('#btnNow').click(() => {
			now = new Date();
			year = now.getFullYear();
			month = now.getMonth();
			create(year, month);
			loadLog(year, month, id);
		});
		
		$('#btnNext').click(() => {
			now.setMonth(now.getMonth() + 1);
			year = now.getFullYear();
			month = now.getMonth();
			create(year, month);
			loadLog(year, month, id);
		});
		
		
		//DB 로그 조회
		function loadLog(year, month, id) {
			
			month++;
			
			$.ajax({
				type: 'GET',
				url: '/toy/admin/loadlog.do',
				data: {
					year: year,
					month: month,
					id: id
				},
				dataType: 'json',
				success: function(result) {
					
					//기존 유저의 로그 기록 삭제
					$('#tblCalendar div').html('');
					
					//console.log(result);
					result.forEach(item => {
						
						//item.regdate > 2025-09-23
						//item.lcnt
						//item.bcnt
						//item.ccnt
						
						let date = parseInt(item.regdate.split('-')[2]);
						
						//if (item.lcnt > 0) {
						$('#tblCalendar span[date=' + date + '] + div')
							.append('<span class="lcnt">O</span>');
						//}
						
						if (item.bcnt > 0) {
							$('#tblCalendar span[date=' + date + '] + div')
								.append('<span class="bcnt">' + item.bcnt + '</span>');
						}
						
						if (item.ccnt > 0) {
							$('#tblCalendar span[date=' + date + '] + div')
								.append('<span class="ccnt">' + item.ccnt + '</span>');
						}
						
					});
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		}
		
		//loadLog(year, month, 'cat');
		
		$('#selUser').change(() => {
			
			id = $(event.target).val();
			
			loadLog(year, month, id);	//달력
			drawUserChart(id);			//차트
			
		});
		
		
		//차트
		function drawUserChart(id) {
			
			//데이터 가져오기
			$.ajax({
				type: 'GET',
				url: '/toy/admin/listlog.do',
				data: {
					id: id
				},
				dataType: 'json',
				success: function(result) {
					
					//console.log(result);
					if (result.length > 0) {
						
						//차트 그리기
						let chartData = [["페이지", "접속 횟수"]];
						
				        result.forEach(item => {
				        	chartData.push([item.url, parseInt(item.cnt)]);
				        });
						
						google.charts.load('current', {packages: ['corechart', 'bar']});
						google.charts.setOnLoadCallback(function() {
							drawBasic(chartData);
						});
						
					} else {
						$('#chart_div').text('방문 기록이 없습니다.');
					}
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
			
		}
		
	

		function drawBasic(param) {
		
		  let data = google.visualization.arrayToDataTable(param);
	
	      var options = {
	        title: '유저 페이지 방문 횟수',
	        chartArea: {width: '60%'},
	        hAxis: {
	          title: '방문 횟수',
	          minValue: 0
	        },
	        vAxis: {
	          title: '페이지'
	        }
	      };
	
	      var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
	
	      chart.draw(data, options);
	    }
	
		
		
	</script>

</body>
</html>


