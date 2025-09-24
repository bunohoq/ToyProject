package com.test.toy.board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.util.DBUtil;

public class BoardDAO {

	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public BoardDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int add(BoardDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblBoard (seq, subject, content, id, regdate, readcount, attach, secret, notice) values (seqBoard.nextVal, ?, ?, ?, default, default, ?, ?, ?)";
			
			//현재 장소 > 특정 데이터 없어?
			//1. 이 장소에서 특정 데이터를 가져올 수 있는지? > 스스로
			//2. 현재 객체를 호출한쪽에서 특정 데이터를 전달해줄 수 있는지? > 전달
			
			//로그인한 유저의 아이디 > 세션

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getSubject());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getId());
			pstat.setString(4, dto.getAttach());
			pstat.setString(5, dto.getSecret());
			pstat.setString(6, dto.getNotice());
						
			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public List<BoardDTO> list(Map<String, String> map) {
		
		//queryNoParamListReturn
		try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("where %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
			
			if (map.get("tag") == null) {
				sql = String.format("select * from (select a.*, rownum as rnum from vwBoard a %s) where rnum between %s and %s"
								, where
								, map.get("begin")
								, map.get("end"));
			} else {
				sql = String.format("select * from (select a.*, rownum as rnum from vwBoard a ) b inner join tblTagging t on b.seq = t.bseq inner join tblHashtag h on h.seq = t.hseq where rnum between %s and %s and h.hashtag = '%s'"
						, map.get("begin")
						, map.get("end")
						, map.get("tag"));
			}
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setId(rs.getString("id"));
				dto.setReadcount(rs.getString("readcount"));
				dto.setRegdate(rs.getString("regdate"));
				
				dto.setName(rs.getString("name"));
				dto.setIsnew(rs.getDouble("isnew"));
				dto.setCommentCount(rs.getString("commentCount"));
				
				dto.setSecret(rs.getString("secret"));
				dto.setNotice(rs.getString("notice"));
				
				list.add(dto);		
			}	
			
			
			//공지사항 추가
			sql = "select * from vwNotice";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			while (rs.next()) {
			
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setId(rs.getString("id"));
				dto.setReadcount(rs.getString("readcount"));
				dto.setRegdate(rs.getString("regdate"));
				
				dto.setName(rs.getString("name"));
				dto.setIsnew(rs.getDouble("isnew"));
				dto.setCommentCount(rs.getString("commentCount"));
				
				dto.setSecret(rs.getString("secret"));
				dto.setNotice(rs.getString("notice"));
				
				list.add(0, dto);
			}			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	public BoardDTO get(String seq, String id) {
		
		//queryParamDTOReturn
		try {
					
			String sql = "select tblBoard.*, (select name from tblUser where id = tblBoard.id) as name, (select count(*) from tblScrapBook where bseq = tblBoard.seq and id = ?) as scrapbook from tblBoard where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			pstat.setString(2, seq);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setId(rs.getString("id"));
				dto.setReadcount(rs.getString("readcount"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setContent(rs.getString("content"));
				
				dto.setName(rs.getString("name"));
				dto.setAttach(rs.getString("attach"));
				
				dto.setSecret(rs.getString("secret"));
				
				dto.setScrapbook(rs.getString("scrapbook"));
				
				//해시태그 추가
				sql = "select h.hashtag\r\n"
						+ "from tblBoard b \r\n"
						+ "    inner join tblTagging t\r\n"
						+ "        on b.seq = t.bseq\r\n"
						+ "            inner join tblHashtag h\r\n"
						+ "                on h.seq = t.hseq\r\n"
						+ "    where b.seq = ?";
				
				pstat = conn.prepareStatement(sql);
				pstat.setString(1, seq);
				rs = pstat.executeQuery();
				
				List<String> tlist = new ArrayList<String>();
				
				while (rs.next()) {
					tlist.add(rs.getString("hashtag"));
				}
				
				dto.setHashtag(tlist);
				
				return dto;				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	public void updateReadcount(String seq) {
		
		//queryParamNoReturn
		try {

			String sql = "update tblBoard set readcount = readcount + 1 where seq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int edit(BoardDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "update tblBoard set subject = ?, content = ? where seq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getSubject());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getSeq()); //???

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int del(String seq) {
		
		//queryParamNoReturn
		try {

			String sql = "delete from tblBoard where seq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getTotalCount(Map<String, String> map) {
		
		//queryParamTokenReturn
		try {
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				where = String.format("where %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}

			String sql = "select count(*) as cnt from vwBoard " + where;
			
			pstat = conn.prepareStatement(sql);
		
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return 0;
	}

	public int addComment(CommentDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblComment (seq, content, id, regdate, bseq) values (seqComment.nextVal, ?, ?, default, ?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getContent());
			pstat.setString(2, dto.getId());
			pstat.setString(3, dto.getBseq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public List<CommentDTO> listComment(String bseq) {
		
		//queryParamListReturn
		try {
					
			String sql = "select * from (select tblComment.*, (select name from tblUser where id = tblComment.id) as name from tblComment where bseq = ? order by seq desc) where rownum <= 5";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, bseq);
			
			rs = pstat.executeQuery();
			
			ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();
			
			while (rs.next()) {
				
				CommentDTO dto = new CommentDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setBseq(rs.getString("bseq"));
				
				list.add(dto);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public CommentDTO getComment() {
		
		//queryNoParamDTOReturn
		try {
					
			String sql = "select tblComment.*, (select name from tblUser where id = tblComment.id) as name from tblComment where seq = (select max(seq) from tblComment)";
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			if (rs.next()) {
				
				CommentDTO dto = new CommentDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setBseq(rs.getString("bseq"));
				
				return dto;				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	public List<CommentDTO> moreComment(Map<String, String> map) {
		
		//queryParamListReturn
		try {
					
			String sql = "select * from (select a.*, rownum as rnum from (select tblComment.*, (select name from tblUser where id = tblComment.id) as name from tblComment where bseq = ? order by seq desc) a ) where rnum between ? and ? + 4";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("begin"));
			pstat.setString(3, map.get("begin"));
			
			rs = pstat.executeQuery();
			
			ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();
			
			while (rs.next()) {
				
				CommentDTO dto = new CommentDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setBseq(rs.getString("bseq"));
				
				list.add(dto);				
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	public int editComment(CommentDTO dto) {
		
		//queryParamNoReturn
		try {

			String sql = "update tblComment set content = ? where seq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getContent());
			pstat.setString(2, dto.getSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int delComment(String seq) {
		
		//queryParamNoReturn
		try {

			String sql = "delete from tblComment where seq = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public String getBseq() {
		
		//queryNoParamTokenReturn
		try {

			String sql = "select max(seq) as seq from tblBoard";

			stat = conn.createStatement();

			rs = stat.executeQuery(sql);
			
			if (rs.next()) {
				return rs.getString("seq");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.getBseq");
			e.printStackTrace();
		}

		
		return null;
	}

	public String addHashtag(String tagName) {
		
		try {
			
			//1. 태그가 존재하는지?
			//2. 태그 추가
			//3. 태그 번호 반환
			
			String sql = "select seq from tblHashtag where hashtag = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, tagName);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				//존재O
			} else {
				//존재X
				sql = "insert into tblHashtag (seq, hashtag) values (seqHashtag.nextVal, ?)";
				pstat = conn.prepareStatement(sql);
				pstat.setString(1, tagName);
				pstat.executeUpdate();
			}
			
			//***
			//tagName > seq 반환
			sql = "select seq from tblHashtag where hashtag = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, tagName);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return rs.getString("seq");
			}			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.addHashtag()");
			e.printStackTrace();
		}
		
		return null;
	}

	public void addTagging(Map<String, String> map) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblTagging (seq, bseq, hseq) values (seqTagging.nextVal, ?, ?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("hseq"));

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int getScrapBook(Map<String, String> map) {
		
		//queryParamTokenReturn
		try {

			String sql = "select count(*) as cnt from tblScrapBook where bseq = ? and id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("id"));

			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return 0;
	}

	public void addScrapBook(Map<String, String> map) {
		
		//queryParamNoReturn
		try {

			String sql = "insert into tblScrapBook (seq, bseq, id, regdate) values (seqScrapBook.nextVal, ?, ?, default)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("id"));

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void delScrapBook(Map<String, String> map) {
		
		try {

			String sql = "delete from tblScrapBook where bseq = ? and id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("id"));

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public List<BoardDTO> listScrapBook(String id) {
		
		//queryParamListReturn
		try {
					
			String sql = "select * from vwBoard where seq in (select bseq from tblScrapBook where id = ?)";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			
			rs = pstat.executeQuery();
			
			ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {

				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setId(rs.getString("id"));
				dto.setReadcount(rs.getString("readcount"));
				dto.setRegdate(rs.getString("regdate"));
				
				dto.setName(rs.getString("name"));
				dto.setIsnew(rs.getDouble("isnew"));
				dto.setCommentCount(rs.getString("commentCount"));
				
				dto.setSecret(rs.getString("secret"));
				dto.setNotice(rs.getString("notice"));
				
				list.add(dto);
						
			}	
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}











