import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.io.BufferedReader;
import java.util.Map;
import java.util.List;
import java.nio.file.*;

public class Commit {

    private String shaOfTreeObject;
    private String shaOfPreviousCommit;
    private String shaOfNextCommit;
    private String author;
    private String summary;
    private Index content;

    public Commit(String parentSha1, String author, String summary, Index index) throws IOException {
        shaOfPreviousCommit = parentSha1;

        shaOfTreeObject = createTree(index);
        this.author = author;
        this.summary = summary;
    }

    public Commit(String author, String summary, Index index) throws IOException {
        shaOfTreeObject = createTree(index);
        this.author = author;
        this.summary = summary;
    }

    public void saveCommit() throws IOException {
        FileWriter fw = new FileWriter(new File("objects/" + generateSha1()));

        fw.write(shaOfTreeObject + "\n");
        fw.write(shaOfPreviousCommit + "\n");
        fw.write(shaOfNextCommit + "\n");
        fw.write(author + "\n");
        fw.write(getDate() + "\n");
        fw.write(summary);

        fw.close();

        if (shaOfPreviousCommit != null && !shaOfPreviousCommit.isEmpty()) {
            changeNextCommitOfPreviousCommit();
        }
    }

    public void changeNextCommitOfPreviousCommit() throws IOException {
        File inputFile = new File("objects/" + shaOfPreviousCommit);
        File tempFile = new File("__temp__");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;
        int i = 0;

        while ((currentLine = reader.readLine()) != null) {
            if (i == 2) {
                writer.write(generateSha1() + "\n");
            } else if (i != 5) {
                writer.write(currentLine + "\n");
            } else {
                writer.write(currentLine);
            }
            i++;
        }
        writer.close();
        reader.close();
        tempFile.renameTo(inputFile);
    }

    public String createTree(Index index) throws IOException {
        Tree tree = new Tree();

        Map <String, String> blobMap = index.getBlobMap();
        List <Tree> treeList = index.getTreeList();

        for (Map.Entry<String, String> entry : blobMap.entrySet())
        {
            tree.addTreeEntry ("blob",entry.getValue(), entry.getKey());
        }
        for (Tree dir : treeList)
        {
            String directoryName = dir.getFileName();
            String treeSHA1 = dir.getSHA1();
            tree.addTreeEntry ("tree", treeSHA1, directoryName);
        }
        if (shaOfPreviousCommit != null && !shaOfPreviousCommit.isEmpty())
        {
            tree.addTreeEntry("tree",shaOfPreviousCommit,"prev_commit");
        }
        index.clearIndexFile();
        
       return tree.calculateTreeSHA1();
    }

    public String getCommitTree (String commitSHA1)
    {
        Path commitPath = Paths.get ("objects",commitSHA1);
        List <String> lines = new ArrayList<>();
        try
        {
           lines = Files.readAllLines (commitPath);
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
    if (lines.isEmpty())
    {
        throw new IllegalArgumentException();
    }
    String firstLine = lines.get(0);

    String [] parts = firstLine.split (" : ");
    if (parts.length < 2 || !parts[0].equals("tree"))
    {
        throw new IllegalArgumentException();
    }

    String treeSHA1 = parts[1];
    return treeSHA1;
}
public void update (String prevSHA1, String newSHA1)
{
    Path commitPath = Paths.get("objects", prevSHA1);
    List<String> lines = new ArrayList ();
    try
    {
        lines = Files.readAllLines (commitPath);
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
    lines.set(2, newSHA1);
    try
    {
        Files.write (commitPath, lines);
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
}

    public String generateSha1() throws IOException {
        String toEncrypt = shaOfTreeObject + "\n" + shaOfPreviousCommit + "\n" + author + "\n" + getDate() + "\n"
                + summary;

        String passwordString = toEncrypt;
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(passwordString.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

}
