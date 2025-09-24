package com.test.toy.board;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.test.toy.board.model.BoardDAO;
import com.test.toy.board.model.BoardDTO;
import com.test.toy.board.model.CommentDTO;

@WebServlet(value = "/board/view.do")
public class View extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		
		
		
		//View.java
		
		String seq = req.getParameter("seq");
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		
		BoardDAO dao = new BoardDAO();
		
		if (session.getAttribute("read") != null && session.getAttribute("read").toString().equals("n")) {
			//조회수 증가
			dao.updateReadcount(seq);
			session.setAttribute("read", "y");
		}
		
		String id = "";
		
		if (session.getAttribute("id") != null) {
			id = session.getAttribute("id").toString();
		}
		
		BoardDTO dto = dao.get(seq, id);
		
		
		
		//비밀글 열람 통제
		//- 작성자 or 관리자
		if (dto.getSecret().equals("1")
			&& (!session.getAttribute("id").toString().equals(dto.getId()) 
					&& session.getAttribute("lv").toString().equals("1"))) {
			resp.sendRedirect("/toy/board/list.do");
			
			//응답이 이미 커밋된 후에는 forward할 수 없습니다.
			return;
		}
		
		
		//데이터 조작
		String subject = dto.getSubject();
		
		subject = subject.replace("<", "&lt;").replace(">", "&gt;");
		dto.setSubject(subject);
		
		String content = dto.getContent();
		
		content = content.replace("<", "&lt;").replace(">", "&gt;");
		dto.setContent(content);
		
		
		//검색어 부각시키기
		//- 오늘 자바 수업 중입니다.
		//- 오늘 <span style="background-color:gold;color:tomato;">자바</span> 수업 중입니다.
		
		//검색중 + 내용 검색
		if (((column != null && word != null)
			|| (!column.equals("") && !word.equals("")))
				&& column.equals("content")) {
			
			content = content.replace(word, "<span style='background-color:gold;color:tomato;'>" + word + "</span>");
			dto.setContent(content);
			
		}
		
		
		
		//댓글 가져오기
		List<CommentDTO> clist = dao.listComment(seq);
		
		
		
		
		
		//첨부파일 > 메타정보 가져오기
		if (dto.getAttach() != null
			&& (dto.getAttach().toLowerCase().endsWith(".jpg")
			|| dto.getAttach().toLowerCase().endsWith(".jpeg")
			|| dto.getAttach().toLowerCase().endsWith(".gif")
			|| dto.getAttach().toLowerCase().endsWith(".png"))) {
			
			//BufferedImage img = ImageIO.read(new File(req.getServletContext().getRealPath("/asset/place") + "/" + dto.getAttach()));
			
			//System.out.println(img.getWidth());
			//System.out.println(img.getHeight());
			
			//GPS
			try {
				
				Metadata metadata = ImageMetadataReader.readMetadata(new File(req.getRealPath("/asset/place") + "/" + dto.getAttach()));
				
				GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
				
				if (gps.containsTag(GpsDirectory.TAG_LATITUDE)
					  && gps.containsTag(GpsDirectory.TAG_LONGITUDE)) {
					
					req.setAttribute("lat", gps.getGeoLocation().getLatitude());
					req.setAttribute("lng", gps.getGeoLocation().getLongitude());
					
				}
				
			} catch (Exception e) {
				System.out.println("View.doGet()");
				e.printStackTrace();
			}
			
		}
		
		
		
		
		
		req.setAttribute("dto", dto);
		req.setAttribute("column", column);
		req.setAttribute("word", word);
		req.setAttribute("clist", clist);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
		dispatcher.forward(req, resp);
	}

}






