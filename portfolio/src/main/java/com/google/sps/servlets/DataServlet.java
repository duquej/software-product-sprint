// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("sysTime", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()){
      String message = (String) entity.getProperty("comment");
      String commenter = (String) entity.getProperty("commenter");
      String timeSubmitted = (String) entity.getProperty("timeSubmitted");
      String email = (String) entity.getProperty("email");
      long sysTime = (long) entity.getProperty("sysTime");

      Comment comment = new Comment(message,commenter,timeSubmitted,sysTime,email);
      comments.add(comment);
    }
    
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()){
      response.sendRedirect("/");
      return;
    }

    String email = userService.getCurrentUser().getEmail();
    String nickname = getUserNickname(email);

    if (nickname == null) {
      response.sendRedirect("/nickname.html");
      return;
    }

    String message = request.getParameter("comment");
    String commenter = nickname;
    String currentTime = new Date().toString();
    long systemTime = System.currentTimeMillis(); 

    storeToDatabase(message, commenter,currentTime, systemTime, email);

    response.sendRedirect("/index.html");
  }

  /**
   * Stores a comment to the database using Datastore.
   */
  private static void storeToDatabase(String message, String commenter, String time, long sysTime, String email ){
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment",message);
    commentEntity.setProperty("commenter", commenter);
    commentEntity.setProperty("timeSubmitted",time);
    commentEntity.setProperty("sysTime", sysTime);
    commentEntity.setProperty("email", email);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }

  /**
   * Converts an ArrayList of Comment objects to Json.
   */
  private static String convertToJson(ArrayList<Comment> lst){
    Gson gson = new Gson();
    String json = gson.toJson(lst);
    return json;  
  }

  /**
   * Retrieves the nickname associated with the gmail account.
   */
  private String getUserNickname(String email) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("UserInfo");
    query.setFilter(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email));

    PreparedQuery results = datastore.prepare(query);

    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }

    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }

}
