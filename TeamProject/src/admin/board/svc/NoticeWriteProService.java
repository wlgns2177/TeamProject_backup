package admin.board.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.commit;
import static db.JdbcUtil.getConnection;
import static db.JdbcUtil.rollback;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardBean;

public class NoticeWriteProService {

	public int writeArtice(BoardBean bb) {
//		System.out.println("Notice_WriteProService 의 writeArticle()");
		
int boardNum = 0;
		
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
			boardNum = boardDAO.getMaxNum(bb.getK1());	// 다시 k1 카테고리의 글 중 최신글의 글 번호(가장 큼)를 가져옮
		} else {
			rollback(con);
		}
		
		// DB 연결 종료
		close(con);
		
		
		return boardNum; // 가져온 최신글의 글 번호 반환
	}

}
