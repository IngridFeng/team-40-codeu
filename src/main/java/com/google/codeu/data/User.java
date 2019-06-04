package com.google.codeu.data;

public class User {
  private String email;
  private String aboutMe;
  private String nickName;

  public User(String email, String aboutMe, String nickName) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.nickName = nickName;
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

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }
}
