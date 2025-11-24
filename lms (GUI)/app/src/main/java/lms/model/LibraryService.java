package lms.model;
// WORKTODO: Convert the SQL Queries To ORM-Based.
import lms.connector.db.SQLConnector;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private double finePerDay = 2.0;

    //FETCHES DATA FROM DB

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id ASC";

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"));
                boolean isIssued = rs.getBoolean("is_issued");
                
                if (isIssued) {
                    // We need to manually inject the issue details into the object
                    // In a real app, the Book constructor might handle this, but we'll use the setter method we created earlier
                    // Note: We need to modify Book.java slightly to allow setting these directly, 
                    // or just use the issue() method with a hack. 
                    // Let's assume we add specific setters to Book.java or use reflection.
                    // For simplicity, we will reconstruct the state:
                    b.restoreIssueState(
                        rs.getString("issued_to"),
                        rs.getTimestamp("issue_date") != null ? rs.getTimestamp("issue_date").toLocalDateTime() : null,
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                    );
                }
                books.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id ASC";

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(rs.getString("id"), rs.getString("name"));
                s.setFineAmount(rs.getDouble("fine_amount")); // Need to add this setter in Student.java
                students.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return students;
    }

    // --- ACTIONS ---

    public String addBook(int id, String title, String author) {
        String sql = "INSERT INTO books (id, title, author) VALUES (?, ?, ?)";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.executeUpdate();
            return "Success: Book added to DB.";
            
        } catch (SQLException e) {
            return "Error: " + e.getMessage(); // Likely duplicate ID
        }
    }

    public String removeBook(int id) {
        // First check if issued
        if (isBookIssued(id)) return "Error: Cannot remove issued book.";

        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? "Success: Book removed." : "Error: Book ID not found.";
            
        } catch (SQLException e) { return "Error: DB Error."; }
    }

    public String addStudent(String id, String name) {
        String sql = "INSERT INTO students (id, name) VALUES (?, ?)";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            return "Success: Student added.";
            
        } catch (SQLException e) { return "Error: Student ID likely exists."; }
    }

    public String issueBook(int bookId, String studentId, int days) {
        // Checks
        if (!bookExists(bookId)) return "Error: Book not found.";
        if (isBookIssued(bookId)) return "Error: Book already issued.";
        if (!studentExists(studentId)) return "Error: Student not found.";

        String sql = "UPDATE books SET is_issued = TRUE, issued_to = ?, issue_date = ?, return_date = ? WHERE id = ?";
        
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setDate(3, Date.valueOf(LocalDate.now().plusDays(days)));
            pstmt.setInt(4, bookId);
            
            pstmt.executeUpdate();
            return "Success: Book Issued.";
            
        } catch (SQLException e) { return "Error: " + e.getMessage(); }
    }

    public String returnBook(int bookId) {
        if (!isBookIssued(bookId)) return "Error: Book is not issued.";

        String fetchSql = "SELECT issued_to, return_date FROM books WHERE id = ?";
        String updateBookSql = "UPDATE books SET is_issued = FALSE, issued_to = NULL, issue_date = NULL, return_date = NULL WHERE id = ?";
        String updateFineSql = "UPDATE students SET fine_amount = fine_amount + ? WHERE id = ?";

        try (Connection conn = SQLConnector.getConnection()) {
            conn.setAutoCommit(false); // Start Transaction

            // 1. Get Details
            String studentId = null;
            LocalDate returnDate = null;
            
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setInt(1, bookId);
                ResultSet rs = fetchStmt.executeQuery();
                if (rs.next()) {
                    studentId = rs.getString("issued_to");
                    returnDate = rs.getDate("return_date").toLocalDate();
                }
            }

            // 2. Calculate Fine
            double fine = 0.0;
            long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
            if (overdueDays > 0) {
                fine = overdueDays * finePerDay;
            }

            // 3. Update Fine if needed
            if (fine > 0) {
                try (PreparedStatement fineStmt = conn.prepareStatement(updateFineSql)) {
                    fineStmt.setDouble(1, fine);
                    fineStmt.setString(2, studentId);
                    fineStmt.executeUpdate();
                }
            }

            // 4. Reset Book
            try (PreparedStatement bookStmt = conn.prepareStatement(updateBookSql)) {
                bookStmt.setInt(1, bookId);
                bookStmt.executeUpdate();
            }

            conn.commit(); // Commit Transaction
            
            String msg = "Returned.";
            if (fine > 0) msg += String.format(" Late! Fine applied: $%.2f", fine);
            return msg;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Transaction failed.";
        }
    }

    // --- HELPER METHODS ---

    private boolean bookExists(int id) {
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement p = conn.prepareStatement("SELECT 1 FROM books WHERE id=?")) {
            p.setInt(1, id);
            return p.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    private boolean isBookIssued(int id) {
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement p = conn.prepareStatement("SELECT is_issued FROM books WHERE id=?")) {
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getBoolean("is_issued");
        } catch (SQLException e) { return false; }
    }

    private boolean studentExists(String id) {
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement p = conn.prepareStatement("SELECT 1 FROM students WHERE id=?")) {
            p.setString(1, id);
            return p.executeQuery().next();
        } catch (SQLException e) { return false; }
    }
}