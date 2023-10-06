import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public void initialize() throws IOException {
        File indexFile = new File("index");
        indexFile.createNewFile();
        Files.createDirectories(Paths.get("objects/"));
    }

    public  void writeStringToFile(String path, String contents) throws IOException {
        FileWriter fw = new FileWriter(path, false);
        fw.write(contents);
        fw.close();
    }

    public void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public void deleteDirectory(String path) {
        File file = new File(path);
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDirectory(f.getPath());
                }
            }
        }
        file.delete();
    }
}
