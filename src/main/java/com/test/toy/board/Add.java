package com.test.toy.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.BoardDTO;

@WebServlet(value = "/board/add.do")
public class Add extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Add.java
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/add.jsp");
		dispatcher.forward(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//AddOk.java 역할
		//req.setCharacterEncoding("UTF-8"); > 필터를 통해서 구현
		
		//req > multi
		MultipartRequest multi = new MultipartRequest(
							req,
							req.getServletContext().getRealPath("/asset/place"),
							1024 * 1024 * 30,
							"UTF-8",
							new DefaultFileRenamePolicy()
						);
		
		System.out.println(req.getServletContext().getRealPath("/asset/place"));
		
		
		
		
		
		
		String subject = multi.getParameter("subject");
		String content = multi.getParameter("content");
		String attach = multi.getFilesystemName("attach");
		String hashtag = multi.getParameter("hashtag");
		String secret = multi.getParameter("secret"); //1 or null
		if (secret == null) secret = "0";
		
		String notice = multi.getParameter("notice"); //1 or null
		if (notice == null) notice = "0";
		
		//System.out.println(subject);
		//System.out.println(content);
		//System.out.println(hashtag == null);
		//System.out.println(hashtag.equals(""));
		
		//[{"value":"게시판"},{"value":"태그"},{"value":"JSP"},{"value":"프로젝트"}]
		
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		dto.setSubject(subject);
		dto.setContent(content);
		dto.setAttach(attach);
		dto.setSecret(secret);
		dto.setNotice(notice);
		
		HttpSession session = req.getSession();
		dto.setId(session.getAttribute("id").toString());
		
		
		int result = dao.add(dto);
		String bseq = dao.getBseq();
		
		
		//해시태그
		if (!hashtag.equals("")) {
			
			//[{"value":"게시판"},{"value":"태그"},{"value":"JSP"}]
			try {
				
				JSONParser parser = new JSONParser();
				JSONArray arr = (JSONArray)parser.parse(hashtag);
				
				for (Object obj : arr) {
					
					JSONObject tag = (JSONObject)obj;
					String tagName = tag.get("value").toString();
					//System.out.println(tagName);
					
					//해시태그 > DB 추가
					String hseq = dao.addHashtag(tagName);
					
					//연결 테이블 > 관계 추가
					Map<String,String> map = new HashMap<String,String>();
					map.put("bseq", bseq);
					map.put("hseq", hseq);
					
					dao.addTagging(map);
					
				}
				
			} catch (Exception e) {
				System.out.println("Add.doPost()");
				e.printStackTrace();
			}
			
		}
		
		
		if (result > 0) {
			resp.sendRedirect("/toy/board/list.do");
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}
	

}











