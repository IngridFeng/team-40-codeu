package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import java.io.IOException;

import java.util.Set;
import java.util.List;
import com.google.codeu.data.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles fetching all users for the community page.
 */
@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      response.setContentType("application/json");
      String topic = request.getParameter("topic");
      String timezone = request.getParameter("timezone");
      String studypace = request.getParameter("studypace");
      List<User> users = datastore.getUsers(topic, timezone, studypace);
      Gson gson = new Gson();
      String json = gson.toJson(users);
      response.getOutputStream().println(json);
    }
}
