package com.google.codeu.data;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class StudySession {
  private UUID id;
  private String topic;
  private String description;
  private List<User> buddies;
  private String time; // might change Kind
  private String location; // might change Kind

  public StudySession(String topic, String description, List<User> buddies, String time, String location) {
    this(UUID.randomUUID(),topic,description, buddies, time, location);
  }

  public StudySession(UUID id, String topic, String description, List<User> buddies, String time, String location) {
    this.id = id;
    this.topic = topic;
    this.description = description;
    this.buddies = buddies;
    this.time = time;
    this.location = location;
  }

  public UUID getId() {
    return id;
  }

  public String getTopic(){
    return topic;
  }

  public String getDescription() {
    return description;
  }

  public List<User> getBuddies() {
    return buddies;
  }

  public String getTime() {
    return time;
  }

  public String getLocation() {
    return location;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBuddies() {
    this.buddies =  buddies;
  }

  public void setTime() {
    this.time = time;
  }

  public void setLocation() {
    this.location = location;
  }
}
