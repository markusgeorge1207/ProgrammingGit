import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TreeTest {

    @Test
    void testAdd() {
        Tree tree = new Tree();
        tree.add("blob : abcdef : file.txt");

        assertEquals("blob : abcdef : file.txt", tree.getBlobTree().get(0));
    }

    @Test
    void testAddDirectory() {
        Tree root = new Tree();

        assertThrows(IllegalArgumentException.class, () -> root.addDirectory("nonexistent-directory"));

        // You can add more comprehensive tests for addDirectory.
    }

    @Test
    void testGetSHA1() {
        Tree tree = new Tree();
        tree.add("blob : abcdef : file.txt");
        tree.add("tree : 123456 : folder");

       
        assertEquals("Expected SHA1 value", tree.getSHA1());
    }

    @Test
    void testRemove() {
        Tree tree = new Tree();
        tree.add("blob : abcdef : file.txt");
        tree.add("tree : 123456 : folder");

        tree.remove("blob : abcdef : file.txt");

        // Ensure that the specified entry is removed
        assertEquals("tree : 123456 : folder", tree.getBlobTree().get(0));
    }


    @Test
    void testHashFromString() {
        // Ensure that the SHA1 is calculated correctly for a given string
        String input = "Hello, World!";
        String expectedSHA1 = "2ef7bde608ce5404e97d5f042f95f89f1c1c87e5d5";
        String actualSHA1 = Tree.hashFromString(input);

        assertEquals(expectedSHA1, actualSHA1);
    }

    @Test
    void testByteToHex() {
        // Ensure that byteToHex converts bytes to a hexadecimal string correctly
        byte[] bytes = {0x12, 0x34, (byte) 0xAB};
        String expectedHex = "1234ab";
        String actualHex = Tree.byteToHex(bytes);

        assertEquals(expectedHex, actualHex);
    }
}
