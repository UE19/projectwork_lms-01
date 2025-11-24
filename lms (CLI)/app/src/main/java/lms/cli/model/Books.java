package lms.cli.model;

public class Books {
    private int bookId;
    private String title;
    private String author;
    private boolean isIssued;
    private String issuedToStudentId; // Null if not issued
    private java.time.LocalDateTime issueDateTime;
    private java.time.LocalDate returnDate;

    public Books(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedToStudentId = null;
        this.issueDateTime = null;
        this.returnDate = null;
    }

    //Getters.
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }
    public String getIssuedToStudentId() { return issuedToStudentId; }
    public java.time.LocalDateTime getIssueDateTime() { return issueDateTime; }
    public java.time.LocalDate getReturnDate() { return returnDate; }

    //Issuing/Returning Logic (Setters).
    public void issueBook(String studentId, java.time.LocalDateTime issueTime, java.time.LocalDate returnDate) {
        this.isIssued = true;
        this.issuedToStudentId = studentId;
        this.issueDateTime = issueTime;
        this.returnDate = returnDate;
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedToStudentId = null;
        this.issueDateTime = null;
        this.returnDate = null;
    }

    @Override
    public String toString() {
        String status = isIssued ? "ISSUED (TO: " + issuedToStudentId + ", Returned by Date: " + returnDate + ")" : "AVAILABLE";
        return "ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Status: " + status;
    }
} 
