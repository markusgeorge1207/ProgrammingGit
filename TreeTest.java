import org.junit.jupiter.api.AfterEach;  
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    private static final String basicTest = "objects/";
    private final String advancedTest = "objects/";
    private String testFile = "index.txt";

    @AfterEach
    void tearDown() {
        deleteDirectory(new File(basicTest));
        deleteDirectory(new File(advancedTest));
    }

    @Test
    void testAddDirectoryBasic() throws IOException {
        File fold = new File("test1");
        fold.mkdirs();
        Tree tree = new Tree();

        for (int i = 0; i < 3; i++) {
            FileWriter fw = new FileWriter(new File(fold.getPath() + "/" + i));
            fw.write(i);
            fw.close();
        }

        tree.addDirectory ("test1");
        tree.save();
        assertNotNull(tree.getSHA1());
    }

    @Test
    void testAddTreeEntry ()
    {
        Blob test = null;
        Tree tree = new Tree();

        try
        {
            test = new Blob ();
            tree.addTreeEntry ("tree : ", test.calculateSHA1(testFile), testFile);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        assertTrue(tree.getTreeList().isEmpty());
    }
    @Test
    void testRemoveTreeEntry ()
    {
        Blob test = null;
        Tree tree = new Tree();

        try
        {
            test = new Blob ();
            tree.addTreeEntry ("tree : ", test.calculateSHA1(testFile), testFile);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        tree.remove (testFile);
        assertTrue(tree.getTreeList().isEmpty());
    }

    private void createTempFiles(String directoryPath, int numFiles) throws IOException {
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
