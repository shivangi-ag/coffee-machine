import java.util.List;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Machine;
import service.CoffeeMachineBuilder;

public class CoffeeMachine {

  public List<CreateBeverageResult> run(Machine machine)
      throws InterruptedException, ExecutionException {

    CoffeeMachineBuilder coffeeMachineBuilder = new CoffeeMachineBuilder();
    List<CreateBeverageResult> readyBeverages = coffeeMachineBuilder
        .stockInventory(machine.getIngredients())
        .startOutlets(machine.getOutlet().getCount())
        .prepareBeverages(machine.getBeverages())
        .shutDownOutlets()
        .build();

    readyBeverages.sort((one, two) -> {
      if (one.isSuccessful() && two.isSuccessful()) {
        return 0;
      } else if (one.isSuccessful()) {
        return -1;
      } else {
        return 1;
      }
    });

    return readyBeverages;
  }

}
