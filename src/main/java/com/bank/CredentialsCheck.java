package com.bank;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.customer.Customer;

@WebServlet("/bank/logincredentialscheck")
public class CredentialsCheck extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String cardNumber = request.getParameter("phoneNumber");
        String pin = request.getParameter("pin");

        UserDataHandler login = new UserDataHandler();

        if (login.check(cardNumber, pin)) {
            Customer customer = login.getCustomerData();
            if (customer != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userdata", customer);
                RequestDispatcher rd = request.getRequestDispatcher("../home.jsp");
                rd.forward(request, response);
            }
        }
        request.setAttribute("errorMessage", "Invalid Credentials");
        RequestDispatcher rd = request.getRequestDispatcher("../index.jsp");
        rd.forward(request, response);
    }
}
