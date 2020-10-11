package com.hsbc.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hsbc.Security.SessionManager;
import com.hsbc.exceptions.CompanyNotFoundException;
import com.hsbc.exceptions.CustomerNotFoundException;
import com.hsbc.exceptions.OrderNotFoundForEmployee;
import com.hsbc.exceptions.ProductNotFoundException;
import com.hsbc.exceptions.SessionExpiredException;
import com.hsbc.models.OrderDetails;
import com.hsbc.models.SessionEntity;
import com.hsbc.service.NewQuoteService;

@WebServlet("/orderList")
public class OrderList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NewQuoteService newQuoteService; 
    
    public OrderList() {
        super();
        newQuoteService = new NewQuoteService();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String customerId = request.getParameter("customerId");
		try {
			SessionEntity se = SessionManager.getSessionData(request);
			List<OrderDetails> list = newQuoteService.getCustomerOrderDetailsList(se.getPersonId());
			request.setAttribute("orderList", list);
			request.setAttribute("customerData",newQuoteService.getCustomerData(String.valueOf(se.getPersonId())));
		} catch (SessionExpiredException e) {
			request.setAttribute("errorMessage", "Session Expired Due to Inactivity");
			request.getRequestDispatcher("home.html").forward(request, response);
		} catch (OrderNotFoundForEmployee | ProductNotFoundException |CompanyNotFoundException e) {
			//error page
		}
		request.getRequestDispatcher("/orderCustomer.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
