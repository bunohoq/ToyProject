package com.test.toy.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.BoardDTO;

@WebServlet(value = "/board/scrapbook.do")
public class ScrapBook extends HttpServlet {
	
	//doGet > 브라우저 호출(링크)
	//doPost > Ajax 호출
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//tblScrapBook
		BoardDAO dao = new BoardDAO();
		
		List<BoardDTO> list = dao.listScrapBook(req.getSession().getAttribute("id").toString());
		
		req.setAttribute("list", list);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/scrapbook.jsp");
		dispatcher.forward(req, resp);
	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//ScrapBook.java
		
		//1. 10번글 > 즐겨찾기 클릭
		//1.5 조회 > select
		//2.1 등록
		//	2.1.1 insert
		//2.2 해제
		//	2.2.1 delete
		
		String bseq = req.getParameter("seq");
		String id = req.getSession().getAttribute("id").toString();
		
		BoardDAO dao = new BoardDAO();
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("bseq", bseq);
		map.put("id", id);
		
		//1.5 조회
		int result = dao.getScrapBook(map); //count(*) > 1, 0
		
		if (result > 0) {			
			//2.2 해제
			dao.delScrapBook(map);
		} else {			
			//2.1 등록
			dao.addScrapBook(map);
		}
		
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
		
	}

}







