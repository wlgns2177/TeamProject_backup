package admin.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import admin.board.svc.FAQDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class FAQDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		String num = request.getParameter("num");
		// FAQ 작성한 것을 불러옴
		FAQDetailService  faq_DetailService = new FAQDetailService();
		BoardBean bb = faq_DetailService.getArticle(num);
		
		if(bb != null) {
			forward = new ActionForward();
			forward.setPath("./admin/board/FAQDetail.jsp");
		}
		
		return forward;
	}

}
