package admin.board.svc;

import dao.BoardDAO;
import vo.BoardBean;
import static db.JdbcUtil.*;

import java.sql.Connection;

public class FAQDetailService {

	public BoardBean getArticle(String num) {
		System.out.println("Q_DetailService 의 getArticle()");
		BoardBean bb = null;
		
		BoardDAO boardDAO = BoardDAO.getInstance();
		Connection con = getConnection();
		
		boardDAO.setConnection(con);
		
		int boardNum = Integer.parseInt(num);
		String k1 = "FAQ";
		
		bb = boardDAO.selectArticle(boardNum, k1);
		
		int kID = boardDAO.get_kID(bb.getK1(), bb.getK2(), bb.getK3());
		int updateCount = boardDAO.increaseReadCount(boardNum, kID);
		if(updateCount != 0) {
			commit(con);
		} else {
			rollback(con);
		}
		
		close(con);
		
		return bb;
	}

}
