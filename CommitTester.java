import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class CommitTester {
    private static Index index;

    private static TestUtils saveCommitTest;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        TestUtils commitTest = new TestUtils();
        index = new Index();
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
    void testByteToHex() throws IOException {
        Commit commit = new Commit("test author", "summary", index);

        byte[] empty = {};
        assertEquals("", commit.byteToHex(empty));

        byte[] nonEmpty = { 0x00, 0x01, 0x02, 0x03 };
        assertEquals("00010203", commit.byteToHex(nonEmpty));
    }
    @Test
    void testCreateTree() throws IOException {
        try
        {
            index.addBlob("index.txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
         Commit commit = new Commit ("Markus", "First Commit", index);
         index.addBlob("index");
        assertEquals (commit.createTree(index), "da39a3ee5e6b4b0d3255bfef95601890afd80709");
    }

    @Test
    void testCommit1 ()
    {
        
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
            assertEquals (commit.createTree(index), "da39a3ee5e6b4b0d3255bfef95601890afd80709");
            assertNull (commit.getSHAofPreviousCommit());
            Commit commit2 = new Commit ("da39a3ee5e6b4b0d3255bfef95601890afd80709", "M", "Commit", index);
            commit2.saveCommit();
            File testCommit = new File ("./objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
            assertTrue(testCommit.exists());
            

        


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
     @Test
    void testGetDate() throws IOException {
        Calendar cal = Calendar.getInstance();
        index.addBlob("index");
        Commit commit = new Commit("test author", "summary", index);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(commit.getDate(), sdf.format(cal.getTime()));

    }
    @Test
    void testCommit2 ()
    {
        File file = new File ("file1.txt");
        File file2 = new File ("file2.txt");
        Tree dir = new Tree ("directory");
        
        try
        {
            index.addBlob ("index.txt");
            index.addBlob ("file1.txt");
            index.addDirectory("directory");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Commit commit2 = null;
        Commit commit3 = null;

        try
        {
            commit2 = new Commit ("Markus", "2nd commit", index);
            File file3 = new File ("file3.txt");
            index.addBlob ("file3");
            commit3 = new Commit ("Markus", "3rd", index);
        assertNull(commit2.getSHAofPreviousCommit());  
        assertNotNull(commit2.createTree(index));
        assertNotNull (commit2.getNextCommitSHA1());
        assertNotNull (commit3.createTree(index));
        assertNotNull (commit3.getSHAofPreviousCommit());
        assertEquals(commit3.getSHAofPreviousCommit(), commit2.createTree(index));
        assertEquals (commit2.getNextCommitSHA1(), commit3.createTree(index));
        commit2.saveCommit();
        commit3.saveCommit();
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Test

    void testCommits3and4()
    {
        File file4 = new File ("file4.txt");
        File file5 = new File ("file5.txt");
        Tree dir2 = new Tree ("directory2");
        

        try

        {
            index.addBlob("file4.txt");
            index.addDirectory("dir2");
            index.addBlob ("file5.txt");
            Commit commit4 = new Commit ("Markus", "4th", index);
            File file6 = new File ("file6.txt");
            File file7 = new File ("file7.txt");
            index.addBlob ("file6.txt");
            index.addBlob("file7.txt");
            Commit commit5 = new Commit ("Markus", "5th", index);
            assertTrue(commit5.getSHAofPreviousCommit().equals(commit4.createTree(index)));
            assertTrue (commit4.getNextCommitSHA1().equals (commit5.createTree(index)));
            assertNotNull (commit4.createTree(index));
            assertNotNull (commit5.createTree(index));
            File file8 = new File ("file8.txt");
            File file9 = new File ("file9.txt");
            Tree dir3 = new Tree ("directory3");
            index.addBlob("file8.txt");
            index.addBlob("file9.txt");
            index.addDirectory("dir3");
            Commit commit6 = new Commit ("Markus", "6th", index);
            File file10  = new File ("file10.txt");
            File file11 = new File ("file11.txt");
            index.addBlob("file10.txt");
            index.addBlob ("file11.txt");
            Commit commit7 = new Commit ("Markus", "7th", index);
            assertTrue(commit7.getSHAofPreviousCommit().equals(commit6.createTree(index)));
            assertTrue (commit6.getNextCommitSHA1().equals (commit7.createTree(index)));

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    void testCommitWithDeletionsAndEdits() {
        try
        {
        Commit initialCommit = new Commit ("Markus", "First Commit", index);
        
        // Commit 1: Edit file1.txt
        index.addBlob("file2.txt");
        index.addBlob("file3.txt");
        index.deleteFile("file2.txt");
        Commit commit2 = new Commit ("Markus", "2nd", index);
        assertFalse(commit2.getCommitTree(commit2.getSHA1()).contains("file2.txt"));
        index.addBlob ("file4.txt");
        String hash = Blob.calculateSHA1("file4.txt");
        index.editFile("file3.txt");
        assertNotEquals (hash, Blob.calculateSHA1("file4.txt"));
        Commit commit3 = new Commit ("Markus", "3rd", index);
        index.addBlob ("file5.txt");
        index.addBlob("file6.txt");
        index.deleteFile("file5.txt");
        index.deleteFile("file6.txt");
        assertEquals(commit3.createTree(index), commit2.createTree(index));
        assertEquals (initialCommit.getNextCommitSHA1(), commit3.getSHA1(), commit2.getSHA1());
        assertEquals (commit3.getSHAofPreviousCommit(), commit2.getSHAofPreviousCommit(), initialCommit.getSHA1());


        
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

   
}
