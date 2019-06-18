package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.University;
import com.google.gson.Gson;


/**
 * Handles fetching and saving university name data.
 */
@WebServlet("/university")
public class UniversityServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "Add your university" section.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    List<University> universities = datastore.getAllUniversities();
    Gson gson = new Gson();
    String json = gson.toJson(universities);
   	response.getOutputStream().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // UserService userService = UserServiceFactory.getUserService();
    // if (!userService.isUserLoggedIn()) {
    //   response.sendRedirect("/index.html");
    //   return;
    // } // TO DO: automatically add university to user profile

    University university = new University(request.getParameter("university"));
    datastore.storeUniversity(university);
    System.out.println("university stored");

  }
}
