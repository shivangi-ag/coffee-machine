package model;

public class CreateBeverageResult {

  private String beverageName;
  private boolean isSuccessful;
  private String ingredientMissing;
  private FailureReason reason;

  public String getBeverageName() {
    return beverageName;
  }

  public void setBeverageName(String beverageName) {
    this.beverageName = beverageName;
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public void setSuccessful(boolean successful) {
    isSuccessful = successful;
  }

  public String getIngredientMissing() {
    return ingredientMissing;
  }

  public void setIngredientMissing(String ingredientMissing) {
    this.ingredientMissing = ingredientMissing;
  }

  public FailureReason getReason() {
    return reason;
  }

  public void setReason(FailureReason reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return "CreateBeverageResult{" +
        "beverageName='" + beverageName + '\'' +
        ", isSuccessful=" + isSuccessful +
        ", ingredientMissing='" + ingredientMissing + '\'' +
        ", reason=" + reason +
        '}';
  }
}
