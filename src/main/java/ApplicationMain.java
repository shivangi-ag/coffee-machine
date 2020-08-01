import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import model.Input;

public class ApplicationMain {

  public static void main(String[] args) throws FileNotFoundException {
    FileReader fileReader = new FileReader(getFileFromResources("inputData.json"));
    Gson gson = new Gson();
    Input input = gson.fromJson(fileReader, Input.class);
    System.out.println("done");
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
