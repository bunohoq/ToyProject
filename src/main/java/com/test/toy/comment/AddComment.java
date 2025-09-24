package com.test.toy.comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.CommentDTO;

@WebServlet(value = "/board/addcomment.do")
public class AddComment extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AddComment.java
		
		HttpSession session = req.getSession();
		
		String content = req.getParameter("content");
		String bseq = req.getParameter("bseq");
		String id = session.getAttribute("id").toString();
		
		BoardDAO dao = new BoardDAO();
		
		CommentDTO dto = new CommentDTO();
		dto.setContent(content);
		dto.setBseq(bseq);
		dto.setId(id);
		
		int result = dao.addComment(dto); //댓글 쓰기
		
		CommentDTO dto2 = dao.getComment(); //방금 쓴 댓글 가져오기 
		
		resp.setContentType("application/json");
		
		/*
			
			{
				"result": 1,
				"dto": {
					"seq": 5,
					"name": "홍길동",
					"id": "hong",
					"content": "댓글내용",
					"regdate": "날짜"
				}
			}
		
		*/
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		JSONObject subObj = new JSONObject();
		subObj.put("seq", dto2.getSeq());
		subObj.put("name", dto2.getName());
		subObj.put("id", dto2.getId());
		subObj.put("content", dto2.getContent());
		subObj.put("regdate", dto2.getRegdate());
		
		obj.put("dto", subObj);
		
		//System.out.println(obj.toString());
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
	}

}





