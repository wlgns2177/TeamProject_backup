package admin.board.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.commit;
import static db.JdbcUtil.getConnection;
import static db.JdbcUtil.rollback;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.startup.SetContextPropertiesRule;

import dao.AdminBoardDAO;
import vo.BoardBean2;
import vo.PageInfo;

public class BoardService {

	// 해당 대분류 내의 가장 큰 글 번호를 가져오는 메서드
	public int getMaxNum(String k1) {
		System.out.println("BoardService의 getMaxNum() 메서드");
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
		// DB 연결
		con = getConnection();
		boardDAO.setConnection(con);
		// DB 작업
		int maxNum = boardDAO.getMaxNum(k1);	// k1 카테고리의 글 중 최신글의 글 번호(가장 큼)를 가져옮
		// DB 연결 종료
		close(con);
		return maxNum; // 가져온 최신글의 글 번호 반환
	}

	// 글을 작성하는 메서드
	public int writeArticle(BoardBean2 bb) {
		System.out.println("BoardService의 writeArticle() 메서드");
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
		// DB 연결
		con = getConnection();
		boardDAO.setConnection(con);
		// DB 작업
		// 글이 작성되었는가 여부는 int로 리턴받게 된다. 0이면 실패
		int insertCount = boardDAO.insertArticle(bb);
		
		if(insertCount != 0) {
			commit(con);	// 삽입된 글 정보 적용(커밋)
		} else {
			rollback(con);
		}
		// DB 연결 종료
		close(con);
		
		return insertCount; // 가져온 최신글의 글 번호 반환
	}

	// 글 정보를 가져오는 메서드
	public BoardBean2 getArticle(int boardNum, String k1, String k2) {
		System.out.println("BoardService의 getArticle() 메서드");
		// 객체 반환을 위한 BoardBean 객체 선언
		BoardBean2 bb = null;
		//
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
		// DB 연결
		con = getConnection();
		boardDAO.setConnection(con);
		// DB 작업
		bb = boardDAO.selectArticle(boardNum, k1, k2);
		// DB 연결 종료
		close(con);
		
		return bb;
	}

	public int deleteArticle(int boardNum, String k1, String k2) {
		System.out.println("BoardService의 deleteArticle() 메서드");
		
		BoardBean2 bb = null;
		Connection con = null;
		
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
		con = getConnection();
		
		boardDAO.setConnection(con);
		
		int deleteCount = 0;
		deleteCount = boardDAO.deleteArticle(boardNum, k1, k2);
		
		return 0;
	}

	public int modifyArticle(BoardBean2 bb, List<String> deleteFileName) {
		System.out.println("BoardService의 modifyArticle() 메서드");

		Connection con = null;
		
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
		con = getConnection();
		
		int updateCount = 0;
		
		boardDAO.setConnection(con);
		
		updateCount = boardDAO.updateArticle(bb, deleteFileName);
		
		if(updateCount != 0) {
			System.out.println("수정성공");
			commit(con);
		} else {
			System.out.println("수정 실패");
			rollback(con);
		}
		
		return updateCount;
	}

	
	public int getListCount(String k1, String k2) {
		int listCount = 0;
		
		// 1. Connection 객체 가져오기
		Connection con = getConnection(); // static import 가 아니라면  jdbcUtill.으로 호출(스태틱 메서드 호출 시)
				
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
				
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
		
		// 4. DAO 객체의 getListCount() 메서드 실행
		listCount = boardDAO.selectListCount(k1, k2);
		
		// 5. Connection 객체 반환하기
		close(con);
		
		return listCount;
	}

	public ArrayList<BoardBean2> getArticleList(PageInfo pageInfo) {
		ArrayList<BoardBean2> articleList = null;
		// 1. Connection 객체 가져오기
		Connection con = getConnection(); // static import 가 아니라면  jdbcUtill.으로 호출(스태틱 메서드 호출 시)
						
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
						
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
				
		// 4. DAO 객체의 selectArticleList() 메서드 실행
		articleList = boardDAO.selectArticleList(pageInfo);
		
		// 5. Connection 객체 반환하기
		close(con);
		return articleList;
	}

	
	
	// 미답변 글 갯수 가져오기
	public int getNAListCount(String k1, String k2) {
		int listCount = 0;
		
		// 1. Connection 객체 가져오기
		Connection con = getConnection(); // static import 가 아니라면  jdbcUtill.으로 호출(스태틱 메서드 호출 시)
				
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
				
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
		
		// 4. DAO 객체의 getListCount() 메서드 실행
		listCount = boardDAO.selectNAListCount(k1, k2);
		
		// 5. Connection 객체 반환하기
		close(con);
		
		return listCount;
	}

	public ArrayList<BoardBean2> getArticleNAList(PageInfo pageInfo) {
		ArrayList<BoardBean2> articleList = null;
		// 1. Connection 객체 가져오기
		Connection con = getConnection(); // static import 가 아니라면  jdbcUtill.으로 호출(스태틱 메서드 호출 시)
						
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		AdminBoardDAO boardDAO = AdminBoardDAO.getInstance();
						
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
				
		// 4. DAO 객체의 selectArticleList() 메서드 실행
		articleList = boardDAO.selectArticleNAList(pageInfo);
		
		// 5. Connection 객체 반환하기
		close(con);
		return articleList;
	}

	
	
}
