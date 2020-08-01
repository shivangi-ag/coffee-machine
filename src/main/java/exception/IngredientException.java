package exception;

import model.FailureReason;

public class IngredientException extends Exception {

  private String ingredientName;
  private FailureReason reason;

  public IngredientException(String ingredientName, FailureReason reason) {
    this.ingredientName = ingredientName;
    this.reason = reason;
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public FailureReason getReason() {
    return reason;
  }
}
