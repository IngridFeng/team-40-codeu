package com.google.codeu.data;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
//import java.time.LocalDate;

public class StudySession {
  private UUID id;
  private String topic;
  private String description;
  private List<String> buddies;
  //private LocalDate time;
  private String time;
  private String location; // might change Kind
  private boolean allowPublic;

  public StudySession(String topic, String description, List<String> buddies, String time, String location, boolean allowPublic) {
    this(UUID.randomUUID(), topic, description, buddies, time, location, allowPublic);
  }

  public StudySession(UUID id, String topic, String description, List<String> buddies, String time, String location, boolean allowPublic) {
    this.id = id;
    this.topic = topic;
    this.description = description;
    this.buddies = buddies;
    this.time = time;
    this.location = location;
    this.allowPublic = allowPublic;
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

  public List<String> getBuddies() {
    return buddies;
  }

  public String getTime() {
    return time;
  }

  public String getLocation() {
    return location;
  }

  public boolean getAllowPublic() {
    return allowPublic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBuddies(List<String> buddies) {
    this.buddies =  buddies;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setAllowPublic(boolean allowPublic) {
    this.allowPublic = allowPublic;
  }
}
