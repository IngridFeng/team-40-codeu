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
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.codeu.data.Chat;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/topic")
public class TopicServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "topic" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if(user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User userData = datastore.getUser(user);

    if(userData == null || userData.getCurrentTopics() == null) {
      return;
    }

    // change this to just topic data later
    Gson gson = new Gson();
    String json = gson.toJson(userData);

    response.getOutputStream().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      System.out.println("WHOA SLOW DOWN");
      System.out.println(request.getParameter("currentTopics"));
      // maybe don't require login?
      UserService userService = UserServiceFactory.getUserService();
      if (!userService.isUserLoggedIn()) {
        response.sendRedirect("/index.html");
        return;
      }

      //get email of current user
      String userEmail = userService.getCurrentUser().getEmail();

      //get info that user put in
      // List<String> pastTopics = new ArrayList<String> (Arrays.asList( request.getParameter("pastTopics").split(",")));
      List<String> currentTopics = new ArrayList<String> (Arrays.asList( request.getParameter("currentTopics").split(",")));
      System.out.println(currentTopics);
      //get current user by querying the email
      User user = datastore.getUser(userEmail);
      if (user == null) {
        //set chats of current user to null
        List<String> chats = new ArrayList<String>();
        //create the user
        user = new User(userEmail, null, null, chats, null, null, null, null, null, null, currentTopics);
      }
      else{
        //modify the info
        // user.setPastTopics(pastTopics);
        user.setCurrentTopics(currentTopics);
      }
      //store the user
      datastore.storeUser(user);

      // redirect to community page
      // response.sendRedirect("/community.html?past=" + pastTopics + "&current=" + currentTopics);
      // Ingrid -- I think we can redirect the users after they put in all the info -- studypace & topics & timezone & nickname etc.
      // back demon
      response.sendRedirect(request.getHeader("referer"));

  }

}