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

        Commit commit = new Commit("samskulsky", "This is a test");
        commit.saveCommit();

        Commit commit2 = new Commit("128b3bf909a02dea713c89f88c6cfa5c2d0a47ae", "samskulsky", "This is dasd a test");
        commit2.saveCommit();
    }
}