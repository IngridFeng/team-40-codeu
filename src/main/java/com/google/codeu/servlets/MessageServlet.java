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
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.User;
import com.google.codeu.data.Chat;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;

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
    String chat = request.getParameter("chat");

    if (chat != null && !chat.equals("")){
      // Get by chat
      List<Message> messages = datastore.getMessagesbyChat(chat);
      Gson gson = new Gson();
      String json = gson.toJson(messages);
      response.getWriter().println(json);
    }

    else if (user != null && !user.equals("")) {
      // Get by user
      List<Message> messages = datastore.getMessages(user);
      Gson gson = new Gson();
      String json = gson.toJson(messages);
      response.getWriter().println(json);
    }

    else {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }


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

    // store the user if the user isn't stored already. 
    if (datastore.getUser(user) == null) {
    	//set chats of current user to null
    	List<String> chats = new ArrayList<String>();
    	//create the user
    	User newUser = new User(user, null, null, chats, null, null, null, null, null);
    	datastore.storeUser(newUser);
    }
    
    // Get nickName
    String nickName = datastore.getUser(user).getNickName();

    // Get text
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.simpleText());
    TextProcessor processor = BBProcessorFactory.getInstance().create();
    text = processor.process(text);

    // Get image
    String imageUrl = getUploadedFileUrl(request, "image");

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

    String chat = request.getParameter("chat");
    
    // Profile picture of user
    String profilePic = datastore.getUser(user).getImageUrl();


    Message message = new Message(chat , nickName, textWithVideosReplaced, score, imageUrl, profilePic);
    datastore.storeMessage(message);
    response.sendRedirect(request.getHeader("referer"));
  }

  /**
   * Returns a URL that points to the uploaded file, or null if the user didn't upload a file.
   */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName){
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a URL. (devserver)
    if(blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // We could check the validity of the file here, e.g. to make sure it's an image file
    // https://stackoverflow.com/q/10779564/873165

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    return imagesService.getServingUrl(options);
  }
}
