package com.nd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class Login extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
			
			PrintWriter out = response.getWriter();
		
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			HttpSession s = request.getSession();
			s.setAttribute("email", email);
			
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_db", "root", "namrata");
				
				PreparedStatement ps = con.prepareStatement("select * from register where email = ? and password = ?");
				ps.setString(1, email);
				ps.setString(2, password);
				
				ResultSet rs = ps.executeQuery();
				
				if(rs.next())
				{
					
					//HttpSession session = request.getSession();
					//session.setAttribute("user_email", email);
					
					request.setAttribute("email", email);
					
					RequestDispatcher rd = request.getRequestDispatcher("./home.html");
					rd.include(request, response);
			
//					RequestDispatcher rd = request.getRequestDispatcher("addentry");
//					rd.forward(request, response);
					
				}
				else
				{
					response.setContentType("text/html");
					out.print("<h3 style = 'color : red'> Email id and Password didn't match...</h3>");
					
					RequestDispatcher rd = request.getRequestDispatcher("./login.html");
					rd.include(request, response);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				response.setContentType("text/html");
				out.print("<h3 style = 'color : red'> Error: " + e.getMessage() + "</h3>");
				
				RequestDispatcher rd = request.getRequestDispatcher("./login.html");
				rd.include(request, response);
			}
	}
}
