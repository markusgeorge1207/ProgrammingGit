import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {

    private String indexFile = "index";
    private Map<String, String> blobMap;

    public Index() {
        blobMap = new HashMap<>();
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

    public void createBlobs(String originalFileName, String sha1Hash) throws IOException {
        if (initProject (originalFileName))
        {
            blobMap.put(originalFileName, sha1Hash);
            String entry = originalFileName + " : " + sha1Hash;
        Files.write(Paths.get(indexFile), (entry + System.lineSeparator()).getBytes());
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

    public Map<String, String> getBlobMap() {
        return blobMap;
    }
}