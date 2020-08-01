package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.CreateBeverageResult;
import repository.Inventory;
import task.CreateBeverageTask;

public class CoffeeMachineBuilder {

  private ExecutorService outlets;
  private List<CreateBeverageResult> results = new ArrayList<>();

  public CoffeeMachineBuilder stockInventory(Map<String, Integer> ingredients) {
    Inventory.getInstance().initialiseInventory(ingredients);
    return this;
  }

  public CoffeeMachineBuilder startOutlets(int outletCount) {
    outlets = Executors.newFixedThreadPool(outletCount);
    return this;
  }

  public CoffeeMachineBuilder prepareBeverages(
      Map<String, Map<String, Integer>> beverages) throws InterruptedException, ExecutionException {

    Set<CreateBeverageTask> tasks = createTasks(beverages);

    List<Future<CreateBeverageResult>> futures = outlets.invokeAll(tasks);
    waitForCompletion(futures);

    for (Future<CreateBeverageResult> future : futures) {
      results.add(future.get());
    }

    return this;
  }

  public CoffeeMachineBuilder shutDownOutlets() {
    outlets.shutdown();
    return this;
  }

  public List<CreateBeverageResult> build() {
    return results;
  }

  private Set<CreateBeverageTask> createTasks(Map<String, Map<String, Integer>> beverages) {
    Set<CreateBeverageTask> taskSet = new HashSet<>();

    for (Map.Entry<String, Map<String, Integer>> entry : beverages.entrySet()) {
      CreateBeverageTask task = new CreateBeverageTask(entry.getKey(), entry.getValue());
      taskSet.add(task);
    }
    return taskSet;
  }

  private void waitForCompletion(List<Future<CreateBeverageResult>> futures) {
    while (true) {
      boolean flag = true;
      for (Future future : futures) {
        if (!future.isDone()) {
          flag = false;
          break;
        }
      }

      if (flag) {
        break;
      }
    }
  }

}
