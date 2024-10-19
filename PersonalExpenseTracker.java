import java.io.*;
import java.util.*;

class Expense {
    private String description;
    private double amount;
    private String category;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f (%s)", description, amount, category);
    }
}

class ExpenseManager {
    private List<Expense> expenses;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpenses();
    }

    public void addExpense(String description, double amount, String category) {
        expenses.add(new Expense(description, amount, category));
        saveExpenses();
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, expenses.get(i));
        }
    }

    public void deleteExpense(int index) {
        if (index < 0 || index >= expenses.size()) {
            System.out.println("Invalid index.");
        } else {
            expenses.remove(index);
            System.out.println("Expense deleted.");
            saveExpenses();
        }
    }

    public void summarizeExpenses() {
        Map<String, Double> summary = new HashMap<>();
        double total = 0;

        for (Expense expense : expenses) {
            summary.put(expense.getCategory(), summary.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
            total += expense.getAmount();
        }

        System.out.println("Expense Summary:");
        summary.forEach((category, amount) -> System.out.printf("%s: $%.2f%n", category, amount));
        System.out.printf("Total Spending: $%.2f%n", total);
    }

    private void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("expenses.dat"))) {
            expenses = (List<Expense>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    private void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
}

public class PersonalExpenseTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Summarize Expenses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    manager.addExpense(description, amount, category);
                    break;
                case 2:
                    manager.viewExpenses();
                    break;
                case 3:
                    System.out.print("Enter expense index to delete: ");
                    int index = scanner.nextInt() - 1;  // Adjust for zero-based index
                    manager.deleteExpense(index);
                    break;
                case 4:
                    manager.summarizeExpenses();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
