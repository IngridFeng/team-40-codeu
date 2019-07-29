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

import java.util.UUID;

/** A single message posted by a user. */
public class Message {

  private UUID id;
  private String chat;
  private String user;
  private String text;
  private long timestamp;
  /*private double sentiment; */
  private String imageUrl;
  private String profilePic;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
   * random ID and uses the current system time for the creation time.
   */
  public Message(String chat, String user, String text, String imageUrl, String profilePic) {
    this(UUID.randomUUID(), chat, user, text, System.currentTimeMillis(), imageUrl, profilePic);
  }

  public Message(UUID id, String chat, String user, String text, long timestamp, String imageUrl, String profilePic) {
    this.id = id;
    this.chat = chat;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    /*this.sentiment = sentiment; */
    this.imageUrl = imageUrl;
    this.profilePic = profilePic;
  }

  public UUID getId() {
    return id;
  }

  public String getUser() {
    return user;
  }

  public String getText() {
    return text;
  }

  public String getChat() {
    return chat;
  }

  public long getTimestamp() {
    return timestamp;
  }

  /*
  public double getSentiment() {
    return sentiment;
  }
  */

  public String getImageUrl() {
  	return imageUrl;
  }

  public String getProfilePic() {
    return profilePic;
  }
}
