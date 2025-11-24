package lms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book {
    private int id;
    private String title;
    private String author;
    private boolean isIssued;
    private String issuedToStudentId;
    private LocalDateTime issueDateTime;
    private LocalDate returnDate;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean getIsIssued() { return isIssued; }
    public String getIssuedToStudentId() { return issuedToStudentId; }
    public LocalDateTime getIssueDateTime() { return issueDateTime; }
    public LocalDate getReturnDate() { return returnDate; }

    // Used when loading from Database
    public void restoreIssueState(String studentId, LocalDateTime issueTime, LocalDate returnDate) {
        this.isIssued = true;
        this.issuedToStudentId = studentId;
        this.issueDateTime = issueTime;
        this.returnDate = returnDate;
    }
    
    // Normal in-memory issue (optional now as DB handles it, but good for UI updates if not reloading DB)
    public void issue(String studentId, int days) {
        this.isIssued = true;
        this.issuedToStudentId = studentId;
        this.issueDateTime = LocalDateTime.now();
        this.returnDate = LocalDate.now().plusDays(days);
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedToStudentId = null;
        this.issueDateTime = null;
        this.returnDate = null;
    }
}