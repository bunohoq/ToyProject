package com.test.toy.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.test.util.DBUtil;

public class Dummy {
	
	private static DBUtil util;
	private static Connection conn;
	private static Statement stat;
	private static PreparedStatement pstat;
	private static ResultSet rs;

	static {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		m1();
		
	}

	private static void m1() {
		
		//게시물 여러개 등록
		//queryParamNoReturn
		try {

			String sql = "insert into tblBoard (seq, subject, content, id, regdate, readcount) values (seqBoard.nextVal, ?, ?, ?, default, default)";
			pstat = conn.prepareStatement(sql);
			pstat.setString(2, "내용");
			pstat.setString(3, "cat");

			for (int i=0; i<250; i++) {				
				pstat.setString(1, "게시판 페이징입니다." + i);
				pstat.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}










