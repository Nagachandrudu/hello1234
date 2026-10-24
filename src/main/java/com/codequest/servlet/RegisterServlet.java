package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = trim(request.getParameter("username"));
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        if (username == null || !username.matches("[A-Za-z0-9_]{3,40}") ||
            email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
            password == null || password.length() < 8 || confirm == null || !password.equals(confirm)) {
            request.setAttribute("error", "Check all fields: username must be 3–40 letters/numbers/underscores, email must be valid, password at least 8 characters, and passwords must match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        String sql = "INSERT INTO users(username,email,password_hash,xp,level) VALUES(?,?,?,0,1)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.sha256(password));
            ps.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=1");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "That username or email is already registered.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not register user. Verify MySQL settings and schema.", e);
        }
    }
    private String trim(String value) { return value == null ? null : value.trim(); }
}
