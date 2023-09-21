import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTest {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        TestUtils.deleteFile("index");
        TestUtils.deleteDirectory("objects");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        TestUtils.deleteFile("index");
        TestUtils.deleteDirectory("objects");
    }

    @Test
    void testByteToHex() {

    }

    @Test
    void testChangeNextCommitOfPreviousCommit() {

    }

    @Test
    void testCreateTree() {

    }

    @Test
    void testGenerateSha1() {

    }

    @Test
    void testGetDate() {

    }

    @Test
    void testSaveCommit() {

    }
}
