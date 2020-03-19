package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import admin.board.action.NoticeDetailAction;
import board.action.FAQDetailAction;
import board.action.FAQListAction;
import board.action.NoticeListAction;
import board.action.NoticeModifyFormAction;
import board.action.MemberQListAction;
import board.action.MemberQWriteFormProAction;
import board.action.QWriteProAction;
import board.action.MemberQdetailAction;
import vo.ActionForward;

@WebServlet("*.bo")
public class BoardController extends HttpServlet {
	
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String command = request.getServletPath();
		System.out.println(command);
		
		Action action = null;
		ActionForward forward = null;
		
		// 서블릿 주소에 따라 각각 다른 작업을 수행
		if (command.equals("/QWriteForm.bo")) {// 1:1 문의 작성 폼
			forward = new ActionForward();
			forward.setPath("/QWriteForm.jsp");
		} else if(command.equals("/QWriteFormPro.bo")) {// 1:1 문의 작성
			action = new MemberQWriteFormProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/QList.bo")) {//1:1 문의내역보기
			
			action = new MemberQListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/Qdetail.bo")) {//1:1 문의내용 보기, 답변 보기
			
			action = new MemberQdetailAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
            //
            // FAQ 관련
            //
        } else if(command.equals("/FAQList.bo")) { // --- FAQ
			// FAQ 목록 FAQListAction()
			action = new FAQListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } else if(command.equals("/FAQDetail.bo")) {
            // --- FAQ 상세보기
            action = new FAQDetailAction();
            try {
                forward = action.execute(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            //
            // 공지 관련
            //
        } else if(command.equals("/NoticeList.bo")) { // --- Notice
            // Notice 목록 NoticeListAction()
            action = new NoticeListAction();
            try {
                forward = action.execute(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(command.equals("/NoticeDetail.bo")) {
            // --- Notice 상세보기
            action = new NoticeDetailAction();
            try {
                forward = action.execute(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
	
		

		//=================================================================================
        // ActionForward 객체의 포워딩 방식
		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			}else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}else {
			System.out.println("ActionFoward 이 null입니다");
		}
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

}
