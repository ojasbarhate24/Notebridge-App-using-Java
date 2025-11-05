import java.io.Serializable;
import java.util.ArrayList;

public class Classroom implements Serializable {
    private String className;
    private String accessCode;
    private ArrayList<Note> notes;

    public Classroom(String className) {
        this.className = className;
        this.accessCode = "CLS" + (int)(Math.random() * 9000 + 1000);
        this.notes = new ArrayList<>();
    }

    public String getClassName() { return className; }
    public String getAccessCode() { return accessCode; }
    public ArrayList<Note> getNotes() { return notes; }

    public void addNote(String title, String authorEmail, String content) {
        notes.add(new Note(title, authorEmail, content));
    }
}
