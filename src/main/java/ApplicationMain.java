import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import model.CreateBeverageResult;
import model.Input;

public class ApplicationMain {

  public static void main(String[] args)
      throws FileNotFoundException, InterruptedException, ExecutionException {

    CoffeeMachine coffeeMachine = new CoffeeMachine();
    List<CreateBeverageResult> output = coffeeMachine.run(getInput().getMachine());

    for (CreateBeverageResult result : output) {
      System.out.println(result.toString());
    }

  }

  private static Input getInput() throws FileNotFoundException {
    FileReader fileReader = new FileReader(getFileFromResources("inputData.json"));
    Gson gson = new Gson();
    return gson.fromJson(fileReader, Input.class);
  }

  private static File getFileFromResources(String fileName) {

    ClassLoader classLoader = ApplicationMain.class.getClassLoader();
    URL resource = classLoader.getResource(fileName);

    if (resource == null) {
      throw new IllegalArgumentException("file is not found!");
    } else {
      return new File(resource.getFile());
    }

  }
}
