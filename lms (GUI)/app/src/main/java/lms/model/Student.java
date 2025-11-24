package lms.model;

public class Student {
    private String id;
    private String name;
    private double fineAmount;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.fineAmount = 0.0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getFineAmount() { return fineAmount; }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void addFine(double amount) {
        this.fineAmount += amount;
    }
}