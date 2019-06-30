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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * Handles fetching and saving user data.
 */
@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /** Returns JSON of chat details with matching id  **/

  // TO DO: Get the getChatbyId to work
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      response.setContentType("application/json");
      String chatId = request.getParameter("chatId");

      Chat chat = datastore.getChatbyId(chatId);
      Gson gson = new Gson();
      String json = gson.toJson(chat);
      response.getWriter().println(json);
    }

  /** Creates and stores a new Chat **/

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      response.setContentType("text/html");

      // get user

      UserService userService = UserServiceFactory.getUserService();
      if (!userService.isUserLoggedIn()) {
        response.sendRedirect("/index.html");
        return;
      }

      //get emails of users
      String userEmail = userService.getCurrentUser().getEmail();
      String selectedUserEmail = request.getParameter("selectedUser");
      System.out.println(selectedUserEmail);

      // create new chat
      Chat chat = new Chat(userEmail + " and " + selectedUserEmail,"Omg a new friend! :D");

      //get current user by querying the email
      User user = datastore.getUser(userEmail);
      if (user == null) {
        //create new user
        List<UUID> userChats = new ArrayList<UUID>();
        userChats.add(chat.getId());

        user = new User(userEmail, null, null, userChats);
      }
      else{
        //modify the chats
        List<UUID> userChats = user.getChats();
        userChats.add(chat.getId());
        user.setChats(userChats);
      }

      // get selected user my querying the email
      User selectedUser = datastore.getUser(selectedUserEmail);
      if (selectedUser == null) {
        //create new user
        List<UUID> selectedUserChats = new ArrayList<UUID>();
        selectedUserChats.add(chat.getId());
        selectedUser = new User(userEmail, null, null, selectedUserChats);
      }
      else{
        //modify the chats
        List<UUID> selectedUserChats = selectedUser.getChats();
        selectedUserChats.add(chat.getId());
        selectedUser.setChats(selectedUserChats);
      }

      // store chat
      System.out.println(chat.getName());
      System.out.println(chat.getDescription());
      datastore.storeChat(chat);

      // redirect to chat package
      response.sendRedirect("/chat.html?chat=" + chat.getId());
      return;



    }

}
