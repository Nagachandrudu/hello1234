package com.codequest.servlet;
import com.codequest.util.DBConnection;
import com.codequest.util.LevelUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false);
  if(s==null||s.getAttribute("studentId")==null){resp.sendRedirect(req.getContextPath()+"/login.jsp");return;}
  long id=(Long)s.getAttribute("studentId");
  String sql="SELECT u.username,u.xp,u.level,COUNT(CASE WHEN ms.is_correct=TRUE THEN 1 END) completed FROM users u LEFT JOIN mission_submissions ms ON ms.user_id=u.id WHERE u.id=? GROUP BY u.id,u.username,u.xp,u.level";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
   p.setLong(1,id);try(ResultSet r=p.executeQuery()){if(!r.next()){s.invalidate();resp.sendRedirect(req.getContextPath()+"/login.jsp");return;}
    req.setAttribute("username",r.getString("username"));req.setAttribute("xp",r.getInt("xp"));req.setAttribute("level",r.getInt("level"));req.setAttribute("completed",r.getInt("completed"));req.setAttribute("progress",LevelUtil.progressInLevel(r.getInt("xp")));
   }req.getRequestDispatcher("/dashboard.jsp").forward(req,resp);
  }catch(SQLException e){throw new ServletException("Could not load dashboard.",e);}
 }
}
