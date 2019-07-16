package com.google.codeu.data;
import java.util.UUID;


public class StudySession {
  private UUID id;
  private String topic;
  private String description;
  private List<Users> buddies;
  private String time; // might change Kind
  private String location; // might change Kind

  public StudySession(String name, String description, List<Users> buddies, String time, String location) {
    this(UUID.randomUUID(),name,description, buddies, time, location);
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

  public List<Users> getBuddies() {
    return buddies;
  }

  public String getTime() {
    return time;
  }

  public String getLocation() {
    return location;
  }

  public void setTopic(String name) {
    this.name = name;
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
