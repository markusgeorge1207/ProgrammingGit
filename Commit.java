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
import java.util.Calendar;
import java.util.Formatter;

public class Commit {

    private String shaOfTreeObject;
    private String shaOfPreviousCommit;
    private String shaOfNextCommit;
    private String author;
    private String summary;

    public Commit(String parentSha1, String author, String summary) throws IOException {
        shaOfPreviousCommit = parentSha1;

        shaOfTreeObject = createTree();
        this.author = author;
        this.summary = summary;
    }

    public Commit(String author, String summary) throws IOException {
        shaOfTreeObject = createTree();
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

    public String createTree() throws IOException {
        Tree tree = new Tree();
        return tree.getSha1();
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
