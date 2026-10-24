package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {
    @Override public void init() throws ServletException {
        // ServletConfig is per-servlet; ServletContext is shared application-wide.
        String appName = getServletConfig().getInitParameter("applicationName");
        getServletContext().setAttribute("applicationName", appName == null ? "CodeQuest" : appName);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sql = "SELECT id,username FROM admins WHERE username=? AND password_hash=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username == null ? "" : username.trim());
            ps.setString(2, PasswordUtil.sha256(password == null ? "" : password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpSession old = request.getSession(false); if (old != null) old.invalidate();
                    HttpSession session = request.getSession(true);
                    session.setAttribute("adminId", rs.getLong("id"));
                    session.setAttribute("adminUsername", rs.getString("username"));
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
                } else {
                    request.setAttribute("error", "Invalid administrator credentials.");
                    request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) { throw new ServletException("Admin login database error.", e); }
    }
}
