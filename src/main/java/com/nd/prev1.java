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

@WebServlet("/prev")
public class prev1 extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		PrintWriter out = response.getWriter();
		
		//String email = request.getParameter("email");
		HttpSession s = request.getSession();
		String emailfromsession = s.getAttribute("email").toString();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_db", "root", "namrata");
			
			PreparedStatement ps = con.prepareStatement("select title, entry from entries where email = ?");
			ps.setString(1, emailfromsession);
			
			
			ResultSet rs = ps.executeQuery();
			String title = "";
			String entry = "";
			
			if(rs.next())
			{
				out.println("Previous Entries:");
                // Iterate through ResultSet to display entries
                do {
                    title = rs.getString(1);
                    entry = rs.getString(2);
                    out.print(title + ":");
                    out.println(entry);
                } while (rs.next());
			}
			else
			{
				response.setContentType("text/html");
				out.print("<h3 style = 'color : red'> Cannot display entries... </h3>");
				
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