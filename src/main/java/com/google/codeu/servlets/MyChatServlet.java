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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;



/**
 * Handles fetching and saving user data.
 */
@WebServlet("/mychats")
public class MyChatServlet extends HttpServlet {

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
      	response.sendRedirect("/login");
      	return;
      }

      //get emails of users
      String userEmail = userService.getCurrentUser().getEmail();
      String selectedUserEmail = request.getParameter("selectedUser");
      
      //get nick names
      String userNick = datastore.getUser(userEmail).getNickName();
      String selectedUserNick = datastore.getUser(selectedUserEmail).getNickName();
    
      //get current user by querying the email
      User user = datastore.getUser(userEmail);
      // get selected user by querying the email
      User selectedUser = datastore.getUser(selectedUserEmail);

      // check if a chat exists between the two users
      if (user != null && selectedUser != null) {
      	if (!userEmail.equals(selectedUserEmail)) {
      		// user and selected user are different
      		List<String> userChats = user.getChats();
      		List<String> selectedUserChats = selectedUser.getChats();
      		List<String> common = new ArrayList<String>(userChats);
      		common.retainAll(selectedUserChats);
      		if (common.size() > 0) {
      			// a chat already exists between the two users
      			String chatId = common.get(0);
      			// redirect to chat package
      			response.sendRedirect("/mychats.html?chat=" + chatId);
      			return;
      		}
      	} else {
      		// user and selected user are actually the same person
      		List<String> userChats = user.getChats();
      		Set<String> unique = new HashSet<String>();
      		for(int i = 0; i < userChats.size(); i++) {
      			if (unique.contains(userChats.get(i))) {
      				// a chat already exists
      				String chatId = userChats.get(i);
      				// redirect to chat package
      				response.sendRedirect("/mychats.html?chat=" + chatId);
      				return;
      			} else {
      				unique.add(userChats.get(i));
      			}
      		}
      	}
      }

      // chat doesn't exist between the two users
      // create new chat
      Chat chat = new Chat(userNick + " and " + selectedUserNick,"Omg a new friend! :D");

      if (user == null) {
      	//create new user
      	List<String> userChats = new ArrayList<String>();
      	userChats.add(chat.getId().toString());

      	user = new User(userEmail, null, null, userChats, null, null, null, null, null, null, null);
      }
      else{
      	//modify the chats
      	List<String> userChats = user.getChats();
      	userChats.add(chat.getId().toString());
      	user.setChats(userChats);
      }

      if (selectedUser == null) {
      	//create new user
      	List<String> selectedUserChats = new ArrayList<String>();
      	selectedUserChats.add(chat.getId().toString());
      	if (userEmail.equals(selectedUserEmail)) {
      		selectedUserChats.add(chat.getId().toString());
      	}
      	selectedUser = new User(userEmail, null, null, selectedUserChats, null, null, null, null, null, null, null);
      }
      else {
      	//modify the chats
      	List<String> selectedUserChats = selectedUser.getChats();
      	selectedUserChats.add(chat.getId().toString());
      	if (userEmail.equals(selectedUserEmail)) {
      		selectedUserChats.add(chat.getId().toString());
      	}
      	selectedUser.setChats(selectedUserChats);
      }

      // store updated users
      datastore.storeUser(user);
      datastore.storeUser(selectedUser);
      // store chat
      datastore.storeChat(chat);

      // redirect to chat package
      response.sendRedirect("/mychats.html?chat=" + chat.getId());
      return;

  }

}
