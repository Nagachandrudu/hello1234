<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><title>Student Login | CodeQuest</title><link rel="stylesheet" href="css/style.css"></head><body>
<nav class="nav"><a class="brand" href="index.html">⌘ CodeQuest</a><div class="navlinks"><a href="index.html">Home</a><a href="register.jsp">Register</a><a href="admin-login.jsp">Admin Login</a></div></nav>
<main class="page"><section class="form-card"><h1>Welcome back</h1>
<% if("1".equals(request.getParameter("registered"))){ %><div class="success">Registration successful. You can log in now.</div><% } %>
<% if(request.getAttribute("error")!=null){ %><div class="error"><%= request.getAttribute("error") %></div><% } %>
<form action="login" method="post"><div class="field"><label for="identity">Username or email</label><input id="identity" name="identity" required autocomplete="username"></div><div class="field"><label for="password">Password</label><input id="password" name="password" type="password" required autocomplete="current-password"></div><button type="submit">Log in</button></form><p class="muted" style="text-align:center">New here? <a href="register.jsp">Create an account</a></p></section></main></body></html>
