package com.test.toy.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.test.toy.user.model.UserDTO;
import com.test.util.DBUtil;

public class LogDAO {

	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public LogDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<UserDTO> listUser() {
		
		//queryNoParamListReturn
		try {
					
			String sql = "select * from tblUser order by name asc";
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<UserDTO> list = new ArrayList<UserDTO>();
			
			while (rs.next()) {
				
				UserDTO dto = new UserDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));				
				
				list.add(dto);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	public List<LogDTO> listLog(LogDTO dto) {
		
		//queryParamListReturn
		try {
					
			String sql = "select to_char(regdate, 'yyyy-mm-dd') as regdate, count(*) as lcnt, (select count(*) from tblBoard where to_char(regdate, 'yyyy-mm-dd') = to_char(l.regdate, 'yyyy-mm-dd') and id = ?) as bcnt, (select count(*) from tblComment where to_char(regdate, 'yyyy-mm-dd') = to_char(l.regdate, 'yyyy-mm-dd') and id = ?) as ccnt from tblLog l where to_char(regdate, 'yyyy-mm') = ? and id = ? group by to_char(regdate, 'yyyy-mm-dd')";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getId());
			pstat.setString(2, dto.getId());
			pstat.setString(3, dto.getRegdate());
			pstat.setString(4, dto.getId());
			
			rs = pstat.executeQuery();
			
			List<LogDTO> list = new ArrayList<LogDTO>();
			
			while (rs.next()) {
				
				LogDTO result = new LogDTO();
				
				result.setLcnt(rs.getString("lcnt"));
				result.setBcnt(rs.getString("bcnt"));
				result.setCcnt(rs.getString("ccnt"));
				result.setRegdate(rs.getString("regdate"));
				
				list.add(result);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public List<LogDTO> listLog(String id) {
		
		//queryParamListReturn
		try {
					
			String sql = "select count(*) as cnt, url from tblLog where id = ? group by url order by count(*) desc";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			
			rs = pstat.executeQuery();
			
			List<LogDTO> list = new ArrayList<LogDTO>();
			
			while (rs.next()) {
				
				LogDTO dto = new LogDTO();
				
				dto.setCnt(rs.getString("cnt"));
				
				//- board/list.do > "게시판 목록보기"
				dto.setUrl(rs.getString("url").substring(5));
				
				list.add(dto);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}








