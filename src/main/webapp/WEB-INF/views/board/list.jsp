<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
</head>
<body>
	<!-- list.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	
	<div id="main">
		<h1>게시판 <small>목록</small></h1>
	
		<div>
			<button type="button" class="add primary" onclick="location.href='/toy/board/add.do';">쓰기</button>
		</div>
	</div>
	
</body>
</html>

