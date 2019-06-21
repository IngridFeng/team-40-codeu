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
      String user = request.getParameter("user");
      System.out.println("Hi");
      System.out.println(user);
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
