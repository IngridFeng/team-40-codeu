/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.servlets;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.Chat;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.kefirsf.bb.TextProcessor;
import org.kefirsf.bb.BBProcessorFactory;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
   * an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Message> messages = datastore.getMessages(user);
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    // Get user
    String user = userService.getCurrentUser().getEmail();

    // Get text
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.simpleText());
    TextProcessor processor = BBProcessorFactory.getInstance().create();
    text = processor.process(text);
    String regex = "(https?://\\w+\\.\\S+\\.(png|jpg|gif))";
    String replacement = "<img src=\"$1\" />";
    String textWithImagesReplaced = text.replaceAll(regex, replacement);

    String regexVid = "(https?://\\w+\\.\\S+\\.(mp4|ogg))";
    String repVid = ("<video controls> "
    									+ "<source src=\"$1\" type=\"video/mp4\" > "
    									+ "</video>");
    String textWithVideosReplaced = textWithImagesReplaced.replaceAll(regexVid, repVid);

    // Get sentiment score
    Document doc = Document.newBuilder().setContent(textWithVideosReplaced).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    double score = sentiment.getScore();
    languageService.close();

    // get chat
    String chatName = request.getParameter("chat");
    Chat chat = datastore.getChat(chatName);
    String chatId = chat.getId().toString();

    Message message = new Message(chatId ,user, textWithVideosReplaced, score);
    datastore.storeMessage(message);

    response.sendRedirect("/user-page.html?user=" + user);
  }
}
