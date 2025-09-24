<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     

<!-- header.jsp -->
<header>
	<h1>
		<c:if test="${empty id}">
		<span>Toy</span>
		</c:if>
		
		<c:if test="${not empty id}">
		<span class="material-symbols-outlined">toys</span>
		</c:if>
		
		<c:if test="${not empty id and lv == '1'}">
		<span style="color: cornflowerblue;">Toy</span>
		</c:if>
		
		<c:if test="${not empty id and lv == '2'}">
		<span style="color: tomato;">Toy</span>
		</c:if>
		 
		Project
	</h1>
	<ul>
		<li><a href="/toy/index.do">Home</a></li>
		
		<c:if test="${empty id}">
		<li><a href="/toy/user/register.do">Register</a></li>
		<li><a href="/toy/user/login.do">Login</a></li>
		</c:if>		
		
		<c:if test="${not empty id}">
		<li><a href="/toy/user/unregister.do">Unregister</a></li>
		<li><a href="/toy/user/logout.do">Logout</a></li>
		<li><a href="/toy/user/info.do">Info</a></li>		
		</c:if>
		
		<li><a href="/toy/board/list.do">Board</a></li>
		<li><a href="/toy/board/scrapbook.do">ScrapBook</a></li>
		
		<c:if test="${not empty id && lv == '2'}">
		<li><a href="/toy/admin/log.do">Log</a></li>
		</c:if>			
		
		<!--
		<li><a href=""></a></li>
		-->
		
	</ul>
</header>	








