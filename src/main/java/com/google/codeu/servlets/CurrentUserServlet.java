// not currently in use!!

package com.google.codeu.servlets;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import com.google.gson.Gson;

import com.google.codeu.data.User;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Replies current user
 */
@WebServlet("/current-user")
public class CurrentUserServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("HIT CU SERVLET");
    response.setContentType("application/json");

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    //return current user details
    String curUser = userService.getCurrentUser().getEmail();
    System.out.println(curUser);
    response.getOutputStream().println(curUser);

  }

}
