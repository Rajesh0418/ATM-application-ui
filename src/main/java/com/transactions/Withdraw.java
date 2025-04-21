package com.transactions;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.*;
import com.customer.*;
import com.dao.DAOClass;

@WebServlet("/transactions/withdrawl")
public class Withdraw extends HttpServlet 
{
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank admin;

	public Withdraw()  {
		daoClass = new DAOClass();
		loginDao = new UserDataHandler();
		customer = new Customer();
		admin=new Bank();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();	
		Customer user = (Customer) session.getAttribute("userdata");
		double currentBalance=user.getBalanceAmount();
		int id = user.getId();

        String accountholdername=request.getParameter("accountholdername");
		String accountNumber = request.getParameter("accountnumber");
		String ifsccode=request.getParameter("ifsccode");
		String amount = request.getParameter("amount");
		double withdrawlAmount=Double.valueOf(amount);
		
		Customer cust=loginDao.getCustomerData();
        System.out.println("hi hello namaste aadaam"+currentBalance);

		
		if(currentBalance>=withdrawlAmount) 
		{
			admin.updateWithdrawlAmount(user, withdrawlAmount);
			currentBalance=currentBalance-withdrawlAmount;

			// query
			String quary = "UPDATE userdata SET balance=?  WHERE accountholdername=? and accountnumber=? and ifsccode=?";
			try {
	
				PreparedStatement st = (daoClass.conn).prepareStatement(quary);
				st.setDouble(1, currentBalance);
				st.setString(2, accountholdername.toUpperCase());
				st.setString(3, accountNumber);
				st.setString(4,ifsccode);
				
	
				st.executeUpdate();
                loginDao.fetchById(id);
                
				session.setAttribute("userdata", cust);
				session.setAttribute("successMessage", withdrawlAmount+" Rupees Withdrawal Successful! ");
			    RequestDispatcher rd = request.getRequestDispatcher("../home.jsp");
			    rd.forward(request, response);
	
	
			} catch (SQLException e) 
			{
				e.printStackTrace();
		    }
	   }
		else {
			request.setAttribute("errorMessage","Your current balance is "+ currentBalance);

        RequestDispatcher rd = request.getRequestDispatcher("withdraw.jsp");
        rd.forward(request, response);
		}
	}
}
