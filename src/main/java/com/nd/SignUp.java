package com.nd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signup")

public class SignUp extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection con = null;
        PreparedStatement chk = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_db", "root", "namrata");

            if (con != null) 
            {
                System.out.println("Connected to the database!");
            }
            else
            {
                System.out.println("Not connected to the database...");
            }

            // Check if email is already registered
            chk = con.prepareStatement("select * from register where email = ?");
            chk.setString(1, email);
            rs = chk.executeQuery();

            if (rs.next()) // If a record is found, email is already registered
            {
                response.setContentType("text/html");
                out.print("<h3 style = 'color : red'> This email is already in use... </h3>");
                RequestDispatcher rd = request.getRequestDispatcher("./index.html");
                rd.include(request, response);
            }
            else
            {
                // Insert new user
                ps = con.prepareStatement("insert into register values(?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);

                int count = ps.executeUpdate();
                if(count > 0)
                {
                    response.setContentType("text/html");
                    out.print("<h3 style = 'color : green'>User Registered Successfully!</h3>");
                }
                else
                {
                    response.setContentType("text/html");
                    out.print("<h3 style = 'color : red'>User Was Not Registered due to some Error...</h3>");
                }
                RequestDispatcher rd = request.getRequestDispatcher("./index.html");
                rd.include(request, response);
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
            response.setContentType("text/html");
            out.print("<h3 style = 'color : red'> Exception Occurred: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("./index.html");
            rd.include(request, response);
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (chk != null) chk.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}