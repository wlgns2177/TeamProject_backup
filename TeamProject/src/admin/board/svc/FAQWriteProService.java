package admin.board.svc;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardBean;
import static db.JdbcUtil.*;

public class FAQWriteProService {

	public int writeArticle(BoardBean bb) {
		System.out.println("FAQ_WriteProService 의 writeArticle()");
		int boardNum = 0;
		
		Connection con = null;
		
		// BoardDAO 객체 생성(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
		
		// DB 연결
		con = getConnection();
		boardDAO.setConnection(con);
		
		// DB 작업
		
		// 글이 작성되었는가 여부는 int로 리턴받게 된다. 0이면 실패
		boardNum = boardDAO.getMaxNum(bb.getK1()) + 1;	// 다시 k1 카테고리의 글 중 최신글의 글 번호(가장 큼)를 가져옮
		bb.setBoardNum(boardNum);
		int insertCount = boardDAO.insertArticle(bb);
		
		if(insertCount != 0) {
			commit(con);	// 삽입된 글 정보 적용(커밋)
			
		} else {
			rollback(con);
		}
		
		// DB 연결 종료
		close(con);
		
		
		return boardNum; // 가져온 최신글의 글 번호 반환
	}

}
