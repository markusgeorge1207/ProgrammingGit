import java.io.File;
import java.io.IOException;

public class Tree {
    public Tree() throws IOException {
        File file = new File("objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        file.createNewFile();
    }

    public String getSha1() {
        return "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    }
}