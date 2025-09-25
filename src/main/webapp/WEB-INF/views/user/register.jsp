<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
</head>
<body>
	<!-- register.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<div id="main">
		<h1>회원 <small>가입하기</small></h1>
		
		<form method="POST" action="/toy/user/register.do" enctype="multipart/form-data">
		<table class="vertical">
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" id="id" required class="short"></td>
			</tr>
			<tr>
				<th>암호</th>
				<td><input type="password" name="pw" id="pw" required class="short"></td>
			</tr>
			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name" required class="short"></td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
					<div>
						<input type="email" name="email" id="email" required class="long">
						<input type="button" value="인증 메일 보내기" id="btnMail">
					</div>
					<div style="margin-top: 10px;">
						<input type="text" id="validNumber" class="short" disabled maxlength="5">
						<input type="button" value="입력하기" id="btnValid" disabled>
						<span id="remainTime" style="display: none;">05:00</span>
					</div>
				</td>
			</tr>
			<tr>
				<th>사진</th>
				<td><input type="file" name="attach" id="attach" class="long"></td>
			</tr>
			<tr>
				<th>소개</th>
				<td><textarea name="intro" id="intro" class="long"></textarea></td>
			</tr>
		</table>
		<div>
			<button type="button" class="back" onclick="location.href='/toy/index.do';">돌아가기</button>
			<button type="submit" class="in primary">가입하기</button>
		</div>
		</form>
		
	</div>
	
	<script>
	
		let timer = 0;
	
		$('#btnMail').click(() => {
			
			if ($('#email').val().trim() != '') {
				
				$.ajax({
					type: 'POST',
					url: '/toy/user/sendmail.do',
					data: {
						email: $('#email').val().trim()
					},
					dataType: 'json',
					success: function(result) {
						
						if (result.result > 0) {
							
							//alert('성공');
							$('#validNumber').prop('disabled', false);
							$('#btnValid').prop('disabled', false);
							$('#remainTime').show();
							
							//타이머 동작
							const remainTime = new Date();
							remainTime.setMinutes(5);
							remainTime.setSeconds(0);
							
							timer = setInterval(() => {
								
								remainTime.setSeconds(remainTime.getSeconds() - 1);
								$('#remainTime').text(
									String(remainTime.getMinutes()).padStart(2, '0')
									+ ':'
									+ String(remainTime.getSeconds()).padStart(2, '0')
								);
								
								if ($('#remainTime').text() == '00:00') {
									
									
									//인증 시간 만료
									$.ajax({
										type: 'POST',
										url: '/toy/user/delmail.do',
										dataType: 'json',
										success: function(result) {
																						
											if (result.result > 0) {
												
												$('#validNumber').val('');
												$('#btnValid').prop('disabled', true);
												$('#validNumber').prop('disabled', true);
												$('#remainTime').hide();
												
												clearInterval(timer);
												timer = 0;
												
											}
											
										},
										error: function(a,b,c) {
											console.log(a,b,c);
										}
									});
									
								}
						
							}, 1000);
							
						} else {
							alert('인증 메일 발송에 실패했습니다.');
						}
						
					},
					error: function(a,b,c) {
						console.log(a,b,c);
					}
				});
				
			} else {
				alert('이메일을 입력하세요.');
			}		
			
		});
		
		$('#btnValid').click(() => {
			
			$.ajax({
				type: 'POST',
				url: '/toy/user/validmail.do',
				data: {
					validNumber: $('#validNumber').val()
				},
				dataType: 'json',
				success: function(result) {
					
					if (result.result > 0) {
						
						alert('성공');
						isValid = true;
						
					} else {
						alert('인증 번호가 틀립니다.');
					}
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		});
		
		let isValid = false;
		
		$('form').submit(() => {
			
			if (!isValid) {
				alert('이메일 인증을 진행하세요.');
				event.preventDefault();
				return false;
			}
			
		});
	
	</script>
		
</body>
</html>























