package repository;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

  private static Inventory inventory;
  private Map<String, Integer> quantityByIngredients;

  private Inventory() {
    quantityByIngredients = new HashMap<String, Integer>();
  }

  public static Inventory getInstance() {
    if (inventory == null) {
      inventory = new Inventory();
    }
    return inventory;
  }

  public void initialiseInventory(Map<String, Integer> ingredients) {
    quantityByIngredients = ingredients;
  }

}
