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
import java.util.Iterator;
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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
      System.out.println("Hi");
    }

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
      String selectedUserEmail = request.getParameter("selectedUserEmail");

      System.out.println(userEmail);

      // create new chat
      Chat chat = new Chat(userEmail + " and " + selectedUserEmail,"Omg a new friend! :D");

      //get current user by querying the email
      User user = datastore.getUser(userEmail);
      if (user == null) {
        //create new user
        List<UUID> userChats = new ArrayList<UUID>();
        userChats.add(chat.getId());

        user = new User(userEmail, null, null, userChats, null);
      }
      else{
        //modify the chats
        List<UUID> userChats = user.getChats();
        System.out.println(userChats);
        userChats.add(chat.getId());
        user.setChats(userChats);
      }

      // get selected user my querying the email
      User selectedUser = datastore.getUser(selectedUserEmail);
      if (selectedUser == null) {
        //create new user
        List<UUID> selectedUserChats = new ArrayList<UUID>();
        selectedUserChats.add(chat.getId());
        selectedUser = new User(userEmail, null, null, selectedUserChats, null);
      }
      else{
        //modify the chats
        List<UUID> selectedUserChats = selectedUser.getChats();
        selectedUserChats.add(chat.getId());
        selectedUser.setChats(selectedUserChats);
      }

      System.out.println("Hi");
      System.out.println(user);
      System.out.println(selectedUser);
      System.out.println(chat);
      System.out.println(user.getChats());
      System.out.println(selectedUser.getChats());

    }
}

  /**
   * upon posting, log a new chat
   * currently, only direct messages. Groups in the future.
   */

    /**
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get the users involved
    List<String> users = request.getParameter('users');

    // get the chat name
    String chatName = request.getParameter('chatName');
    String chatDescription = request.getParameter('chatDescription');

    // create new chat
    chat = new Chat(chatName, chatDescription);

    // store the new chat instance for each user
    while (users.hasNext()) {
      try {
        System.oit.println(iterator.next());
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
  }

  **/

  // send users to the new chat

  /**
    * upon getting, read all chats and return a JSON?
     check with userlist servlet
  */


    /**
    throws IOException {

    }

    response.getOutputStream().println();
    **/
