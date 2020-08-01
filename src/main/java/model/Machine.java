package model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Machine {

  @SerializedName("outlets")
  private Outlet outlet;

  @SerializedName("total_items_quantity")
  private Map<String, Integer> ingredients;

  @SerializedName("beverages")
  private Map<String, Map<String, Integer>> beverages;

  public Outlet getOutlet() {
    return outlet;
  }

  public void setOutlet(Outlet outlet) {
    this.outlet = outlet;
  }

  public Map<String, Integer> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Map<String, Integer> ingredients) {
    this.ingredients = ingredients;
  }

  public Map<String, Map<String, Integer>> getBeverages() {
    return beverages;
  }

  public void setBeverages(
      Map<String, Map<String, Integer>> beverages) {
    this.beverages = beverages;
  }
}