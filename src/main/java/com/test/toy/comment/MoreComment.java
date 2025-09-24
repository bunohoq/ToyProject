package com.test.toy.comment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.CommentDTO;

@WebServlet(value = "/board/morecomment.do")
public class MoreComment extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		

		//MoreComment.java
		String bseq = req.getParameter("bseq");
		String begin = req.getParameter("begin"); //end = begin + 5
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("bseq", bseq);
		map.put("begin", begin);
		
		BoardDAO dao = new BoardDAO();
		
		List<CommentDTO> clist = dao.moreComment(map);
		
		JSONArray arr = new JSONArray();
		
		for (CommentDTO dto : clist) {
			//DTO 1개 > JSONObject 1개
			JSONObject obj = new JSONObject();
			
			obj.put("seq", dto.getSeq());
			obj.put("content", dto.getContent());
			obj.put("id", dto.getId());
			obj.put("name", dto.getName());
			obj.put("regdate", dto.getRegdate());
			
			arr.add(obj);
		}
		
		resp.setContentType("application/json");
		
		resp.getWriter().print(arr.toString());
		resp.getWriter().close();

	}

}



