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

@WebServlet(value = "/admin/listlog.do")
public class ListLog extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//ListLog.java
		
		String id = req.getParameter("id");
		
		LogDAO dao = new LogDAO();
		
		List<LogDTO> list = dao.listLog(id);
		
		resp.setContentType("application/json");
		
		JSONArray arr = new JSONArray();
		
		for (LogDTO dto : list) {
			
			JSONObject obj = new JSONObject();
			
			obj.put("cnt", dto.getCnt());
			obj.put("url", dto.getUrl());
			
			arr.add(obj);
		}
		
		resp.getWriter().print(arr.toString());
		resp.getWriter().close();
		
	}

}







