package telega;

public class Note {
    private int id;
    private final String title;
    private final String text;
    private final long userId;

    public Note(String title, String text, long userId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
    }

    public Note(int id, String title, String text, long userId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }
}

