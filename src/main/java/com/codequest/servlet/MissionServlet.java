package com.codequest.servlet;
import com.codequest.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
@WebServlet("/missions")
public class MissionServlet extends HttpServlet {
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false);
  if(s==null||s.getAttribute("studentId")==null){resp.sendRedirect(req.getContextPath()+"/login.jsp");return;}
  List<Map<String,Object>> rows=new ArrayList<>();
  String sql="SELECT m.id,m.title,m.description,m.category,m.difficulty,m.option_a,m.option_b,m.option_c,m.option_d,CASE WHEN ms.id IS NULL THEN 0 ELSE 1 END attempted,COALESCE(ms.is_correct,FALSE) is_correct FROM missions m LEFT JOIN mission_submissions ms ON ms.mission_id=m.id AND ms.user_id=? ORDER BY m.id";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
   p.setLong(1,(Long)s.getAttribute("studentId"));try(ResultSet r=p.executeQuery()){while(r.next()){
    Map<String,Object> m=new HashMap<>();m.put("id",r.getLong("id"));m.put("title",r.getString("title"));m.put("description",r.getString("description"));m.put("category",r.getString("category"));m.put("difficulty",r.getString("difficulty"));m.put("a",r.getString("option_a"));m.put("b",r.getString("option_b"));m.put("c",r.getString("option_c"));m.put("d",r.getString("option_d"));m.put("attempted",r.getInt("attempted"));m.put("correct",r.getBoolean("is_correct"));rows.add(m);
   }}
   req.setAttribute("missions",rows);req.getRequestDispatcher("/missions.jsp").forward(req,resp);
  }catch(SQLException e){throw new ServletException("Could not load missions.",e);}
 }
}
