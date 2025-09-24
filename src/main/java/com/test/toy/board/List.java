package com.test.toy.board;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.BoardDTO;

@WebServlet(value = "/board/list.do")
public class List extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//목록보기
		//- /toy/board/list.do
		//검색 결과보기
		//- /toy/board/list.do?column=subject&word=java
		
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		String search = "n"; //목록보기(n), 검색하기(y)
		
		if ((column == null && word == null) || word.trim().equals("")) {
			search = "n";
		} else {
			search = "y";
		}
		
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("column", column);
		map.put("word", word);
		map.put("search", search);
		
		
			
		
		HttpSession session = req.getSession();
		
		//조회수 증가 방지
		session.setAttribute("read", "n");
		
		
		
		//페이징
		//- list.do > 1페이지
		//- list.do?page=1
		//- list.do?page=2
		
		String page = req.getParameter("page");

		int nowPage = 0;	//현재 페이지 번호
		int totalCount = 0; //총 게시물 수
		int pageSize = 10;	//한 페이지에서 보여줄 게시물 수
		int totalPage = 0;	//총 페이지 수
		int begin = 0;		//페이징 시작 위치
		int end = 0;		//페이지 끝 위치
		int n = 0;			//페이지바의 페이지 번호
		int loop = 0;		//페이지바 루프변수
		int blockSize = 10;	//페이지바의 페이지 수
		
		if (page == null || page.equals("")) {
			nowPage = 1;
		} else {
			nowPage = Integer.parseInt(page);
		}

		//- list.do?page=1 > where rnum between 1 and 10
		//- list.do?page=2 > where rnum between 11 and 20
		//- list.do?page=3 > where rnum between 21 and 30
		begin = ((nowPage - 1) * pageSize) + 1;
		end = begin + pageSize - 1;
		
		map.put("begin", begin + "");
		map.put("end", end + "");
		map.put("nowPage", nowPage + "");
			

		//List.java
		BoardDAO dao = new BoardDAO();
		
		
		//총 게시물 수?
		totalCount = dao.getTotalCount(map);
		//System.out.println(totalCount);
		//262 / 10 = 26.2 > 27
		totalPage = (int)Math.ceil((double)totalCount / pageSize); 
		
		map.put("totalCount", totalCount + "");
		map.put("totalPage", totalPage + "");
		
		
		
		//해시 태그
		String tag = req.getParameter("tag");
		map.put("tag", tag);
		
		
		java.util.List<BoardDTO> list = dao.list(map);
		
		
		//데이터 조작
		Calendar now = Calendar.getInstance();
		String nowDate = String.format("%tF", now); //2025-09-17
		
		for (BoardDTO dto : list) {
			
			//날짜 조작 > 오늘 날짜?
			String regdate = dto.getRegdate();
			
			if (regdate.startsWith(nowDate)) {
				//System.out.println("오늘 쓴 글");
				dto.setRegdate(regdate.substring(11));
			} else {
				//System.out.println("과거 쓴 글");
				dto.setRegdate(regdate.substring(0, 10));
			}
			
			
			//제목 자르기
			String subject = dto.getSubject();
			
			if (subject.length() > 15) {
				subject = subject.substring(0, 15) + "..";
			}
			
			//태그 비활성화
			subject = subject.replace("<", "&lt;").replace(">", "&gt;");
			
			dto.setSubject(subject);			
			
		}//for
		
		
		
		
		//페이지바 생성
		String pagebar = "";
		
		/*
		for (int i=1; i<=totalPage; i++) { 
			pagebar += String.format(" <a href='/toy/board/list.do?page=%d'>%d</a> ", i, i); 
		}
		*/
		
		
		//list.do?page=1
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=2
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=10
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=11
		//[] 11 12 13 14 15 16 17 18 19 20 []
		
		//list.do?page=15
		//[] 11 12 13 14 15 16 17 18 19 20 []
		
		
		loop = 1; //루프 변수(10바퀴)
		n = ((nowPage - 1) / blockSize) * blockSize + 1; //페이지 번호
		
		
		//이전 10페이지
		if (n == 1) {
			pagebar += String.format(" <a href='#!'>[이전 %d페이지]</a> ", blockSize);
		} else {		
			pagebar += String.format(" <a href='/toy/board/list.do?page=%d'>[이전 %d페이지]</a> ", n - 1, blockSize);
		}
		
		
		while (!(loop > blockSize || n > totalPage)) {
			
			if (n == nowPage) {
				pagebar += String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n);
			} else {			
				pagebar += String.format(" <a href='/toy/board/list.do?page=%d' class='page'>%d</a> ", n, n);
			}
			
			loop++;
			n++;
		}
		
		
		//다음 10페이지
		if (n >= totalPage) {
			pagebar += String.format(" <a href='#!'>[다음 %d페이지]</a> ", blockSize);
		} else {		
			pagebar += String.format(" <a href='/toy/board/list.do?page=%d'>[다음 %d페이지]</a> ", n, blockSize);
		}
		
			
		req.setAttribute("list", list);
		req.setAttribute("map", map);
		req.setAttribute("pagebar", pagebar);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		dispatcher.forward(req, resp);
	}

		
}






