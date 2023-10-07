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
import java.util.HashSet;

public class Tree {
    private ArrayList<String> blobTree;
    private ArrayList<Tree> childTrees;
    private HashSet<String> fileNameList;
    private ArrayList<String> treeList;
    private HashSet<String> sha1List;
    private String sha1; 
    private String fileName;

    public Tree() {
        blobTree = new ArrayList<String>();
        childTrees = new ArrayList<Tree>();
        sha1 = ""; 
        treeList = new ArrayList<String>();
        fileNameList = new HashSet<String>();
        sha1List = new HashSet<String>();
    }
    public Tree (String fileName)
    {
        blobTree = new ArrayList<String>();
        childTrees = new ArrayList<Tree>();
        sha1 = ""; 
        treeList = new ArrayList<String>();
        this.fileName = fileName;
        fileNameList = new HashSet<String>();
        sha1List = new HashSet<String>();
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
                treeList.remove (indexLine);
            }
        }
    }

    public static String hashFromString(String input) {
        StringBuilder hexString = new StringBuilder();
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes());

        
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
    public String addDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
    
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }
        Tree tree = new Tree();
        File[] files = directory.listFiles();
    
        for (File file : files) {
            if (file.isDirectory()) {
                Tree childTree = new Tree();
                tree.addTreeEntry("tree", childTree.addDirectory(file.getPath()), file.getName());
            } else if (file.isDirectory()) {
                Tree childTree = new Tree();
                childTree.addDirectory(file.getAbsolutePath());
                childTrees.add(childTree);
            }
        }
    
        sha1 = calculateTreeSHA1();
        save();
        return tree.getSHA1();
    }
    public String getFileName ()
    {
        return fileName;
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
    public String calculateTreeSHA1() {
        StringBuilder treeContent = new StringBuilder();
        for (String line : blobTree) {
            treeContent.append(line).append("\n");
        }
        for (Tree child : childTrees) {
            treeContent.append("tree : ").append(child.getSHA1()).append(" : child\n");
        }

        return hashFromString(treeContent.toString());
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
    public void addTreeEntry(String fileType, String sha1, String fileName) {
        if (fileType.equals("tree")) {
            if (!treeList.contains(fileType + " : " + sha1)) {
                if (fileName.isEmpty()) {
                    treeList.add(fileType + " : " + sha1);
                } else {
                    treeList.add(fileType + " : " + sha1 + " : " + fileName);
                    fileNameList.add(fileName);
                }
                sha1List.add(sha1);
            }

        }
        else if (fileType.equals("blob")) {
            if (!treeList.contains(fileType + " : " + sha1 + " : " + fileName)) {
                treeList.add(fileType + " : " + sha1 + " : " + fileName);
                fileNameList.add(fileName);
                sha1List.add(sha1);
            }
        }
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
    public ArrayList<String> getTreeList()
    {
        return treeList;
    }
}