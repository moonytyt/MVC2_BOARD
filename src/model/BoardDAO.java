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

	// ������ ���̽��� ���� �޼ҵ�
	public void getCon() {
		try {
			Context initctx = new InitialContext();

			Context envctx = (Context) initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			con = ds.getConnection();// Ŀ�ؼǿ��� ���ִ� �޼ҵ�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ü �Խñ��� ������ �����ϴ� �޼ҵ�
	public int getAllCount() {
		int count = 0;
		getCon();
		try {
			// �����غ�
			String sql = "SELECT COUNT(*) FROM BOARD";
			pstmt = con.prepareStatement(sql);
			// ���� ������ ����� ����
			rs = pstmt.executeQuery();
			if (rs.next()) {// �����Ͱ� �ִٸ�
				count = rs.getInt(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// ȭ�鿡 ������ �����͸� 10���� �����ؼ� �����ϴ� �޼ҵ�
	public ArrayList<BoardBean> getAllBoard(int startRow, int endRow) {
		ArrayList<BoardBean> list = new ArrayList<>();
		getCon();
		try {
			// ���� �ۼ�
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM BOARD ORDER BY REF DESC, RE_STEP ASC)A) WHERE RNUM >=? AND RNUM <=?";
			// ���� ������ ��ü ����
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				// �����͸� ��Ű¡ ����
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

				// ��Ű¡�� �����͸� ����Ʈ�� ����
				list.add(bean);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ� ȣ��
	public void insertBoard(BoardBean bean) {
		getCon();
		int ref=0;
		int re_step=1; //�����̱� ������
		int re_level=1; // �����̱� ������
		try {
			// �����ۼ�
			String refspl = "SELECT MAX(REF) FROM BOARD";
			pstmt = con.prepareStatement(refspl);
			// ���� ������ �����  ����
			rs = pstmt.executeQuery();
			if(rs.next()){
				ref=rs.getInt(1)+1;// ����ū���� 1�� ������
			}
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			//?
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

	//�ϳ��� �Խñ��� �о�帮�� �޼ҵ� �ۼ�
	public BoardBean getOneBoard(int num) {
		getCon();
		BoardBean bean = null;
		
		try {
			//�ϳ��� �Խñ��� �о��ٴ� ��ȸ�� ����
			String countsql = "UPDATE BOARD SET READCOUNT = READCOUNT + 1 WHERE NUM =?";
			pstmt = con.prepareStatement(countsql);
			pstmt.setInt(1, num);
			//��������
			pstmt.executeUpdate();
			
			// �ѰԽñۿ� ���� ������ �������ִ� ���� �ۼ�
			String sql = "SELECT * FROM BOARD WHERE NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//�������� �� ����� ����
			rs = pstmt.executeQuery();
			if(rs.next()){//�ϳ��� �Խñ��� �����Ѵٸ�
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

}