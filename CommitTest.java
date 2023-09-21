import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTest {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        TestUtils.deleteFile("index");
        TestUtils.deleteDirectory("objects");
        TestUtils.initialize();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        TestUtils.deleteFile("index");
        TestUtils.deleteDirectory("objects");
    }

    // Tests byte array to hex string conversion
    @Test
    void testByteToHex() throws IOException {
        Commit commit = new Commit("test author", "summary");

        byte[] empty = {};
        assertEquals("Incorrect behavior for empty array", "", commit.byteToHex(empty));

        byte[] nonEmpty = { 0x00, 0x01, 0x02, 0x03 };
        assertEquals("Incorrect behavior for non-empty array", "00010203", commit.byteToHex(nonEmpty));
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
