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

@WebServlet("/transactions/transferamount")
public class TransferAmount extends HttpServlet 
{
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank admin;

	public TransferAmount()  {
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
		String cardnumber=request.getParameter("cardnumber");
		String ifsccode=request.getParameter("ifsccode");
		String phonenumber=request.getParameter("phonenumber");
		String amount = request.getParameter("amount");
		double transferAmount=Double.valueOf(amount);
		
		Customer cust=loginDao.getCustomerData();
		
		if(currentBalance>=transferAmount) 
		{
				TransferAmountHandler transferhandler=new TransferAmountHandler();
				
				if(transferhandler.checkAccountDetails(transferAmount, accountholdername, accountNumber, cardnumber, ifsccode, phonenumber)) {
				// query
				String quary = "UPDATE userdata SET balance=?  WHERE accountholdername=? and accountnumber=? and ifsccode=?";
				try {
		
					currentBalance=currentBalance-transferAmount;
					PreparedStatement st = (daoClass.conn).prepareStatement(quary);
					st.setDouble(1, currentBalance);
					st.setString(2, user.getAccountHolderName().toUpperCase());
					st.setString(3, user.getAccountNumber());
					st.setString(4, user.getIFSCCode());
					
		
					st.executeUpdate();
	                loginDao.fetchById(id);
	                
					session.setAttribute("userdata", cust);
					session.setAttribute("successMessage", transferAmount+" Rupees Transferred Successful! ");
				    RequestDispatcher rd = request.getRequestDispatcher("../home.jsp");
				    rd.forward(request, response);
		
		
				} catch (SQLException e) 
				{
					e.printStackTrace();
			    }
			}
		    else {
		    	request.setAttribute("errorMessage","Invalid Credentials");
	            RequestDispatcher rd = request.getRequestDispatcher("transferAmount.jsp");
	            rd.forward(request, response);
		    }
			
	   }
	   else 
	   {
			request.setAttribute("errorMessage","Your current balance is "+ currentBalance);
            RequestDispatcher rd = request.getRequestDispatcher("transferAmount.jsp");
            rd.forward(request, response);
	   }
    }
}
