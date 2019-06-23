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
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("sentiment", message.getSentiment());
    messageEntity.setProperty("imageUrl", message.getImageUrl());

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
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        double sentiment = (double) entity.getProperty("sentiment");

        String imageUrl = (String) entity.getProperty("imageUrl");

        Message message = new Message(id, user, text, timestamp, sentiment, imageUrl);
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
	      String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        double sentiment = (double) entity.getProperty("sentiment");
        String imageUrl = (String) entity.getProperty("imageUrl");

        Message message = new Message(id, user, text, timestamp, sentiment, imageUrl);
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
  public Set<String> getUsers(){
  	Set<String> users = new HashSet<>();
  	Query query = new Query("Message");
  	PreparedQuery results = datastore.prepare(query);
  	for(Entity entity : results.asIterable()) {
  		users.add((String) entity.getProperty("user"));
  	}
  	return users;
  }


  /** Stores the User in Datastore. */
 public void storeUser(User user) {
  Entity userEntity = new Entity("User", user.getEmail());
  userEntity.setProperty("email", user.getEmail());
  userEntity.setProperty("aboutMe", user.getAboutMe());
  userEntity.setProperty("nickName", user.getNickName());
  userEntity.setProperty("chats", user.getChats());
  datastore.put(userEntity);
 }

 /** Stores the University the User inputs. */
public void storeUniversity(University university) {
  Entity universityEntity = new Entity("University", university.getUniversity());
  universityEntity.setProperty("university", university.getUniversity());
  datastore.put(universityEntity);
  System.out.println((String) universityEntity.getProperty("university"));
}

/** Return all universities. */
public List<University> getAllUniversities(){
  List<University> universities = new ArrayList<>();

  Query query = new Query("University");
  PreparedQuery results = datastore.prepare(query);

  for (Entity entity : results.asIterable()) {
    try {
      University university = new University((String) entity.getProperty("university"));
      universities.add(university);
    } catch (Exception e) {
      System.err.println("Error reading university.");
      System.err.println(entity.toString());
      e.printStackTrace();
    }
  }
  return universities;
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
  List<UUID> chats= (List<UUID>) userEntity.getProperty("chats");
  User user = new User(email, aboutMe, nickName, chats);
  return user;
 }
}
