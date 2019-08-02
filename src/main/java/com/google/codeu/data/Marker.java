package io.happycoding.data;

public class Marker {

  private double lat;
  private double lng;
  private String content;
  // private Long timestamp;
  private String universityAddress;

  public Marker(double lat, double lng, String content, String universityAddress) {
    this.lat = lat;
    this.lng = lng;
    // this.timestamp = timestamp;
    this.content = content;
    this.universityAddress = universityAddress;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getContent() {
    return content;
  }

  // public Long getTimestamp() {
  //   return timestamp;
  // }

  public String getUniversityAddress() {
    return universityAddress;
  }
}
