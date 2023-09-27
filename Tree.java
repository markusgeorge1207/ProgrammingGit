import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tree {
    private ArrayList<String> blobTree;
    private ArrayList<Tree> childTrees;
    private static String sha1 = "";

    public Tree() {
        blobTree = new ArrayList();
        childTrees = new ArrayList();
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

    public static String hashFromString(String str) {
         sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }
    public void addDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                // Process files and add as blobs
                String sha1 = addBlob(file);
                String entry = "blob : " + sha1 + " : " + file.getName();
                add(entry);
            } else if (file.isDirectory()) {
                // Recursively process subdirectories and add as child trees
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
        // Read the file content

        try (FileReader reader = new FileReader(file)) {
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
        }

        String sha1 = hashFromString(content.toString());

        // Save the blob
        File blobFile = new File("./objects/" + sha1);
        try (FileWriter writer = new FileWriter(blobFile)) {
            writer.write(content.toString());
        }

        return sha1;
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