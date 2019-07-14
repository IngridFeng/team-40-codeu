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

package com.google.codeu.data;

import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("chat", message.getChat());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("sentiment", message.getSentiment());
    messageEntity.setProperty("profilePic", message.getProfilePic());

    datastore.put(messageEntity);
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has never posted a
   *     message. List is sorted by time descending.
   */
  public List<Message> getMessages(String user) {
    List<Message> messages = new ArrayList<>();

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String chat = (String) entity.getProperty("chat");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        double sentiment = (double) entity.getProperty("sentiment");
        String imageUrl = (String) entity.getProperty("imageUrl");
        String profilePic = (String) entity.getProperty("profilePic");

        Message message = new Message(id, chat, user, text, timestamp, sentiment, imageUrl, profilePic);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }

  /**
   * Gets messages posted by all users.
   *
   * @return a list of messages posted by all users, or empty list if there isn't any message. List is sorted by time descending.
   */
  public List<Message> getAllMessages(){
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String chat = (String) entity.getProperty("chat");
      	String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        double sentiment = (double) entity.getProperty("sentiment");
        String imageUrl = (String) entity.getProperty("imageUrl");
        String profilePic = (String) entity.getProperty("profilePic");

        Message message = new Message(id, chat, user, text, timestamp, sentiment, imageUrl, profilePic);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
}

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /**
   * Gets a set of all users.
   * return a set of strings representing the users.
   */
  public List<User> getUsers(String topic, String timezone, String pace){
    System.out.println("REACHED DATASTORE");
    System.out.println(topic);
    System.out.println(timezone);
    System.out.println(pace);

    List<User> users = new ArrayList<>();
    Query query;

    /*
    // I should really find a better way
    boolean pastIsNull = past.indexOf("null") !=-1? true: false;
    boolean curIsNull = current.indexOf("null") !=-1? true: false;

    if (pastIsNull && curIsNull){
      query = new Query("User").addSort("nickName", SortDirection.DESCENDING);
    } else {
      // process params

      past = past.substring(1, past.length()-1);
      current = current.substring(1, current.length()-1);

      List<String> pastParams = Arrays.asList(past.split(","));
      List<String> currentParams = Arrays.asList(current.split(","));

      // curently only filter on currentTopics
      query = new Query("User").setFilter(new Query.FilterPredicate("currentTopics", FilterOperator.IN, currentParams));
    }
    */

    query = new Query("User").addSort("nickName", SortDirection.DESCENDING);


    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String email = (String) entity.getProperty("email");
        String aboutMe = (String) entity.getProperty("aboutMe");
        String nickName = (String) entity.getProperty("nickName");
        List<String> chats = (List<String>) entity.getProperty("chats");
        String imageUrl = (String) entity.getProperty("imageUrl");
        String universityName = (String) entity.getProperty("universityName");
        String major = (String) entity.getProperty("major");
        List<String> pastTopics = (List<String>) entity.getProperty("pastTopics");
        List<String> currentTopics = (List<String>) entity.getProperty("currentTopics");

        User user = new User(email, aboutMe, nickName, chats, imageUrl, universityName, major, pastTopics, currentTopics);
        users.add(user);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return users;



    /*
  	Set<String> users = new HashSet<>();
  	Query query = new Query("Message");
  	PreparedQuery results = datastore.prepare(query);
  	for(Entity entity : results.asIterable()) {
  		users.add((String) entity.getProperty("user"));
  	}
  	return users;
    */
  }


  /** Stores the User in Datastore. */
 public void storeUser(User user) {
  Entity userEntity = new Entity("User", user.getEmail());
  userEntity.setProperty("email", user.getEmail());
  userEntity.setProperty("aboutMe", user.getAboutMe());
  userEntity.setProperty("nickName", user.getNickName());
  userEntity.setProperty("chats", user.getChats());
  userEntity.setProperty("imageUrl", user.getImageUrl());
  userEntity.setProperty("profilePic", user.getImageUrl());
  userEntity.setProperty("universityName", user.getUniversityName());
  userEntity.setProperty("major", user.getMajor());
  userEntity.setProperty("pastTopics", user.getPastTopics());
  userEntity.setProperty("currentTopics", user.getCurrentTopics());
  datastore.put(userEntity);
 }

 /**
  * Returns the User owned by the email address, or
  * null if no matching User was found.
  */
 public User getUser(String email) {

  Query query = new Query("User")
    .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
  PreparedQuery results = datastore.prepare(query);
  Entity userEntity = results.asSingleEntity();
  if(userEntity == null) {
   return null;
  }

  String nickName = (String) userEntity.getProperty("nickName");
  String aboutMe = (String) userEntity.getProperty("aboutMe");
  List<String> chats= (List<String>) userEntity.getProperty("chats");
  String imageUrl = (String) userEntity.getProperty("imageUrl");
  String universityName = (String) userEntity.getProperty("universityName");
  String major = (String) userEntity.getProperty("major");
  List<String> pastTopics = (List<String>) userEntity.getProperty("pastTopics");
  List<String> currentTopics = (List<String>) userEntity.getProperty("currentTopics");
  User user = new User(email, aboutMe, nickName, chats, imageUrl, universityName, major, pastTopics, currentTopics);
  return user;
 }

 /**
  * Store chat
  */
 public void storeChat(Chat chat) {
   Entity chatEntity = new Entity("Chat", chat.getId().toString());
   chatEntity.setProperty("name", chat.getName());
   chatEntity.setProperty("description", chat.getDescription());
   datastore.put(chatEntity);
 }

 /**
  * Get chat from chat name
  */
 public Chat getChatbyName(String name) {
  Query query = new Query("Chat")
    .setFilter(new Query.FilterPredicate("name", FilterOperator.EQUAL, name));
  PreparedQuery results = datastore.prepare(query);
  Entity chatEntity = results.asSingleEntity();
  if(chatEntity == null) {
    Chat chat = new Chat(name,"");
    return chat;
  }
  else {
    String idString = chatEntity.getKey().getName();
    UUID id = UUID.fromString(idString);
    String description = (String) chatEntity.getProperty("description");
    Chat chat = new Chat(id,name,description);
    return chat;
  }
 }


 /**
  * Returns chat by chat id
  */
 public Chat getChatbyId(String id) {
  try {
    Key chatKey = KeyFactory.createKey("Chat", id);
    Entity chatEntity = datastore.get(chatKey);
    String name = (String) chatEntity.getProperty("name");
    String description = (String) chatEntity.getProperty("description");
    UUID chatId = UUID.fromString(id);
    Chat chat = new Chat(chatId,name,description);
    return chat;
  } catch (EntityNotFoundException expected) {
    return null;
  }


 }

 /**
  * Returns all existing chats.
  */

 public List<Chat> getAllChats(){
   List<Chat> chats = new ArrayList<>();

   Query query = new Query("Chat").addSort("name", SortDirection.DESCENDING);
   PreparedQuery results = datastore.prepare(query);

   for (Entity entity : results.asIterable()) {
     try {
       String idString = entity.getKey().getName();
       UUID id = UUID.fromString(idString);
       String name = (String) entity.getProperty("name");
       String description = (String) entity.getProperty("description");
       Chat chat = new Chat(id, name, description);
       chats.add(chat);
     } catch (Exception e) {
       System.err.println("Error reading chat.");
       System.err.println(entity.toString());
       e.printStackTrace();
     }
   }
   return chats;
  }


/**
 * Returns the Messages associated with the Chat
 * null if none was found.
 */
  public List<Message> getMessagesbyChat(String chat) {
    List<Message> messages = new ArrayList<>();
    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("chat", FilterOperator.EQUAL, chat))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        double sentiment = (double) entity.getProperty("sentiment");
        String imageUrl = (String) entity.getProperty("imageUrl");
        String profilePic = (String) entity.getProperty("profilePic");

        Message message = new Message(id, chat, user, text, timestamp, sentiment, imageUrl, profilePic);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }
}
