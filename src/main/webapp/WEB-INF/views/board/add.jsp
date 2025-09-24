<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/toy/asset/css/tagify.css">
	<script src="/toy/asset/js/tagify.js"></script>
</head>
<body>
	<!-- add.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<div id="main">
		<h1>게시판 <small>쓰기</small></h1>
		
		<form method="POST" action="/toy/board/add.do" enctype="multipart/form-data">
		<table class="vertical">
			<tr>
				<th>제목</th>
				<td><input type="text" name="subject" id="subject" required class="full"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea name="content" id="content" required class="full"></textarea></td>
			</tr>
			<tr>
				<th>장소</th>
				<td><input type="file" name="attach" class="full" accept="image/*"></td>
			</tr>
			<tr>
				<th>해시태그</th>
				<td><input type="text" name="hashtag" id="hashtag" class="full"></td>
			</tr>
			<tr>
				<th>비밀글</th>
				<td>
					<label>
						<input type="checkbox" name="secret" value="1">
						비밀글(작성자 혹은 관리자만 열람 가능)
					</label>
				</td>
			</tr>
			<c:if test="${lv == '2'}">
			<tr>
				<th>공지글</th>
				<td>
					<label>
						<input type="checkbox" name="notice" value="1">
						공지글(관리자만 작성 가능)
					</label>
				</td>
			</tr>
			</c:if>
		</table>
		<div>
			<button type="button" class="back" onclick="location.href='/toy/board/list.do';">돌아가기</button>
			<button type="submit" class="add primary">쓰기</button>
		</div>
		</form>
	</div>
	
	<script>
	
		new Tagify(document.querySelector('#hashtag'));
		
		//window.onclick = function() {
			//[{"value":"강아지"},{"value":"고양이"},{"value":"병아리"}]
		//	alert(document.querySelector('#hashtag').value);
		//};
	
	</script>

</body>
</html>























