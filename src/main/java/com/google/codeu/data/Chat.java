package com.google.codeu.data;
import java.util.UUID;


public class Chat {
  private UUID id;
  private String name;
  private String description;

  public Chat(String name, String description) {
    this(UUID.randomUUID(),name,description);
  }

  public Chat(UUID id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public UUID getId() {
    return id;
  }

  public String getName(){
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
