import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Input;
import model.Machine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CoffeeMachineTest {

  private CoffeeMachine coffeeMachine = new CoffeeMachine();
  private static Machine machine;

  @BeforeAll
  public static void initialize() throws FileNotFoundException {
    machine = getInput().getMachine();
  }

  @Test
  public void testBeveragePreparation() throws ExecutionException, InterruptedException {
    Map<String, Integer> originalInventory = machine.getIngredients();
    Map<String, Map<String, Integer>> beverageDetails = machine.getBeverages();

    List<CreateBeverageResult> results = coffeeMachine.run(machine);

    updateInventoryAfterBeveragesPrepared(originalInventory, beverageDetails, results);

    boolean isAnyIngredientMissing = false;
    boolean isAnyBeverageNotPrepared = false;

    for (CreateBeverageResult result : results) {
      if (!result.isSuccessful()) {
        isAnyBeverageNotPrepared = true;
        String beverageName = result.getBeverageName();
        Map<String, Integer> requiredIngredients = beverageDetails.get(beverageName);

        for (Map.Entry<String, Integer> ingredient : requiredIngredients.entrySet()) {
          String ingredientName = ingredient.getKey();
          Integer availableQuantity = originalInventory.getOrDefault(ingredientName, 0);
          Integer requiredQuantity = requiredIngredients.get(ingredientName);

          if (availableQuantity < requiredQuantity) {
            isAnyIngredientMissing = true;
            break;
          }
        }
      }
    }

    if (isAnyBeverageNotPrepared) {
      Assertions.assertTrue(isAnyIngredientMissing);
    }

    if (!isAnyBeverageNotPrepared) {
      Assertions.assertFalse(isAnyIngredientMissing);
    }

  }

  private void updateInventoryAfterBeveragesPrepared(Map<String, Integer> originalInventory,
      Map<String, Map<String, Integer>> beverageDetails, List<CreateBeverageResult> results) {
    for (CreateBeverageResult result : results) {
      if (result.isSuccessful()) {
        String beverageName = result.getBeverageName();
        Map<String, Integer> requiredIngredients = beverageDetails.get(beverageName);

        for (Map.Entry<String, Integer> ingredient : requiredIngredients.entrySet()) {
          String ingredientName = ingredient.getKey();
          Integer availableQuantity = originalInventory.get(ingredientName);
          Integer requiredQuantity = requiredIngredients.get(ingredientName);

          originalInventory.put(ingredientName, availableQuantity - requiredQuantity);
        }
      }
    }
  }

  private static Input getInput() throws FileNotFoundException {
    FileReader fileReader = new FileReader(getFileFromResources("inputData.json"));
    Gson gson = new Gson();
    return gson.fromJson(fileReader, Input.class);
  }

  private static File getFileFromResources(String fileName) {

    ClassLoader classLoader = CoffeeMachineTest.class.getClassLoader();
    URL resource = classLoader.getResource(fileName);

    if (resource == null) {
      throw new IllegalArgumentException("file is not found!");
    } else {
      return new File(resource.getFile());
    }

  }

}