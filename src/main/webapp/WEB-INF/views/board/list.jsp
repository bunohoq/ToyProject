<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
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
		<h1>게시판 
			<c:if test="${map.search == 'n'}">
			<small>목록</small>
			</c:if>
			
			<c:if test="${map.search == 'y'}">
			<small>검색</small>
			</c:if>
		</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>
		
		<%-- 
		<div id="pagebar">
			<span>페이지: </span>
			<input type="number" class="short" id="page" value="${map.nowPage}" min="1" max="${map.totalPage}">
			<input type="button" value="이동하기" onclick="location.href='/toy/board/list.do?page=' + $('#page').val();">
		</div> 
		--%>
		
		
		<%-- 
		<div id="pagebar">
			<select onchange="location.href='/toy/board/list.do?page=' + $(this).val() + '&column=${map.column}&word=${map.word}';">
				<c:forEach begin="1" end="${map.totalPage}" var="i">
				<option value="${i}" <c:if test="${map.nowPage == i}">selected</c:if>>${i}페이지</option>
				</c:forEach>
			</select>
		</div>  
		--%>
		
		
		<table id="list">
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>이름</th>
				<th>날짜</th>
				<th>조회수</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
			
			<c:if test="${dto.notice == '0'}">
			<tr>
			</c:if>
			
			<c:if test="${dto.notice == '1' }">
			<tr style="background-color: rgba(255,0,0,.03);">
			</c:if>
			
			
				<td>
				
					<c:if test="${dto.notice == '0'}">
						${dto.seq}
					</c:if>
					
					<c:if test="${dto.notice == '1'}">
						<span class="material-symbols-outlined" style="display: block;">volume_up</span>
					</c:if>
					
				</td>
				<td>
				
					<c:if test="${dto.secret == '1'}">
						<span class="material-symbols-outlined" style="font-size: 16px;">lock</span>
					</c:if>
				
					<!-- 제목 -->
					<!--  
						1. 그냥글 > 모두 공개
						2. 비밀글 > 작성자
						3. 비밀글 > 작성자가 아닌 사람
					-->
					<c:if test="${dto.secret == '0' || (dto.secret == '1' && (id == dto.id || lv == '2' ))}">
						<a href="/toy/board/view.do?seq=${dto.seq}&column=${map.column}&word=${map.word}">${dto.subject}</a>
					</c:if>
										
					<c:if test="${dto.secret == '1' && id != dto.id && lv != '2'}">
						<a href="#!" onclick="alert('작성자만 열람이 가능합니다.');">비밀글입니다.</a>
					</c:if>
					
					
					<!-- 댓글 개수 -->
					<c:if test="${dto.commentCount > 0}">
					<span class="commentCount">
						<span class="material-symbols-outlined">chat</span>
						${dto.commentCount}
					</span>
					</c:if>
					
					<!-- 최신글 표시 -->
					<c:if test="${dto.isnew < 1}">
						<span class="isnew">new</span>
					</c:if>
					
				</td>
				<td>${dto.name}</td>
				<td>
					<%-- 
					<fmt:parseDate value="${dto.regdate}" var="regdate" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate>
					<fmt:formatDate value="${regdate}" pattern="yyyy-MM-dd" /> 
					--%>
					${dto.regdate}	
				</td>
				<td>${dto.readcount}</td>
			</tr>
			</c:forEach>
		</table>
		
		<!-- 검색 -->
		<form id="searchForm" method="GET" action="/toy/board/list.do">
			<select name="column">
				<option value="subject">제목</option>
				<option value="content">내용</option>
				<option value="name">이름</option>
			</select>
			<input type="text" name="word" class="long" required>
			<input type="submit" value="검색하기">	
		</form>		
		
		<!-- 페이지바 -->
		<div id="pagebar">${pagebar}</div>
		
		<div>
			<c:if test="${not empty id}">
			<button type="button" class="add primary" onclick="location.href='/toy/board/add.do';">쓰기</button>
			</c:if>
		</div>
		
	</div>
	
	<script>
	
		<c:if test="${map.search == 'y'}">
		$('select[name=column]').val('${map.column}');
		$('input[name=word]').val('${map.word}');
		</c:if>
	
	</script>
		
</body>
</html>























