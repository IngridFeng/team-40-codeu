package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Handles fetching all users for the community page.
 */
@WebServlet("/chats-list")
public class ChatsListServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      response.setContentType("application/json");

      UserService userService = UserServiceFactory.getUserService();
      if (!userService.isUserLoggedIn()) {
      	response.sendRedirect("/login");
      	return;
      }

      //get email of current user
      String userEmail = userService.getCurrentUser().getEmail();
      //get current user by querying the email
      User user = datastore.getUser(userEmail);

      String topic = request.getParameter("topic");
      String timezone = request.getParameter("timezone");
      String studypace = request.getParameter("studypace");
      List<User> users = datastore.getUsersWithParams(topic, timezone, studypace);
      List<User> result = new ArrayList<>();

      for (User selectedUser : users) {
			  String selectedUserEmail = selectedUser.getEmail();
        if (user != null && selectedUser != null) {
          if (!userEmail.equals(selectedUserEmail)) {
            // user and selected user are different
            List<String> userChats = user.getChats();
            List<String> selectedUserChats = selectedUser.getChats();
            List<String> common = new ArrayList<String>(userChats);
            common.retainAll(selectedUserChats);
            if (common.size() > 0) {
              // a chat already exists between the two users
              result.add(selectedUser);
            }
          }
        }
		  }

      Gson gson = new Gson();
      String json = gson.toJson(result);
      response.getOutputStream().println(json);
    }
}
