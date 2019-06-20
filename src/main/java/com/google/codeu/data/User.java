package com.google.codeu.data;

import com.google.codeu.data.Chat;
import java.util.List;

public class User {
  private String email;
  private String aboutMe;
  private String nickName;
  private List<Chat> chats;

  public User(String email, String aboutMe, String nickName, List<Chat> chats) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.nickName = nickName;
    this.chats = chats;
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

  public List<Chat> getChats() {
    return chats;
  }


  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public void setChats(List<Chat> chats) {
    this.chats = chats;
  }
}
