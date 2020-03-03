package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/loginstatus")
public class LoginStatusServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    response.setContentType("application/json");

    Boolean isLoggedIn = userService.isUserLoggedIn();
    String url = "";
    if (isLoggedIn){
      url = userService.createLogoutURL("/");
    } else {
      url = userService.createLoginURL("/");
    }

    String isLoggedInJson = "{ \"loginstatus\" : "
      +isLoggedIn+", \"url\" : "+url+" }";

    response.getWriter().println(isLoggedInJson);
 
  }
}