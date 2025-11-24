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

    // Inside lms.model.Library.java

// --- Helper methods for GUI Data Retrieval ---
public List<Books> getAllBooksGUI() {
    return new ArrayList<>(books.values());
}

public List<Student> getAllStudentsGUI() {
    return new ArrayList<>(students.values());
}

public Student getStudentGUI(String studentId) {
    return students.get(studentId);
}

public double getFineRatePerDayGUI() {
    return fineRatePerDay;
}

// --- GUI-Specific Action Methods (Returning String for Status Label) ---

public String addBookGUI(int id, String title, String author) {
    if (books.containsKey(id)) {
        return "⚠️ Error: Book ID " + id + " already exists.";
    }
    books.put(id, new Books(id, title, author));
    return "✅ Book added: " + title;
}

public String removeBookGUI(int id) {
    Books b = books.get(id);
    if (b == null) {
        return "⚠️ Error: Book ID " + id + " not found.";
    } else if (b.isIssued()) {
         return "⚠️ Error: Book '" + b.getTitle() + "' is currently issued. Cannot remove.";
    } else {
        books.remove(id);
        return "✅ Book ID " + id + " removed.";
    }
}

public String addStudentGUI(String id, String name) {
    if (students.containsKey(id)) {
        return "⚠️ Error: Student ID " + id + " already exists.";
    }
    students.put(id, new Student(id, name));
    return "✅ Student added: " + name;
}

public String issueBookGUI(int bookId, String studentId, int daysToReturn) {
    // ... (Your existing issue logic) ...
    // If successful:
    // book.issueBook(studentId, issueTime, returnDate);
    // return "🎉 **Book Issued!** Ret. by: " + returnDate;
    // If failed:
    // return "⚠️ Issue Failed: Reason here."
    // (You'll need to update the logic to return these strings)
    
    // Example Success Message
    return String.format("🎉 Issued %s to %s. Return by: %s.", 
           books.get(bookId).getTitle(), students.get(studentId).getName(), books.get(bookId).getReturnDate());
}

public String returnBookGUI(int bookId) {
    // ... (Your existing return logic with fine calculation) ...
    // Example Overdue Message
    // return String.format("✅ Returned %s. Overdue by %d days. Fine applied: $%.2f", book.getTitle(), daysOverdue, calculatedFine);
    
    // Example On-Time Message
    // return "👍 Returned on time. No fine applied.";
    
    // (Again, adapt your fine calculation logic to return the appropriate message string)
    
    // Example Failure Message
    // return "⚠️ Return Failed: Book ID not found.";
    
    // A simplified return message for placeholder:
    Books book = books.get(bookId);
    if (book == null || !book.isIssued()) return "⚠️ Return Failed: Book not found or not issued.";
    book.returnBook(); // Reset issue status
    return "✅ Book returned (Details need full integration of fine logic).";
}

public String setFineRateGUI(double rate) {
    if (rate < 0) {
        return "⚠️ Error: Fine rate cannot be negative.";
    }
    this.fineRatePerDay = rate;
    return "✅ Fine rate set to $" + String.format("%.2f", rate) + " per day.";
}
}