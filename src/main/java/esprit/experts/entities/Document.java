package esprit.experts.entities;

import java.sql.Timestamp;

public class Document {
    private int id;
    private String title;
    private String author;
    private String attachmentPath; // Store path or URL instead of Blob
    private Timestamp dateOfInsertion;

    public Document() {
    }

    public Document(String title, String author, String attachmentPath) {
        this.title = title;
        this.author = author;
        this.attachmentPath = attachmentPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Timestamp getDateOfInsertion() {
        return dateOfInsertion;
    }

    public void setDateOfInsertion(Timestamp dateOfInsertion) {
        this.dateOfInsertion = dateOfInsertion;
    }

    public String getAttachmentStatus() {
        return attachmentPath != null ? "Attached" : "Not Attached";
    }
}
