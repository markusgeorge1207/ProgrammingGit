import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class Tree {
    private ArrayList<String> blobTree;
    private ArrayList<Tree> childTrees;
    private static String sha1; // Not static

    public Tree() {
        blobTree = new ArrayList();
        childTrees = new ArrayList();
        sha1 = ""; // Initialize sha1 for this instance
    }

    public void add(String indexLine) {
        if (!(blobTree.contains(indexLine))) {
            blobTree.add(indexLine);
        }
    }

    public void remove(String indexLine) {
        for (int i = blobTree.size() - 1; i >= 0; i--) {
            if (blobTree.get(i).contains(indexLine)) {
                blobTree.remove(i);
            }
        }
    }

    public static String hashFromString(String input) {
        StringBuilder hexString = new StringBuilder();
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes());

        // Convert the byte array to a hexadecimal string
         hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return hexString.toString();
    }
    public void addDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String sha1 = addBlob(file);
                String entry = "blob : " + sha1 + " : " + file.getName();
                add(entry);
            } else if (file.isDirectory()) {
                Tree childTree = new Tree();
                childTree.addDirectory(file.getAbsolutePath());
                childTrees.add(childTree);

                String entry = "tree : " + childTree.getSHA1() + " : " + file.getName();
                add(entry);
            }
        }

       
        save();
    }
    public String getSHA1()
    {
        return sha1;
    }
    private String addBlob(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader reader = new FileReader(file)) {
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
        }

        String sha1 = hashFromString(content.toString());

        File blobFile = new File("./objects/" + sha1);
        try (FileWriter writer = new FileWriter(blobFile)) {
            writer.write(content.toString());
        }

        return sha1;
    }
    public ArrayList<Tree> getChildTrees ()
    {
        return childTrees;
    }


    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public void save() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blobTree.size(); i++) {
            sb.append(blobTree.get(i));
            if (i < blobTree.size() - 1) {
                sb.append("\n");
            }
        }
        String hash = hashFromString(sb.toString());
        File tempFile = new File("./objects/" + hash);
        tempFile.createNewFile();

        FileWriter fw = new FileWriter(tempFile);
        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }

    public ArrayList<String> getBlobTree() {
        return blobTree;
    }
}