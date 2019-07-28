package com.google.codeu.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

/** Handles fetching searched messages for the public feed. */
@WebServlet("/feedSearch")
public class MessageFeedSearchServlet extends HttpServlet{

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /** Responds with a JSON representation of Message data for search results. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String searchKey = request.getParameter("key");
    List<Message> messages = datastore.getAllMessages();
    List<Message> result = new ArrayList<Message>();
    for(Message m : messages) {
    	if (Pattern.compile(Pattern.quote(searchKey), Pattern.CASE_INSENSITIVE).matcher(m.getText()).find() ||
    			Pattern.compile(Pattern.quote(searchKey), Pattern.CASE_INSENSITIVE).matcher(m.getUser()).find()) {
    		result.add(m);
    	} 
    }
    
    Gson gson = new Gson();
    String json = gson.toJson(result);
  	response.getOutputStream().println(json);
  }
}
