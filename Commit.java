import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Commit {

    public Commit(String parentSha1, String author, String summary) {

    }

    public Commit(String author, String summary) {

    }

    public String createTree() throws IOException {
        Tree tree = new Tree();
        return tree.getSha1();
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

}
