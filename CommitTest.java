import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    // Tests Tree's SHA-1
    @Test
    void testCreateTree() throws IOException {
        Commit commit = new Commit("test author", "summary");
        assertEquals("Wrong Tree SHA-1", commit.createTree(), "da39a3ee5e6b4b0d3255bfef95601890afd80709");
    }

    // Test's Commit's overall sha-1
    @Test
    void testGenerateSha1() throws IOException {
        Commit commit = new Commit("test author", "summary");
        assertEquals("Wrong commit SHA-1", commit.generateSha1(), "aa8716c4dd2483417691a41d1b94c977de4c8d00");
    }

    @Test
    void testGetDate() throws IOException {
        Calendar cal = Calendar.getInstance();
        Commit commit = new Commit("test author", "summary");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Corect date", commit.getDate(), sdf.format(cal.getTime()));

    }

    @Test
    void testSaveCommit() {

    }
}
