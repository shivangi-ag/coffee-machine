package model;

import com.google.gson.annotations.SerializedName;

public class Outlet {

  @SerializedName("count_n")
  private int count;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
