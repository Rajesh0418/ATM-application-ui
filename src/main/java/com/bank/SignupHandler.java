package com.bank;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.*;

@WebServlet("/bank/signuphandler")
public class SignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
             doPost(request,response);
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Random random = new Random();

		String ifscCode1 = "RCBN000";
		int ifscCode2 = random.nextInt(3000) + 6000; // 6000–8999
		String finalIFSCCode = ifscCode1 + ifscCode2;

		int accountNumber1 = random.nextInt(900000000) + 100000000; // 100000000–999999999
		int accountNumber2 = random.nextInt(900) + 100; // 100–999
		String finalAccountNumber = accountNumber1 + "" + accountNumber2;

		String cardNumber1 = String.valueOf(random.nextInt(90000000) + 10000000);
		String cardNumber2 = String.valueOf(random.nextInt(90000000) + 10000000);
		String finalCardNumber = cardNumber1 + cardNumber2;

		String accountholdername = request.getParameter("accountholdername");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String phno = request.getParameter("phonenumber");
		String password = request.getParameter("password");
		String balance=request.getParameter("balance");
		double currentBalance=Double.parseDouble(balance);

		// Hash the password before storing
		//String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		String query = "INSERT INTO userdata(accountholdername, ifsccode, cardnumber, accountnumber, email, phonenumber, address, password,balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

		DAOClass dao = new DAOClass();
		try (Connection conn = dao.conn;
			 PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, accountholdername.toUpperCase());
			ps.setString(2, finalIFSCCode);
			ps.setString(3, finalCardNumber);
			ps.setString(4, finalAccountNumber);
			ps.setString(5, email);
			ps.setString(6, phno);
			ps.setString(7, address);
			ps.setString(8, password);
			ps.setDouble(9, currentBalance);

			int i = ps.executeUpdate();

			if (i > 0) {
				response.sendRedirect("../index.jsp");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Signup failed. Try again.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred.");
		}
	}
}
