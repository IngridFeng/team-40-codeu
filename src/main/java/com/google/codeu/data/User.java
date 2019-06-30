package com.google.codeu.data;

import com.google.codeu.data.Chat;
import java.util.List;
import java.util.UUID;

public class User {
  private String email;
  private String aboutMe;
  private String nickName;
  private List<UUID> chats;
  private String imageUrl;

  public User(String email, String aboutMe, String nickName, List<UUID> chats, String imageUrl) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.nickName = nickName;
    this.chats = chats;
    this.imageUrl = imageUrl;
  }

  public String getEmail(){
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public String getNickName() {
    return nickName;
  }

  public List<UUID> getChats() {
    return chats;
  }
  
  public String getImageUrl() {
  	return imageUrl;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public void setChats(List<UUID> chats) {
    this.chats = chats;
  }
  
  public void setImageUrl(String imageUrl) {
  	this.imageUrl = imageUrl;
  }
  
}
