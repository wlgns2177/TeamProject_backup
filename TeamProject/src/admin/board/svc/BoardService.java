package admin.board.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.commit;
import static db.JdbcUtil.getConnection;
import static db.JdbcUtil.rollback;

import java.sql.Connection;

import org.apache.catalina.startup.SetContextPropertiesRule;

import dao.BoardDAO;
import vo.BoardBean;

public class BoardService {

	// 해당 대분류 내의 가장 큰 글 번호를 가져오는 메서드
	public int getMaxNum(String k1) {
		System.out.println("BoardService의 getMaxNum() 메서드");
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
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
	public int writeArticle(BoardBean bb) {
		System.out.println("BoardService의 writeArticle() 메서드");
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
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
	public BoardBean getArticle(int boardNum, String k1, String k2) {
		System.out.println("BoardService의 getArticle() 메서드");
		// 객체 반환을 위한 BoardBean 객체 선언
		BoardBean bb = null;
		//
		Connection con = null;
		// BoardDAO 객체 생성(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
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
		
		BoardBean bb = null;
		Connection con = null;
		
		BoardDAO boardDAO = BoardDAO.getInstance();
		con = getConnection();
		
		boardDAO.setConnection(con);
		
		int deleteCount = 0;
		deleteCount = boardDAO.deleteArticle(boardNum, k1, k2);
		
		return 0;
	}

	

	
	
}
