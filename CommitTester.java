import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTester {
    private static TestUtils saveCommitTest;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        TestUtils commitTest = new TestUtils();
        commitTest.deleteFile("index");
        commitTest.deleteDirectory("objects");
        commitTest.initialize();
        saveCommitTest = commitTest;
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        saveCommitTest.deleteFile("index");
        saveCommitTest.deleteDirectory("objects");
    }
    @Test
    void testCommit1 ()
    {
        Index index = new Index ();
        
        try
        {
            index.addBlob ("index.txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            Commit commit = new Commit ("Markus", "First Commit", index);
            commit.saveCommit();
            assertFalse (commit.createTree(index).equals (""));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
