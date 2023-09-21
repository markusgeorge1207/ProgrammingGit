import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static void initialize() throws IOException {
        File indexFile = new File("index");
        indexFile.createNewFile();
        Files.createDirectories(Paths.get("objects/"));
    }

    public static void main(String[] args) throws IOException {
        TestUtils.initialize();
        Tree tree = new Tree();
    }
}