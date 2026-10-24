package com.codequest.servlet;
import com.codequest.util.DBConnection;
import com.codequest.util.LevelUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {
 @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false);
  if(s==null||s.getAttribute("studentId")==null){resp.sendRedirect(req.getContextPath()+"/login.jsp");return;}
  long userId=(Long)s.getAttribute("studentId");long missionId;
  String selected=req.getParameter("answer");
  try{missionId=Long.parseLong(req.getParameter("missionId"));}catch(Exception e){resp.sendRedirect(req.getContextPath()+"/missions");return;}
  if(!("A".equals(selected)||"B".equals(selected)||"C".equals(selected)||"D".equals(selected))){req.setAttribute("resultMessage","Please select a valid answer.");req.setAttribute("correct",false);req.getRequestDispatcher("/mission-result.jsp").forward(req,resp);return;}
  Connection c=null;
  try{
   c=DBConnection.getConnection();c.setAutoCommit(false);
   try(PreparedStatement check=c.prepareStatement("SELECT id FROM mission_submissions WHERE user_id=? AND mission_id=?")){
    check.setLong(1,userId);check.setLong(2,missionId);try(ResultSet r=check.executeQuery()){if(r.next()){c.rollback();req.setAttribute("resultMessage","You have already attempted this mission. One attempt is allowed.");req.setAttribute("correct",false);req.getRequestDispatcher("/mission-result.jsp").forward(req,resp);return;}}
   }
   String correctAnswer;int reward;
   try(PreparedStatement q=c.prepareStatement("SELECT correct_answer,xp_reward FROM missions WHERE id=? FOR UPDATE")){
    q.setLong(1,missionId);try(ResultSet r=q.executeQuery()){if(!r.next()){c.rollback();req.setAttribute("resultMessage","Mission not found.");req.setAttribute("correct",false);req.getRequestDispatcher("/mission-result.jsp").forward(req,resp);return;}correctAnswer=r.getString(1);reward=r.getInt(2);}
   }
   boolean isCorrect=selected.equals(correctAnswer);int awarded=isCorrect?reward:0;
   try(PreparedStatement ins=c.prepareStatement("INSERT INTO mission_submissions(user_id,mission_id,selected_answer,is_correct,xp_awarded) VALUES(?,?,?,?,?)")){
    ins.setLong(1,userId);ins.setLong(2,missionId);ins.setString(3,selected);ins.setBoolean(4,isCorrect);ins.setInt(5,awarded);ins.executeUpdate();
   }
   if(isCorrect){
    try(PreparedStatement up=c.prepareStatement("UPDATE users SET xp=xp+?, level=FLOOR((xp+?)/100)+1 WHERE id=?")){
     up.setInt(1,awarded);up.setInt(2,awarded);up.setLong(3,userId);up.executeUpdate();
    }
   }
   c.commit();req.setAttribute("correct",isCorrect);req.setAttribute("xpAwarded",awarded);
   req.setAttribute("resultMessage",isCorrect?"Correct answer! You earned "+awarded+" XP.":"Incorrect answer. No XP awarded.");
   req.getRequestDispatcher("/mission-result.jsp").forward(req,resp);
  }catch(SQLException e){
   if(c!=null)try{c.rollback();}catch(SQLException ignored){}
   if(e instanceof SQLIntegrityConstraintViolationException){req.setAttribute("resultMessage","This mission was already attempted. No additional XP was awarded.");req.setAttribute("correct",false);req.getRequestDispatcher("/mission-result.jsp").forward(req,resp);}
   else throw new ServletException("Could not evaluate answer.",e);
  }finally{if(c!=null)try{c.setAutoCommit(true);c.close();}catch(SQLException ignored){}}
 }
}
