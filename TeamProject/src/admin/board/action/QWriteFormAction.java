package admin.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import vo.ActionForward;

public class QWriteFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
//		System.out.println("Q_WriteFormAction");
		// 사용자가 1:1 작성한 것을 불러옴
		
		forward = new ActionForward();
		forward.setPath("/board/QWriteForm.jsp");
		return forward;
	}

}
