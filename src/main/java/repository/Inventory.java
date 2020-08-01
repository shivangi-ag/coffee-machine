package repository;

import exception.IngredientException;
import java.util.HashMap;
import java.util.Map;
import model.FailureReason;

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
  }

  private void doInventoryRollback(Map<String, Integer> transactionLog) {
    for (Map.Entry<String, Integer> entry : transactionLog.entrySet()) {
      quantityByIngredients.put(entry.getKey(), entry.getValue());
    }
  }


}
