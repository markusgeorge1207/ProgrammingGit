import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    private static final String TEST_DIRECTORY_BASIC = "objects/";
    private static final String TEST_DIRECTORY_ADVANCED = "objects/";

    @BeforeEach
    void setUp() {
       
        new File(TEST_DIRECTORY_BASIC).mkdir();
        new File(TEST_DIRECTORY_ADVANCED).mkdir();
    }

    @Test
    void testAddDirectoryWithFilesOnly() throws IOException {
        setUp();
        createTemporaryFiles(TEST_DIRECTORY_BASIC, 3);

        Tree tree = new Tree();

        tree.addDirectory(TEST_DIRECTORY_BASIC);

        
        assertEquals(3, tree.getBlobTree().size());

        
        deleteDirectory(new File(TEST_DIRECTORY_BASIC));
    }

    @Test
    void testAddDirectoryWithFilesAndFolders() throws IOException {
        
        Blob blob = new Blob ();
        Index index = new Index();
        index.initProject(blob.getSha1());
        createComplexDirectoryStructure(TEST_DIRECTORY_ADVANCED);

        Tree tree = new Tree();

        
        tree.addDirectory(TEST_DIRECTORY_ADVANCED);

        
        assertEquals(4, tree.getBlobTree().size());
        assertEquals(2, tree.getChildTrees().size());

        
        deleteDirectory(new File(TEST_DIRECTORY_ADVANCED));
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

    private void createComplexDirectoryStructure(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        createTemporaryFiles(directoryPath, 3);

        File folder1 = new File(directory, "folder1");
        folder1.mkdir();
        createTemporaryFiles(folder1.getAbsolutePath(), 2);

        File folder2 = new File(directory, "folder2");
        folder2.mkdir();
        createTemporaryFiles(folder2.getAbsolutePath(), 1);

        File subfolder = new File(folder1, "subfolder");
        subfolder.mkdir();
        File fileInSubfolder = new File(subfolder, "subfile.txt");
        fileInSubfolder.createNewFile();
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }
}
