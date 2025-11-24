package lms.cli;

import java.util.Scanner;

import lms.cli.model.Library;

public class App {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Sample Data for testing
        library.addBook(101, "The Great Gatsby", "F. Scott Fitzgerald");
        library.addBook(102, "1984", "George Orwell");
        library.addStudent("S001", "Alice Smith");
        library.addStudent("S002", "Bob Johnson");
        
        runCLI();
    }

    private static void displayMenu() {
        System.out.println("\n=== Library Management System Menu ===");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Add Student");
        System.out.println("4. Issue Book");
        System.out.println("5. Return Book");
        System.out.println("6. Set Daily Fine Rate");
        System.out.println("7. Get Student Fine Details");
        System.out.println("8. Display Available Books");
        System.out.println("9. Display Issued Books");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void runCLI() {
        int choice;
        do {
            displayMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1: addBookHandler(); break;
                    case 2: removeBookHandler(); break;
                    case 3: addStudentHandler(); break;
                    case 4: issueBookHandler(); break;
                    case 5: returnBookHandler(); break;
                    case 6: setFineRateHandler(); break;
                    case 7: getFineDetailsHandler(); break;
                    case 8: getAvailableBooks(); break;
                    case 9: getIssuedBooks(); break;
                    case 0: System.out.println("Exiting Library System!"); break;
                    default: System.out.println("Invalid choice. Please try again.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for the choice.");
                scanner.nextLine(); // Clear the buffer
                choice = -1;
            }
        } while (choice != 0);
        scanner.close();
    }

    // --- Handlers for Menu Options ---

    private static void addBookHandler() {
        System.out.print("Enter Book ID (integer): ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        library.addBook(id, title, author);
    }
    
    private static void removeBookHandler() {
        System.out.print("Enter Book ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        library.removeBook(id);
    }
    
    private static void addStudentHandler() {
        System.out.print("Enter Student ID (e.g., S003): ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        library.addStudent(id, name);
    }
    
    private static void issueBookHandler() {
        System.out.print("Enter Book ID to issue: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Return Period (Days): ");
        int days = scanner.nextInt();
        scanner.nextLine();
        library.issueBook(bookId, studentId, days);
    }

    private static void returnBookHandler() {
        System.out.print("Enter Book ID to return: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
        library.returnBook(bookId);
    }

    private static void setFineRateHandler() {
        System.out.print("Enter New Daily Fine Rate (e.g., 2.50): ₹");
        double rate = scanner.nextDouble();
        scanner.nextLine();
        library.setFineRate(rate);
    }

    private static void getFineDetailsHandler() {
        System.out.print("Enter Student ID to check fine: ");
        String studentId = scanner.nextLine();
        library.getFineDetails(studentId);
    }
    private static void getAvailableBooks() {
        library.displayAvailableBooks();
    }
    private static void getIssuedBooks() {
        library.displayIssuedBooks();
    }
}