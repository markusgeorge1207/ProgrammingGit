import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {

    private String indexFile = "index";
    private Map<String, String> blobMap;
    private ArrayList <Tree> treeList;
    private File contentFile;

    public Index() {
        blobMap = new HashMap<>();
        treeList = new ArrayList<>();
        contentFile = new File (indexFile);
    }

    public boolean initProject(String indexFile) throws IOException {
        this.indexFile = indexFile;
        Path indexPath = Paths.get(indexFile);
        if (!Files.exists(indexPath)) {
            Files.createFile(indexPath);
            return true;
        }
        return false;
    }

    public void addBlob(String originalFileName) throws IOException {
        String fileContents = new String (Blob.readFile(originalFileName));
        String hash = "";
        try
        {
           hash = Blob.calculateSHA1(originalFileName);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        blobMap.put (originalFileName, hash);
        File objectsFile = new File ("objects", hash);
        if (!objectsFile.exists())
        {
            try (PrintWriter pw = new PrintWriter (objectsFile))
            {
                pw.print(fileContents);
            }
        }
        try (PrintWriter writer = new PrintWriter (new FileWriter ("Index", true)))
        {
            writer.println ("blob" + ": " + hash + ": " + originalFileName + "\n");
        }

        
    }


    public void removeBlobs(String originalFileName) throws IOException {
        
        blobMap.remove(originalFileName);

       
        List<String> lines = Files.readAllLines(Paths.get(indexFile));
        List<String> newLines = new ArrayList<>();

        
        for (String line : lines) {
            String[] parts = line.split(" : ");
            if (parts.length == 2 && !parts[0].equals(originalFileName)) {
                newLines.add(line);
            }
        }

        
        Files.write(Paths.get(indexFile), newLines);
    }

    public void addDirectory (String directoryName)
    {
        Tree dir = new Tree ("objects");
        File directory = new File (directoryName);

        writeDirectory (directory, dir);

        treeList.add(dir);
    }
    public void clearIndexFile ()
    {
        try 
        {
            Files.deleteIfExists(Paths.get(indexFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void writeDirectory (File directory, Tree tree)
    {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    Tree subdirectoryTree = new Tree();
                    writeDirectory(file, subdirectoryTree);
                    tree.addTreeEntry("tree", subdirectoryTree.getSHA1(), file.getName());
                } else if (file.isFile()) {
                    
                    StringBuilder content = new StringBuilder();
                try (FileReader reader = new FileReader(file)) {
                    int character;
                    while ((character = reader.read()) != -1) {
                        content.append((char) character);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
                String sha1 = Tree.hashFromString(content.toString());
                
                tree.addTreeEntry("blob", sha1, file.getName());
                }
            }
        }
    }

    
    public Map<String, String> getBlobMap() {
        return blobMap;
    }
    public ArrayList<Tree> getTreeList ()
    {
        return treeList;
    }
}