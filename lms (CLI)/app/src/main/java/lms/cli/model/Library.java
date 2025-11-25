package lms.cli.model;

import java.time.temporal.ChronoUnit;
import java.time.*;
import java.util.*;

public class Library {
    private Map<Integer, Books> books; //HashMap for Books.
    private Map<String, Student> students; //HashMap for Students.
    private double fineRatePerDay = 5.0; // Default fine of ₹5.0 per day

    public Library() {
        books = new HashMap<>();
        students = new HashMap<>();
    }

    // --- Book Management ---
    public void addBook(int id, String title, String author) {
        if (books.containsKey(id)) {
            System.out.println("Error: Book ID " + id + " already exists.");
            return;
        }
        books.put(id, new Books(id, title, author));
        System.out.println("Book added: " + title);
    }

    public void removeBook(int id) {
        Books b = books.get(id);
        if (b == null) {
            System.out.println("Error: Book ID " + id + " not found.");
        } else if (b.isIssued()) {
             System.out.println("Error: Book '" + b.getTitle() + "' is currently issued. Cannot remove.");
        } else {
            books.remove(id);
            System.out.println("Book ID " + id + " removed.");
        }
    }
    
    // --- Student Management ---
    public void addStudent(String id, String name) {
        if (students.containsKey(id)) {
            System.out.println("Error: Student ID " + id + " already exists.");
            return;
        }
        students.put(id, new Student(id, name));
        System.out.println("Student added: " + name);
    }

    // --- Fine Management ---
    public void setFineRate(double rate) {
        if (rate < 0) {
            System.out.println("Error: Fine rate cannot be negative.");
            return;
        }
        this.fineRatePerDay = rate;
        System.out.println("Fine rate set to $" + String.format("%.2f", rate) + " per day.");
    }

    public void getFineDetails(String studentId) {
        Student s = students.get(studentId);
        if (s == null) {
            System.out.println("Error: Student ID " + studentId + " not found.");
            return;
        }
        System.out.println("Fine details for " + s.getName() + ": **$" + String.format("%.2f", s.getFineAmount()) + "**");
    }

    // --- Issue/Return Operations ---
    public void issueBook(int bookId, String studentId, int daysToReturn) {
        Books book = books.get(bookId);
        Student student = students.get(studentId);

        if (book == null) {
            System.out.println("\nIssue Failed: Book ID " + bookId + " not found.");
            return;
        }
        if (student == null) {
            System.out.println("\nIssue Failed: Student ID " + studentId + " not found.");
            return;
        }
        if (book.isIssued()) {
            System.out.println("\nIssue Failed: Book '" + book.getTitle() + "' is already issued.");
            return;
        }

        LocalDateTime issueTime = LocalDateTime.now();
        LocalDate returnDate = issueTime.toLocalDate().plusDays(daysToReturn);
        
        book.issueBook(studentId, issueTime, returnDate);
        System.out.println("\nBook Issued Successfully!");
        System.out.println("> Book: " + book.getTitle() + " (ID: " + bookId + ")");
        System.out.println("> Issued To: " + student.getName() + " (ID: " + studentId + ")");
        System.out.println("> Date & Time of Issue: " + issueTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("> **Expected Return Date: " + returnDate + "**");
    }

    public void returnBook(int bookId) {
        Books book = books.get(bookId);

        if (book == null) {
            System.out.println("\nReturn Failed: Book ID " + bookId + " not found.");
            return;
        }
        if (!book.isIssued()) {
            System.out.println("\nReturn Failed: Book '" + book.getTitle() + "' is not currently issued.");
            return;
        }

        LocalDate expectedReturnDate = book.getReturnDate();
        LocalDate today = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(expectedReturnDate, today);
        
        String studentId = book.getIssuedToStudentId();
        Student student = students.get(studentId);
        double calculatedFine = 0.0;
        
        System.out.println("Book Returned Successfully!");
        System.out.println("> Book: " + book.getTitle());

        if (daysOverdue > 0) {
            calculatedFine = daysOverdue * fineRatePerDay;
            student.addFine(calculatedFine);
            System.out.println("\nOVERDUE by " + daysOverdue + " days.");
            System.out.println("Calculated Fine Applied: ₹" + String.format("%.2f", calculatedFine) + " (Total Fine: ₹" + String.format("%.2f", student.getFineAmount()) + ")");
        } else {
             System.out.println("\nReturned on time. No fine.");
        }

        book.returnBook();
    }
    
    // --- Display Reports ---
    public void displayAvailableBooks() {
        System.out.println("\n--- Available Books ---");
        boolean found = false;
        for (Books b : books.values()) {
            if (!b.isIssued()) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("No books are currently available.");
    }
    
    public void displayIssuedBooks() {
        System.out.println("\n--- Issued Books ---");
        boolean found = false;
        for (Books b : books.values()) {
            if (b.isIssued()) {
                System.out.println("ID: " + b.getBookId() + ", Title: " + b.getTitle() + 
                                   ", Issued To SID: " + b.getIssuedToStudentId() + 
                                   ", Issued On: " + b.getIssueDateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                                   ", **Return By: " + b.getReturnDate() + "**");
                found = true;
            }
        }
        if (!found) System.out.println("No books are currently issued.");
    }
}
