package com.google.codeu.data;

import com.google.codeu.data.Chat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
  private String email;
  private String aboutMe;
  private String nickName;
  private List<String> chats;
  private String imageUrl;
  private String universityName;
  private String major;
  private Long timezone;
  private String studypace;
  private List<String> pastTopics;
  private List<String> currentTopics;

  public User(String email, String aboutMe, String nickName, List<String> chats, String imageUrl, String universityName, String major, Long timezone, String studypace, List<String> pastTopics, List<String> currentTopics) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.nickName = nickName;
    this.chats = chats;
    this.imageUrl = imageUrl;
    this.universityName = universityName;
    this.major = major;
    this.timezone = timezone;
    this.studypace = studypace;
    this.pastTopics = pastTopics;
    this.currentTopics = currentTopics;
  }

  public List<String> getPastTopics() {
    return pastTopics;
  }

  public List<String> getCurrentTopics() {
    return currentTopics;
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

  public List<String> getChats() {
    if (chats == null){
      return new ArrayList<String>();
    }
    else {
      return chats;
    }
  }

  public String getImageUrl() {
  	return imageUrl;
  }

  public String getUniversityName() {
    return universityName;
  }

  public String getMajor() {
    return major;
  }

  public Long getTimeZone() {
    return timezone;
  }

  public String getStudyPace() {
    return studypace;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public void setChats(List<String> chats) {
    this.chats = chats;
  }

  public void setImageUrl(String imageUrl) {
  	this.imageUrl = imageUrl;
  }

  public void setPastTopics(List<String> pastTopics) {
    this.pastTopics = pastTopics;
  }

  public void setCurrentTopics(List<String> currentTopics) {
    this.currentTopics = currentTopics;
  }

  public void setUniversityName(String universityName) {
    this.universityName = universityName;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public void setTimeZone(Long timezone) {
    this.timezone = timezone;
  }

  public void setStudyPace(String studypace) {
    this.studypace = studypace;
  }
}
