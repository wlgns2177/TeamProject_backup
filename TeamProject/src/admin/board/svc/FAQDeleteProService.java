package admin.board.svc;

import static db.JdbcUtil.*;
import java.sql.Connection;

import dao.BoardDAO;

public class FAQDeleteProService {

	public boolean deleteArticle(int boardNum) {
		boolean isDeleteSuccess = false;
		Connection con = null;
		String k1="FAQ";
		
		BoardDAO boardDAO = BoardDAO.getInstance();
		con = getConnection();
		
		boardDAO.setConnection(con);
		
		int deleteCount = boardDAO.deleteArticle(boardNum, k1);
		
		close(con);
		
		return isDeleteSuccess;
	}

}
