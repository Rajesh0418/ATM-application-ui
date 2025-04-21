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

@WebServlet("/transactions/deposit")
public class Deposit extends HttpServlet 
{
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank bank;

	public Deposit()  {
		daoClass = new DAOClass();
		loginDao = new UserDataHandler();
		customer = new Customer();
		bank=new Bank();
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
		double depositeAmount=Double.valueOf(amount);
		
		
		Customer cust=loginDao.getCustomerData();
		
		bank.updateDepositAmount(cust, depositeAmount);
		currentBalance+=depositeAmount;
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
				session.setAttribute("successMessage", depositeAmount+" Rupees Debitted Successful! ");
			    RequestDispatcher rd = request.getRequestDispatcher("../home.jsp");
			    rd.forward(request, response);
	
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
    }
}
