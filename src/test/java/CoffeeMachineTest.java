import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Input;
import model.Machine;
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
    List<CreateBeverageResult> results = coffeeMachine.run(machine);

    System.out.println("done");

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