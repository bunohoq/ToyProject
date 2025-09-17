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
		
		

		//List.java
		BoardDAO dao = new BoardDAO();
		
		java.util.List<BoardDTO> list = dao.list(map);
		
		
		//데이터 조작
		Calendar now = Calendar.getInstance();
		String nowDate = String.format("%tF", now); //2025-09-17
		
		for (BoardDTO dto : list) {
			
			//날짜 조작 > 오늘 날짜?
			String regdate = dto.getRegdate();
			
			if (regdate.startsWith(nowDate)) {
				//System.out.println("오늘 쓴 글");
				dto.setRegdate(regdate.substring(11,16));
			} else {
				//System.out.println("과거 쓴 글");
				dto.setRegdate(regdate.substring(0, 10));
			}
			
			
			//제목 자르기
			String subject = dto.getSubject();
			
			if (subject.length() > 15) {
				subject = subject.substring(0, 10) + "..";
			}
			
			//태그 비활성화
			subject = subject.replace("<", "&lt;").replace(">", "&gt;");
			
			dto.setSubject(subject);			
			
		}//for
		
		
		req.setAttribute("list", list);
		req.setAttribute("map", map);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		dispatcher.forward(req, resp);
	}

}






