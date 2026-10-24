package com.codequest.servlet;
import com.codequest.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
@WebServlet("/admin/questions")
public class ViewQuestionsServlet extends HttpServlet {
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false);
  if(s==null||s.getAttribute("adminId")==null){resp.sendRedirect(req.getContextPath()+"/admin-login.jsp");return;}
  List<Map<String,Object>> rows=new ArrayList<>();
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT id,title,category,difficulty,xp_reward FROM missions ORDER BY id DESC");ResultSet r=p.executeQuery()){
   while(r.next()){Map<String,Object> m=new HashMap<>();m.put("id",r.getLong("id"));m.put("title",r.getString("title"));m.put("category",r.getString("category"));m.put("difficulty",r.getString("difficulty"));m.put("xp",r.getInt("xp_reward"));rows.add(m);}
   req.setAttribute("questions",rows);req.getRequestDispatcher("/admin/view-questions.jsp").forward(req,resp);
  }catch(SQLException e){throw new ServletException("Could not load questions.",e);}
 }
}
