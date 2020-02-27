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
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<Comment> comments = new ArrayList<Comment>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()){
      String message = (String) entity.getProperty("comment");
      String commenter = (String) entity.getProperty("commenter");
      String timeSubmitted = (String) entity.getProperty("timeSubmitted");

      Comment comment = new Comment(message,commenter,timeSubmitted);
      comments.add(comment);
    }
    
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = request.getParameter("comment");
    String commenter = request.getParameter("name");
    String currentTime = new Date().toString();

    storeToDatabase(message,commenter,currentTime);

    response.sendRedirect("/index.html");
  }

  /**
   * Stores a comment to the database using Datastore.
   */
  private static void storeToDatabase(String message, String commenter, String time){
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment",message);
    commentEntity.setProperty("commenter", commenter);
    commentEntity.setProperty("timeSubmitted",time);

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
}
