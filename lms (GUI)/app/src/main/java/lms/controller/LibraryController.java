package lms.controller;
import lms.model.Book;
import lms.model.LibraryService;
import lms.model.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LibraryController {

    private final LibraryService library = new LibraryService();

    // UI Components linked via fx:id
    @FXML private Label statusLabel;
    
    // Book Tab Inputs
    @FXML private TextField bookIdField, titleField, authorField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle, colAuthor;
    @FXML private TableColumn<Book, String> colStatus, colIssuedTo, colReturnDate;

    // Issue/Return Inputs
    @FXML private TextField issueBookIdField, issueStudentIdField, returnBookIdField;
    @FXML private Spinner<Integer> daysSpinner;

    // Student Tab Inputs
    @FXML private TextField studentIdField, studentNameField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colStudentId, colStudentName;
    @FXML private TableColumn<Student, Double> colFine;

    @FXML
    public void initialize() {
        // 1. Setup Book Columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        // Custom formatting for Status
        colStatus.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getIsIssued() ? "Issued" : "Available"));
        
        colIssuedTo.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getIssuedToStudentId() == null ? "-" : cell.getValue().getIssuedToStudentId()));
        
        colReturnDate.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getReturnDate() == null ? "-" : cell.getValue().getReturnDate().toString()));

        // 2. Setup Student Columns
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));

        // 3. Setup Spinner
        daysSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 7));

        refreshTables();
    }

    private void refreshTables() {
        bookTable.setItems(FXCollections.observableArrayList(library.getAllBooks()));
        studentTable.setItems(FXCollections.observableArrayList(library.getAllStudents()));
    }

    private void displayMessage(String msg) {
        statusLabel.setText(msg);
        if (msg.startsWith("Error")) statusLabel.setStyle("-fx-text-fill: red;");
        else statusLabel.setStyle("-fx-text-fill: green;");
    }

    // --- Event Handlers ---

    @FXML
    public void handleAddBook() {
        try {
            int id = Integer.parseInt(bookIdField.getText());
            String res = library.addBook(id, titleField.getText(), authorField.getText());
            displayMessage(res);
            refreshTables();
        } catch (NumberFormatException e) { displayMessage("Error: Invalid ID format"); }
    }

    @FXML
    public void handleRemoveBook() {
        try {
            int id = Integer.parseInt(bookIdField.getText());
            String res = library.removeBook(id);
            displayMessage(res);
            refreshTables();
        } catch (NumberFormatException e) { displayMessage("Error: Invalid ID format"); }
    }

    @FXML
    public void handleAddStudent() {
        String res = library.addStudent(studentIdField.getText(), studentNameField.getText());
        displayMessage(res);
        refreshTables();
    }

    @FXML
    public void handleIssue() {
        try {
            int bId = Integer.parseInt(issueBookIdField.getText());
            String sId = issueStudentIdField.getText();
            int days = daysSpinner.getValue();
            
            String res = library.issueBook(bId, sId, days);
            displayMessage(res);
            refreshTables();
        } catch (NumberFormatException e) { displayMessage("Error: Invalid ID"); }
    }

    @FXML
    public void handleReturn() {
        try {
            int bId = Integer.parseInt(returnBookIdField.getText());
            String res = library.returnBook(bId);
            displayMessage(res);
            refreshTables();
        } catch (NumberFormatException e) { displayMessage("Error: Invalid ID"); }
    }
}