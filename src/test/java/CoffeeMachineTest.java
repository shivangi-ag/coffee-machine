import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Input;
import model.Machine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

class CoffeeMachineTest {

  //UPDATE DATA IN THIS FILE TO RUN VARIOUS TEST CASES
  private static final String fileInput = "inputData.json";

  private CoffeeMachine coffeeMachine = new CoffeeMachine();
  private static Machine machine;

  @BeforeAll
  public static void initialize() throws FileNotFoundException {
    machine = getInput().getMachine();
  }

  private static Input getInput() throws FileNotFoundException {
    FileReader fileReader = new FileReader(getFileFromResources(fileInput));
    Gson gson = new Gson();
    return gson.fromJson(fileReader, Input.class);
  }

  @TestFactory
  public List<DynamicTest> testBeveragePreparation()
      throws ExecutionException, InterruptedException {
    Map<String, Integer> originalInventory = machine.getIngredients();
    Map<String, Map<String, Integer>> beverageDetails = machine.getBeverages();

    List<CreateBeverageResult> results = coffeeMachine.run(machine);

    List<DynamicTest> tests = new ArrayList<>();
    Executable executable;
    DynamicTest test;

    for (CreateBeverageResult result : results) {
      if (result.isSuccessful()) {
        boolean allIngredientsAvailable = true;
        String beverageName = result.getBeverageName();
        Map<String, Integer> requiredIngredients = beverageDetails.get(beverageName);

        for (Map.Entry<String, Integer> ingredient : requiredIngredients.entrySet()) {
          String ingredientName = ingredient.getKey();
          Integer availableQuantity = originalInventory.get(ingredientName);
          Integer requiredQuantity = requiredIngredients.get(ingredientName);

          if (availableQuantity - requiredQuantity < 0) {
            allIngredientsAvailable = false;
          }

          originalInventory.put(ingredientName, availableQuantity - requiredQuantity);
        }
        boolean ans = allIngredientsAvailable;
        executable = () -> assertTrue(ans);
        test = DynamicTest.dynamicTest("Beverage prepared: " + beverageName, executable);
        tests.add(test);
      }
    }

    for (CreateBeverageResult result : results) {
      if (!result.isSuccessful()) {
        String beverageName = result.getBeverageName();
        String ingredientMissing = result.getIngredientMissing();

        Integer availableQuantity = originalInventory.getOrDefault(ingredientMissing, 0);
        Integer requiredQuantity = beverageDetails.get(beverageName).get(ingredientMissing);

        executable = () -> assertTrue(availableQuantity < requiredQuantity);
        test = DynamicTest.dynamicTest("Beverage not prepared: " + beverageName, executable);
        tests.add(test);
      }
    }

    return tests;

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