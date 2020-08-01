package task;

import exception.IngredientException;
import java.util.Map;
import java.util.concurrent.Callable;
import model.CreateBeverageResult;
import repository.Inventory;

public class CreateBeverageTask implements Callable<CreateBeverageResult> {

  private Map<String, Integer> beverageIngredients;
  private String beverageName;

  public CreateBeverageTask(String beverageName, Map<String, Integer> beverageIngredients) {
    this.beverageIngredients = beverageIngredients;
    this.beverageName = beverageName;
  }

  public CreateBeverageResult call() {

    CreateBeverageResult result = new CreateBeverageResult();
    result.setBeverageName(beverageName);

    try {
      Inventory.getInstance().prepareBeverage(beverageIngredients);
      result.setSuccessful(true);
    } catch (IngredientException e) {
      result.setSuccessful(false);
      result.setIngredientMissing(e.getIngredientName());
      result.setReason(e.getReason());
    }
    return result;

  }
}
