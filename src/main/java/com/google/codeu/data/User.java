package com.google.codeu.data;

public class User {

  private String email;
  private String aboutMe;
  private String nickName;
  // private String favoriteSubject;
  // private String favoriteFood;

  public User(String email, String aboutMe, String nickName) {
    this.email = email;
    if (aboutMe != null){
      this.aboutMe = aboutMe;
    }
    if (nickName != null){
      this.nickName = nickName;
    }
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
}
