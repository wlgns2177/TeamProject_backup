package admin.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import admin.board.svc.FAQDeleteProService;
import vo.ActionForward;

public class FAQDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		int boardNum = Integer.parseInt(request.getParameter("num"));
		
		FAQDeleteProService faq_DeleteProService = new FAQDeleteProService();
		boolean isDeleteSuccess = faq_DeleteProService.deleteArticle(boardNum);
		
		forward = new ActionForward();
		forward.setPath("FAQList.bo");
		forward.setRedirect(true);
		return forward;
	}

}
