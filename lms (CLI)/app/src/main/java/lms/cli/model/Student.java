package lms.cli.model;

public class Student {
    private String studentId;
    private String name;
    private double fineAmount;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.fineAmount = 0.0;
    }

    // --- Getters ---
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public double getFineAmount() { return fineAmount; }

    // --- Setters ---
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void addFine(double amount) {
        this.fineAmount += amount;
    }

    @Override
    public String toString() {
        return "ID: " + studentId + ", Name: " + name + ", Fine: $" + String.format("%.2f", fineAmount);
    }
}