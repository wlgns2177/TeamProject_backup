package member.book.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import member.book.svc.ReviewListService;
import vo.ActionForward;
import vo.BookBean;

public class ReviewListAcion implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("ReviewListAcion - ActionForward");
		
//		int num = Integer.parseInt(request.getParameter("num"));

//		ReviewListService reviewListService = new ReviewListService();
//		BookBean reviews = reviewListService.getRevies(num);
		
		ActionForward action = new ActionForward();
		action.setPath("/book/review_list.jsp");

		
		return action;
	}

}
