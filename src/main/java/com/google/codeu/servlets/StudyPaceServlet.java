package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;

import com.google.codeu.data.Chat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/studypace")
public class StudyPaceServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "studypace" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("text/html");

    String user = request.getParameter("user");

    if(user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User userData = datastore.getUser(user);

    if(userData == null || userData.getStudyPace() == null) {
      return;
    }

    response.getOutputStream().println(userData.getStudyPace());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    //get email of current user
    String userEmail = userService.getCurrentUser().getEmail();
    //get studypace that user put in
    Long studypace = Long.parseLong(request.getParameter("studypace"));
    //get current user by querying the email
    User user = datastore.getUser(userEmail);
    if (user == null) {
      //set chats of current user to null
      List<String> chats = new ArrayList<String>();
      //create the user
      user = new User(userEmail, null, null, chats, null, null, null, null, studypace, null, null);
    }
    else{
      //set the studypace
      user.setStudyPace(studypace);
    }
    //store the user
    datastore.storeUser(user);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}
