package com.nd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addentry")
public class AddEntry extends HttpServlet
{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{

		String title = request.getParameter("title");
		String entry = request.getParameter("entry");
		//String email =  request.getSession().toString();
		
		PrintWriter out = response.getWriter();
		
		HttpSession s = request.getSession();
		String emailfromsession = s.getAttribute("email").toString();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_db", "root", "namrata");
						
			PreparedStatement ps = con.prepareStatement("insert into entries values(?, ?, ?)");
			ps.setString(1, emailfromsession);
			ps.setString(2, title);
			ps.setString(3, entry);
			
			int c = ps.executeUpdate();
			
			if(c > 0)
			{
				
				response.setContentType("text/html");
				out.print("<h3 style = 'color : green'> Entry done successfully!</h3>");
				
				RequestDispatcher rd = request.getRequestDispatcher("./home.html");
				rd.include(request, response);
			}
			else
			{
				response.setContentType("text/html");
				out.print("<h3 style = 'color : red'> Entry couldn't be done due to some error...</h3>");
				
				RequestDispatcher rd = request.getRequestDispatcher("./home.html");
				rd.include(request, response);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			response.setContentType("text/html");
			out.print("<h3 style = 'color : red'> Error: " + e.getMessage() + "</h3>");
			
			RequestDispatcher rd = request.getRequestDispatcher("./home.html");
			rd.include(request, response);
		}
		
	}
}

