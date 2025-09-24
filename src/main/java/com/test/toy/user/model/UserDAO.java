package com.test.toy.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.test.util.DBUtil;

public class UserDAO {

	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public UserDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int register(UserDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblUser (id, pw, name, email, lv, pic, intro, regdate, ing) values (?, ?, ?, ?, 1, ?, ?, default, default)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getId());
			pstat.setString(2, dto.getPw());
			pstat.setString(3, dto.getName());
			pstat.setString(4, dto.getEmail());
			pstat.setString(5, dto.getPic());
			pstat.setString(6, dto.getIntro());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public UserDTO login(UserDTO dto) {
		
		//queryParamDTOReturn
		try {
					
			String sql = "select * from tblUser where id = ? and pw = ? and ing = 1";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getId());
			pstat.setString(2, dto.getPw());
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				
				UserDTO result = new UserDTO();
				
				result.setId(rs.getString("id"));
				result.setName(rs.getString("name"));
				result.setEmail(rs.getString("email"));
				result.setLv(rs.getString("lv"));
				result.setPic(rs.getString("pic"));
				result.setIntro(rs.getString("intro"));
				result.setIng(rs.getString("ing"));
				
				return result;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	public int unregister(String id) {
		
		//queryParamNoReturn
		try {

			String sql = "update tblUser set pw = '0000', name = '익명', email = '0000', lv = 1, pic = 'pic.png', intro = '0000', ing = 0 where id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public void addLog(UserLogDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblLog (seq, id, regdate, url) values (seqLog.nextVal, ?, default, ?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getId());
			pstat.setString(2, dto.getUrl());

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}








