import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Input;
import model.Machine;
import repository.Inventory;
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

    return readyBeverages;
  }

}
