package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/nickname")
public class NicknameServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    String loginLogoutUrl = "";
    UserService userService = UserServiceFactory.getUserService();
    boolean isLoggedIn = userService.isUserLoggedIn();
    if (isLoggedIn) {
      loginLogoutUrl = userService.createLogoutURL("/");
      String nickname = getUserNickname(userService.getCurrentUser().getEmail());
      String json = createNicknameJson(loginLogoutUrl, nickname, isLoggedIn);
      out.println(json);

    } else {
      loginLogoutUrl = userService.createLoginURL("/nickname.html");
      String json = createNicknameJson(loginLogoutUrl, null, isLoggedIn);
      out.println(json);
      
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/nickname");
      return;
    }

    String nickname = request.getParameter("nickname");
    String email = userService.getCurrentUser().getEmail();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("UserInfo", email);
    entity.setProperty("email", email);
    entity.setProperty("nickname", nickname);
    datastore.put(entity);

    response.sendRedirect("/");
  }

  /**
   * Creates a json query with {@code url}, {@code nickname}, and {@code loggedin}
   * Ex.  { "url" : /index.html, "nickname" : "john", "loggedin" : true }
   */
  private String createNicknameJson(String url, String nickname, boolean loggedin){
    String json = "{ \"url\" : \""+url+"\", \"nickname\" : \""+nickname
      +"\", \"loggedin\" : "+loggedin+" }";
    return json; 
  }

  /**
   * Returns the nickname of the user with email, or empty String if the user has not set a nickname.
   */
  private String getUserNickname(String email) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("UserInfo");
    query.setFilter(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email));

    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }

    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
