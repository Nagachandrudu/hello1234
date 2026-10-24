package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String identity = request.getParameter("identity");
        String password = request.getParameter("password");
        if (identity == null || password == null) {
            request.setAttribute("error", "Enter your username/email and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response); return;
        }
        String sql = "SELECT id,username FROM users WHERE (username=? OR email=?) AND password_hash=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identity.trim()); ps.setString(2, identity.trim());
            ps.setString(3, PasswordUtil.sha256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpSession old = request.getSession(false);
                    if (old != null) old.invalidate();
                    HttpSession session = request.getSession(true);
                    session.setAttribute("studentId", rs.getLong("id"));
                    session.setAttribute("studentUsername", rs.getString("username"));
                    session.setMaxInactiveInterval(30 * 60);
                    Cookie seen = new Cookie("cqSeenLogin", "yes");
                    seen.setMaxAge(30 * 24 * 60 * 60);
                    seen.setHttpOnly(true);
                    seen.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                    response.addCookie(seen);
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    request.setAttribute("error", "Invalid username/email or password.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) { throw new ServletException("Login database error.", e); }
    }
}
