<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><title>Register | CodeQuest</title><link rel="stylesheet" href="css/style.css"></head><body>
<nav class="nav"><a class="brand" href="index.html">⌘ CodeQuest</a><div class="navlinks"><a href="index.html">Home</a><a href="login.jsp">Student Login</a></div></nav>
<main class="page"><section class="form-card"><h1>Create your account</h1><p class="muted" style="text-align:center">Start at Level 1 and earn your first XP.</p>
<% if(request.getAttribute("error")!=null){ %><div class="error"><%= request.getAttribute("error") %></div><% } %>
<form action="register" method="post">
<div class="field"><label for="username">Username</label><input id="username" name="username" required minlength="3" maxlength="40" pattern="[A-Za-z0-9_]+" autocomplete="username"></div>
<div class="field"><label for="email">Email</label><input id="email" type="email" name="email" required maxlength="120" autocomplete="email"></div>
<div class="field"><label for="password">Password (at least 8 characters)</label><input id="password" type="password" name="password" required minlength="8" autocomplete="new-password"></div>
<div class="field"><label for="confirmPassword">Confirm password</label><input id="confirmPassword" type="password" name="confirmPassword" required minlength="8" autocomplete="new-password"></div><button type="submit">Create account</button></form><p class="muted" style="text-align:center">Already registered? <a href="login.jsp">Log in</a></p></section></main></body></html>
