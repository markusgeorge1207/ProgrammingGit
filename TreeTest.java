import org.junit.jupiter.api.AfterEach;  // Import the AfterEach annotation
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    private static final String basicTest = "objects/";
    private final String advancedTest = "objects/";

    @BeforeEach
    void setUp() {
        // No need to create directories here. You can create them directly in test methods.
    }

    @AfterEach
    void tearDown() {
        deleteDirectory(new File(basicTest));
        deleteDirectory(new File(advancedTest));
    }

    @Test
    void testAddDirectoryBasic() throws IOException {
        new File(basicTest).mkdir();

        
        createTemporaryFiles(basicTest, 3);

        Tree tree = new Tree();

        tree.addDirectory(basicTest);

        assertEquals(3, tree.calculateBlobCount());
    }

    @Test
    void testAddDirectoryAdvanced() throws IOException {
        new File(advancedTest).mkdir();

        Blob blob = new Blob();
        Index index = new Index();
        index.initProject(blob.getSha1());


        Tree tree = new Tree();

        tree.addDirectory(advancedTest);

        assertEquals(7, tree.getBlobTree().size());
        assertEquals(2, tree.getChildTrees().size());
    }

    private void createTemporaryFiles(String directoryPath, int numFiles) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (int i = 1; i <= numFiles; i++) {
            File file = new File(directory, "file" + i + ".txt");
            file.createNewFile();
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
            directory.delete();
        }
    }
}
