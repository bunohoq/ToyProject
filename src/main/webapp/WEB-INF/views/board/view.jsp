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
	<!-- view.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<div id="main">
		<h1>게시판 <small>보기</small></h1>
		
		<table class="vertical" id="view">
			<tr>
				<th>번호</th>
				<td>${dto.seq}</td>
			</tr>
			<tr>
				<th>이름</th>
				<td>${dto.name}(${dto.id})</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${dto.subject}</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>${dto.content}</td>
			</tr>
			<c:if test="${not empty dto.attach}">
			<tr>
				<th>장소</th>
				<td><img src="/toy/asset/place/${dto.attach}" id="imgPlace"></td>
			</tr>
			<c:if test="${not empty lat}">
			<tr>
				<th>위치</th>
				<td>
					<div id="map"></div>
					<%-- ${lat}, ${lng} --%>
				</td>
			</tr>
			</c:if>
			</c:if>
			<tr>
				<th>날짜</th>
				<td>${dto.regdate}</td>
			</tr>
			<tr>
				<th>조회수</th>
				<td>${dto.readcount}</td>
			</tr>
			<tr>
				<th>해시태그</th>
				<td><input type="text" id="hashtag" class="full"></td>
			</tr>
		</table>
		
		<!-- 댓글 목록 -->
		<table id="comment">
			<c:forEach items="${clist}" var="cdto">
			<tr>
				<td class="commentContent">
					<div>${cdto.content}</div>
					<div>${cdto.regdate}</div>
				</td>
				<td class="commentInfo">
					<div>
						<div>${cdto.name}(${cdto.id})</div>
						<c:if test="${not empty id && (id == cdto.id || lv == '2')}">
						<div>
							<span class="material-symbols-outlined" onclick="del(${cdto.seq});">delete</span>
							<span class="material-symbols-outlined" onclick="edit(${cdto.seq});">edit_note</span>
						</div>
						</c:if>
					</div>
				</td>
			</tr>
			</c:forEach>
		</table>
		
		<div id="loading" style="text-align: center; display: none;">
			<img src="/toy/asset/images/loading.gif">
		</div>
		
		<div style="text-align: center;">
			<button type="button" class="comment" id="btnMoreComment">댓글 더보기</button>
		</div>
		
		<!-- 댓글 쓰기 -->
		<form id="addCommentForm">
		<table id="addComment">
			<tr>
				<td><input type="text" name="content" class="full" required></td>
				<td><button type="button" class="comment" id="btnAddComment">댓글 쓰기</button></td>
			</tr>
		</table>
		</form>
		
		<div>
			<button type="button" class="back" onclick="location.href='/toy/board/list.do?column=${column}&word=${word}';">돌아가기</button>
			<!-- <button type="button" class="back" onclick="history.back();">돌아가기</button> -->
			
			<%-- 
			${id}
			${dto.id} 
			--%>
			
			<c:if test="${not empty id and (id == dto.id || lv == '2')}">
			<button type="button" class="edit primary" onclick="location.href='/toy/board/edit.do?seq=${dto.seq}';">수정하기</button>
			<button type="button" class="del primary" onclick="location.href='/toy/board/del.do?seq=${dto.seq}';">삭제하기</button>
			</c:if>
			
			<c:if test="${not empty id && id != dto.id}">
			<button type="button" class="favorite primary" 
					onclick="scrap(${dto.seq});">즐겨찾기</button>
			</c:if>
			
			<button type="button" class="bookmark primary" 
					onclick="copy();">공유하기</button>
			
		</div>

	</div>
	
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c7aebadc3646802527c08622383bc565"></script>
	<script>
	
		$('#btnAddComment').click(() => {
			
			/* 
			$.ajax({
				type: 'POST',
				url: '/toy/board/addcomment.do',
				data: {
					content: $('input[name=content]').val(),
					bseq: ${dto.seq}
				},
				dataType: 'json',
				success: function(result) {
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			}); 
			*/
			
			$.post('/toy/board/addcomment.do', {
				content: $('input[name=content]').val(),
				bseq: ${dto.seq}
			}, function(result) {
				
				//alert(result.result);
				//alert(result.dto);
				//댓글 목록 갱신
				
				//새로 작성한 댓글을 화면에 동적 추가
				let temp = `
				
					<tr>
						<td class="commentContent">
							<div>\${result.dto.content}</div>
							<div>\${result.dto.regdate}</div>
						</td>
						<td class="commentInfo">
							<div>
								<div>\${result.dto.name}(\${result.dto.id})</div>
								<div>
									<span class="material-symbols-outlined" onclick="del(\${result.dto.seq});">delete</span>
									<span class="material-symbols-outlined" onclick="edit(\${result.dto.seq});">edit_note</span>
								</div>
							</div>
						</td>
					</tr>
				
				`;
				
				$('#comment tbody').prepend(temp);
				
				$('input[name=content]').val('');
				
				
			}, 'json').fail(function(a,b,c) {
				console.log(a,b,c);
			});
			
		});
		
		//처음: 1~10
		//버튼: 11~15
		//버튼: 16~20
		//버튼: 21~25
		
		let begin = 6;
		
		$('#btnMoreComment').click(() => {
			
			$('#loading').show();
			
			setTimeout(more, 1500);
			
		});
		
		function more() {
			
			$.get('/toy/board/morecomment.do', {
				bseq: ${dto.seq},
				begin: begin
			}, function (result) {
				
				if (result.length > 0) {
					
					//댓글 5개를 화면에 출력
					result.forEach(obj => {
						
						let temp = `
							
							<tr>
								<td class="commentContent">
									<div>\${obj.content}</div>
									<div>\${obj.regdate}</div>
								</td>
								<td class="commentInfo">
									<div>
										<div>\${obj.name}(\${obj.id})</div>
							`;
							
							//익명: if ('') > false
							//인증: if ('hong') > true
							
							if ('${id}' && ('${id}' == obj.id || ${lv == 2})) {
							temp += `	<div>
											<span class="material-symbols-outlined" onclick="del(\${obj.seq});">delete</span>
											<span class="material-symbols-outlined" onclick="edit(\${obj.seq});">edit_note</span>
										</div>
							`;		
							}
										
							temp += `</div>
								</td>
							</tr>
						
						`;
						
						$('#comment tbody').append(temp);
						
					});//for
					
					
					
					
					begin += 5;
					
				} else {
					alert('더 이상 가져올 댓글이 없습니다.');
				}
				
				$('#loading').hide();
				
			}, 'json')
			.fail(function(a,b,c) {
				console.log(a,b,c);
			});
		}
		
		
		function edit(seq) {
			
			$('.commentEditRow').remove();
			
			//let content = '수정할 댓글입니다.';
			let content = $(event.target).parents('tr').children().eq(0).children().eq(0).text();
			
			$(event.target).parents('tr').after(`
					
				<tr class="commentEditRow">
					<td><input type="text" name="content" class="full" required value="\${content}" id="txtComment"></td>
					<td class="commentEdit">
						<span class="material-symbols-outlined" onclick="editComment(\${seq});">edit_square</span>
						<span class="material-symbols-outlined" onclick="$(event.target).parents('tr').remove();">close</span>
					</td>
				</tr>
					
			`);
			
		}
		
		function editComment(seq) {
			
			//alert(seq);
			//alert($('#txtComment').val());
			
			let div = $(event.target).parents('tr').prev().children().eq(0).children().eq(0);
			let tr = $(event.target).parents('tr');
			
			
			$.post('/toy/board/editcomment.do', {
				seq: seq,
				content: $('#txtComment').val()
			}, function (result) {
				
				if (result.result == '1') {
					//alert('성공');
					div.text($('#txtComment').val());
					tr.remove();
					
				} else {
					alert('댓글 수정을 실패했습니다.');
				}
				
			}, 'json')
			.fail(function (a,b,c) {
				console.log(a,b,c);
			});
			
		}
		
		function del(seq) {
			
			$('.commentEditRow').remove();
			
			let tr = $(event.target).parents('tr');
			
			if (confirm('삭제하겠습니까?')) {
				
				//$.ajax();
				
				$.post('/toy/board/delcomment.do', {
					seq: seq
				}, function (result) {
					
					if (result.result == '1') {
						//alert('성공');
						
						//$(event.target).parents('tr').remove();
						//console.log(event.target);
						tr.remove(); //클로저
						
					} else {
						alert('댓글 삭제를 실패했습니다.');
					}
					
				}, 'json')
				.fail(function(a,b,c) {
					console.log(a,b,c);
				});
				
			}
			
		}
		
		
		
		<c:if test="${not empty lat}">
		
		var container = document.getElementById('map');
		
		var options = { 
			center: new kakao.maps.LatLng(37.499349, 127.033245),
			level: 3 
		};
	
		var map = new kakao.maps.Map(container, options); 
		
		
		const path = '/toy/asset/images/studio.png';
		const size = new kakao.maps.Size(64, 64);
		const op = { offset: new kakao.maps.Point(32, 64) };
		const img = new kakao.maps.MarkerImage(path, size, op);
		
		const m1 = new kakao.maps.Marker({
			position: new kakao.maps.LatLng(${lat}, ${lng}),
			image: img
		});
		
		m1.setMap(map);
		map.panTo(m1.getPosition());
		
		</c:if>
		
		
		//해시 태그
		let taglist = '';
		<c:forEach items="${dto.hashtag}" var="tag">
		taglist += '${tag},';
		</c:forEach>
		
		$('#hashtag').val(taglist);
		
		const tagify = new Tagify(document.getElementById('hashtag'));
		
		tagify.on('click', (e) => {
			
			//alert(e.detail.data.value);
			
			location.href = '/toy/board/list.do?tag=' + e.detail.data.value;
			
		}); 
		
		
		function scrap(seq) {
			//alert(seq);
			
			$.ajax({
				type: 'POST',
				url: '/toy/board/scrapbook.do',
				data: {
					seq: seq
				},
				dataType: 'json',
				success: function(result) {
					
					if (result.result == '0') {
						//등록O
						$('.favorite')
						.css('background-color', 'rgba(255,0,0,.3)');
					} else {
						//등록X
						$('.favorite')
						.css('background-color', 'var(--primary-color)');
					}
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		}
		
		
		if (${dto.scrapbook}) {
			$('.favorite').css('background-color', 'rgba(255,0,0,.3)');
		}
		
		
		function copy() {
			
			let btn = event.target;
			
			window.navigator.clipboard.writeText(location.href).then(() => {
				//alert('주소 복사됨');
				$(btn).text('공유하기(복사됨)');
				//$(btn).hide();
			});
			
		}
		
	</script>
	
		
</body>
</html>























