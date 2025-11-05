import java.io.Serializable;

public class Note implements Serializable {
    private String title;
    private String authorEmail;
    private String content;

    public Note(String title, String authorEmail, String content) {
        this.title = title;
        this.authorEmail = authorEmail;
        this.content = content;
    }

    @Override
    public String toString() {
        return title + " (by " + authorEmail + ")\n" + content;
    }
}
