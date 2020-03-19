package admin.board.svc;

import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardBean;

public class FAQModifyProService {

	public boolean modifyArticle(BoardBean bb) {
		System.out.println("FAQ_ModifyProService 의 modifyArticle()");
		boolean isModifySuccess = false;
		Connection con = null;
		
		BoardDAO boardDAO = BoardDAO.getInstance();
		con = getConnection();
		boardDAO.setConnection(con);
		
		int modifyCount = boardDAO.updateArticle(bb);
		
		if(modifyCount != 0) {
			commit(con);
			isModifySuccess = true;
		} else {
			rollback(con);
		}
		
		close(con);
		
		return isModifySuccess;
	}

}
