package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.StudySession;
import com.google.codeu.data.User;
import com.google.codeu.data.Chat;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles fetching and saving study sessions.
 */
@WebServlet("/study-session")
public class StudySessionServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /*
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
    }
  */

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException {

      String chatId = request.getParameter("chatId");
      List<String> buddies = datastore.getUserEmailsByChat(chatId);

      String topic = request.getParameter("topic");
      String description = request.getParameter("description");
      String time = request.getParameter("time");
      //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
      //LocalDate localDate = LocalDate.parse(time, formatter);
      String location = request.getParameter("location");

      String allow = request.getParameter("public");
      boolean allowPublic = (allow != null);

      StudySession studySession = new StudySession(topic, description, buddies, chatId, time, location, allowPublic);

      // store study session
      datastore.storeStudySession(studySession);
      // send back
      response.sendRedirect(request.getHeader("referer"));
  }

}
