package model;

public enum FailureReason {

  UNAVAILABLE("not available"),
  INSUFFICIENT("not sufficient");

  String value;

  FailureReason(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
