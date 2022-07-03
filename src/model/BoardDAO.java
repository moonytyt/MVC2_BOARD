package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;

	// 데이터 베이스에 연결 메소드
	public void getCon() {
		try {
			Context initctx = new InitialContext();

			Context envctx = (Context) initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			con = ds.getConnection();// 커넥션연결 해주는 메소드
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 전체 게시글의 갯수를 리턴하는 메소드
	public int getAllCount() {
		int count = 0;
		getCon();
		try {
			// 쿼리준비
			String sql = "SELECT COUNT(*) FROM BOARD";
			pstmt = con.prepareStatement(sql);
			// 쿼리 실행후 결과를 리턴
			rs = pstmt.executeQuery();
			if (rs.next()) {// 데이터가 있다면
				count = rs.getInt(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// 화면에 보여질 데이터를 10개씩 추출해서 리턴하는 메소드
	public ArrayList<BoardBean> getAllBoard(int startRow, int endRow) {
		ArrayList<BoardBean> list = new ArrayList<>();
		getCon();
		try {
			// 쿼리 작성
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM BOARD ORDER BY REF DESC, RE_LEVEL ASC)A) WHERE RNUM >=? AND RNUM <=?";
			// 쿼리 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				// 데이터를 패키징 해줌
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));

				// 패키징한 데이터를 리스트에 저장
				list.add(bean);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 하나의 게시글을 저장하는 메소드 호출
	public void insertBoard(BoardBean bean) {
		getCon();
		int ref = 0;
		int re_step = 1; // 새글이기 때문에
		int re_level = 1; // 새글이기 때문에
		try {
			// 쿼리작성
			String refspl = "SELECT MAX(REF) FROM BOARD";
			pstmt = con.prepareStatement(refspl);
			// 쿼리 실행후 결과를 리턴
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ref = rs.getInt(1) + 1;// 가장큰값에 1을 더해줌
			}
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			// ?
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());

			pstmt.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 하나의 게시글을 읽어드리는 메소드 작성
	public BoardBean getOneBoard(int num) {
		getCon();
		BoardBean bean = null;

		try {
			// 하나의 게시글을 읽었다는 조회수 증가
			String countsql = "UPDATE BOARD SET READCOUNT = READCOUNT + 1 WHERE NUM =?";
			pstmt = con.prepareStatement(countsql);
			pstmt.setInt(1, num);
			// 쿼리실행
			pstmt.executeUpdate();

			// 한게시글에 대한 정보를 리턴해주는 쿼리 작성
			String sql = "SELECT * FROM BOARD WHERE NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if (rs.next()) {// 하나의 게시글이 존재한다면
				bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	// 조회수를 증가하지 않는 하나의 게시글을 리턴하는 메소드
	public BoardBean getoneUpdateBoard(int num) {
		getCon();
		BoardBean bean = null;

		try {
			// 한게시글에 대한 정보를 리턴해주는 쿼리 작성
			String sql = "SELECT * FROM BOARD WHERE NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if (rs.next()) {// 하나의 게시글이 존재한다면
				bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	// 답변글을 저장하는 메소드
	public void reInsertBoard(BoardBean bean) {
		getCon();
		int ref = bean.getRef();
		int re_step = bean.getRe_step();
		int re_level = bean.getRe_level();
		try {
			// 쿼리작성
			String levelspl = "UPDATE BOARD SET RE_LEVEL = RE_LEVEL+1 WHERE REF=? AND RE_LEVEL >?"; // 이렇게
																									// 해야
																									// LEVEL이
																									// +1
																									// 이
																									// 되고
																									// 남은
																									// LEVEL에
																									// 넣을
																									// 수
																									// 있음
			pstmt = con.prepareStatement(levelspl);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, re_level);

			pstmt.executeQuery();

			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			// ?
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step + 1);// 기존 부모글에 스텝보다 1을 증가
			pstmt.setInt(7, re_level + 1);// 기존 부모글에 스텝보다 1을 증가 아까 LEVEL+1한거중
											// LEVEL숫자에 해당하는 숫자가 삽입된다
			pstmt.setString(8, bean.getContent());

			pstmt.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 하나의 게시글을 수정하는 메소드
	public void updateBoard(int num, String subject, String content) {

		getCon();
		try {
			String sql = "UPDATE BOARD SET SUBJECT=?, CONTENT=? WHERE NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, num);
			
			pstmt.executeUpdate();
			
			//자원반납
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	// 게시글 삭제 메소드
	public void deleteBoard(int ref) {
		
		getCon();
		
		try {
			String sql = "DELETE FROM BOARD WHERE REF = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.executeUpdate();
			
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
