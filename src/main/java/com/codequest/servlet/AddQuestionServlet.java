package com.codequest.servlet;

import com.codequest.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin/add-question")
public class AddQuestionServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("adminId") == null) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp"); return;
        }
        String title = val(request,"title"), description = val(request,"description");
        String category = val(request,"category"), difficulty = val(request,"difficulty");
        String a=val(request,"optionA"), b=val(request,"optionB"), c=val(request,"optionC"), d=val(request,"optionD");
        String correct=val(request,"correctAnswer");
        int xp;
        try { xp = Integer.parseInt(val(request,"xpReward")); }
        catch (NumberFormatException e) { xp = -1; }
        if (title.isBlank() || description.isBlank() || category.isBlank() ||
            !("Easy".equals(difficulty)||"Medium".equals(difficulty)||"Hard".equals(difficulty)) ||
            a.isBlank()||b.isBlank()||c.isBlank()||d.isBlank()||
            !("A".equals(correct)||"B".equals(correct)||"C".equals(correct)||"D".equals(correct)) || xp < 0 || xp > 10000) {
            request.setAttribute("error","Fill every field correctly. XP must be 0–10000.");
            request.getRequestDispatcher("/admin/add-question.jsp").forward(request,response); return;
        }
        String sql="INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,title);ps.setString(2,description);ps.setString(3,category);ps.setString(4,difficulty);
            ps.setString(5,a);ps.setString(6,b);ps.setString(7,c);ps.setString(8,d);ps.setString(9,correct);ps.setInt(10,xp);
            ps.executeUpdate(); response.sendRedirect(request.getContextPath()+"/admin/questions?added=1");
        }catch(SQLException e){throw new ServletException("Could not save question.",e);}
    }
    private String val(HttpServletRequest r,String n){String v=r.getParameter(n);return v==null?"":v.trim();}
}
