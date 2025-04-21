package com.bank;

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

import com.customer.*;
import com.dao.DAOClass;

@WebServlet("/bank/updateuserdata")
public class UpdateUserData extends HttpServlet {
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank admin;

	public UpdateUserData()  {
		daoClass = new DAOClass();
		loginDao = new UserDataHandler();
		customer = new Customer();
		admin=new Bank();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Customer user = (Customer) session.getAttribute("userdata");
		int id = user.getId();

		String name = request.getParameter("updatedName");
		String email = request.getParameter("updatedEmail");
		String password = request.getParameter("updatedPassword");
		String phno = request.getParameter("updatedPhoneNumber");
		String address = request.getParameter("updatedAddress");
		
		admin.changeData(user,name, email, password, phno, address);
		Customer customer = loginDao.getCustomerData();

		// query
		String quary = "UPDATE userdata SET accountholdername=?, email=?, address=?, phonenumber=?,password=?  WHERE id=?";
		try {

			PreparedStatement st = (daoClass.conn).prepareStatement(quary);
			st.setString(1, name.toUpperCase());
			st.setString(2, email);
			st.setString(3, address);
			st.setString(4, phno);
			st.setString(5, password);
			st.setInt(6, id);

			st.executeUpdate();
			loginDao.fetchById(id);

			session.setAttribute("userdata", customer);
			RequestDispatcher rd = request.getRequestDispatcher("../home.jsp");
			rd.forward(request, response);


		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
