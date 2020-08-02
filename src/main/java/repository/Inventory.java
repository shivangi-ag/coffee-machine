package repository;

import exception.IngredientException;
import java.util.HashMap;
import java.util.Map;
import model.FailureReason;

/**
 * Singleton class that acts as repository for all ingredients stocked up in coffee machine
 */
public class Inventory {

  private static Inventory inventory;
  private Map<String, Integer> quantityByIngredients;
  private static final int MIN_QUANTITY = 50;

  private Inventory() {
    quantityByIngredients = new HashMap<>();
  }

  public static Inventory getInstance() {
    if (inventory == null) {
      inventory = new Inventory();
    }
    return inventory;
  }

  public void initialiseInventory(Map<String, Integer> ingredients) {
    quantityByIngredients = new HashMap<>(ingredients);
  }

  /**
   * Lock is acquired on inventory when machine is preparing one beverage
   *
   * @throws IngredientException if any of the ingredient is missing or insufficient
   */
  public synchronized void prepareBeverage(Map<String, Integer> beverageIngredients)
      throws IngredientException {

    Map<String, Integer> transactionLog = new HashMap<>();

    for (Map.Entry<String, Integer> entry : beverageIngredients.entrySet()) {
      String ingredientName = entry.getKey();
      Integer requiredQuantity = entry.getValue();
      Integer availableQuantity = quantityByIngredients.getOrDefault(ingredientName, 0);

      if (availableQuantity == 0) {
        doInventoryRollback(transactionLog);
        throw new IngredientException(ingredientName, FailureReason.UNAVAILABLE);
      }

      int updatedQuantity = availableQuantity - requiredQuantity;
      if (updatedQuantity < 0) {
        doInventoryRollback(transactionLog);
        throw new IngredientException(ingredientName, FailureReason.INSUFFICIENT);
      }

      transactionLog.put(ingredientName, availableQuantity);
      quantityByIngredients.put(ingredientName, updatedQuantity);
    }

    inventoryCheck();
  }

  /**
   * Indicates when ingredients are running low
   */
  private void inventoryCheck() {
    quantityByIngredients.entrySet().stream().forEach(e -> {
      if (e.getValue() < MIN_QUANTITY) {
        System.out.println(
            "REFILL ALERT! " + e.getKey() + " is running low at: " + e.getValue());
      }
    });
  }

  /**
   * Coffee Machine won't be able to order any beverage while inventory is being refilled
   */
  public synchronized void refillInventory(String ingredientName, Integer refillQuantity) {
    Integer existingInventory = quantityByIngredients.getOrDefault(ingredientName, 0);
    quantityByIngredients.put(ingredientName, existingInventory + refillQuantity);
  }

  private void doInventoryRollback(Map<String, Integer> transactionLog) {
    for (Map.Entry<String, Integer> entry : transactionLog.entrySet()) {
      quantityByIngredients.put(entry.getKey(), entry.getValue());
    }
  }


}
