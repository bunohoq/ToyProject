package com.test.toy.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.test.toy.admin.model.LogDAO;
import com.test.toy.admin.model.LogDTO;

@WebServlet(value = "/admin/loadlog.do")
public class LoadLog extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//LoadLog.java
		String year = req.getParameter("year");
		String month = req.getParameter("month");
		String id = req.getParameter("id");
		
		LogDAO dao = new LogDAO();
		
		LogDTO dto = new LogDTO();
		dto.setId(id);
		dto.setRegdate(String.format("%d-%02d"
									, Integer.parseInt(year)
									, Integer.parseInt(month))); //"2025-09"
		
		List<LogDTO> list = dao.listLog(dto);
		
		//System.out.println(list);
		//[LogDTO(id=null, lcnt=17, bcnt=3, ccnt=2, regdate=2025-09-23), LogDTO(id=null, lcnt=11, bcnt=2, ccnt=0, regdate=2025-09-22)]
		
		resp.setContentType("application/json");
		
		JSONArray arr = new JSONArray();
		
		for (LogDTO result : list) {
			
			JSONObject obj = new JSONObject();
			
			obj.put("regdate", result.getRegdate());
			obj.put("lcnt", result.getLcnt());
			obj.put("bcnt", result.getBcnt());
			obj.put("ccnt", result.getCcnt());
			
			arr.add(obj);
		}
		
		resp.getWriter().print(arr.toString());
		resp.getWriter().close();
		
	}

}








