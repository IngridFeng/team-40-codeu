package com.google.codeu.data;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class StudySession {
  private UUID id;
  private String topic;
  private String description;
  private List<User> buddies;
  private LocalDate time;
  private String location; // might change Kind
  private boolean allowPublic;

  public StudySession(String topic, String description, List<User> buddies, LocalDate time, String location, boolean allowPublic) {
    this(UUID.randomUUID(), topic, description, buddies, time, location, allowPublic);
  }

  public StudySession(UUID id, String topic, String description, List<User> buddies, LocalDate time, String location, boolean allowPublic) {
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

  public List<User> getBuddies() {
    return buddies;
  }

  public LocalDate getTime() {
    return time;
  }

  public String getLocation() {
    return location;
  }

  public boolean getPublic() {
    return allowPublic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBuddies(List<User> buddies) {
    this.buddies =  buddies;
  }

  public void setTime(LocalDate time) {
    this.time = time;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void allowPublic(boolean allowPublic) {
    this.allowPublic = allowPublic;
  }
}
