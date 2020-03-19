package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import member.account.action.OrderListAction;
import member.order.action.ExchangeFormAction;
import member.order.action.ExchangeProAction;
import member.order.action.OrderCancelFormAction;
import member.order.action.OrderCancelProAction;
import member.order.action.OrderDeliveryAction;
import member.order.action.OrderDetailAction;
import member.order.action.OrderQListAction;
import member.order.action.OrderRefundFormAction;
import member.order.action.OrderRefundFormActionPro;
import vo.ActionForward;

@WebServlet("*.mo")
public class MemberOrderController extends HttpServlet {
	
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		
		//��ǰ��ȸ �����ȸ 
		
		request.setCharacterEncoding("utf-8");
		
		String command = request.getServletPath();
		System.out.println(command);
		
		
		Action action = null;
	ActionForward forward = null;
	
	if (command.equals("/OrderList.mo")) {//상품구매리스트
		action = new OrderListAction();
		
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else if(command.equals("/OrderDetail.mo")) {//
		action = new OrderDetailAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderDelivery.mo")) {// 배송조회
		action = new OrderDeliveryAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderCancel.mo")) {// 주문취소
		action = new OrderCancelFormAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderCancelPro.mo")) {// 주문취소 누름
		action = new OrderCancelProAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderExchange.mo")) {// 주문교환신청
		action = new ExchangeFormAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderExchangePro.mo")) {// 주문교환신청 누름
		action = new ExchangeProAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderQList.mo")) {
		System.out.println("ddd");
		action = new OrderQListAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderRefund.mo")) {
		action = new OrderRefundFormAction();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(command.equals("/OrderRefundPro.mo")) {
		action = new OrderRefundFormActionPro();
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		
		
		
		
		
		if (forward!=null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			}else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
			
		}else {
			System.out.println("ActionFoward 객체 널 값");
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
