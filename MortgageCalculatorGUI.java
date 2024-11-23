import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class MortgageCalculatorGUI extends JFrame {
    private JTextField nameField;
    private JFormattedTextField salaryField, expensesField, loanAmountField, downPaymentField, interestRateField, loanTermField;
    private JTextArea resultArea;

    public MortgageCalculatorGUI() {
        // Set up the main frame
        setTitle("Mortgage Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(11, 2)); 

        // Number format for currency fields
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        // User Information
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Monthly Salary:"));
        salaryField = new JFormattedTextField(numberFormat);
        add(salaryField);

        add(new JLabel("Monthly Expenses:"));
        expensesField = new JFormattedTextField(numberFormat);
        add(expensesField);

        // Loan Details
        add(new JLabel("Loan Amount:"));
        loanAmountField = new JFormattedTextField(numberFormat);
        add(loanAmountField);

        add(new JLabel("Down Payment:"));
        downPaymentField = new JFormattedTextField(numberFormat);
        add(downPaymentField);

        add(new JLabel("Annual Interest Rate (%):"));
        interestRateField = new JFormattedTextField(numberFormat);
        add(interestRateField);

        add(new JLabel("Loan Term (years):"));
        loanTermField = new JFormattedTextField(numberFormat);
        add(loanTermField);

        // Result Display
        resultArea = new JTextArea(3, 20);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(new JLabel("Monthly Payment & Recommendation:"));
        add(scrollPane);

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new CalculateButtonListener());
        add(calculateButton);

        // Reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFields());
        add(resetButton);
    }

    // Method to reset all fields
    private void resetFields() {
        nameField.setText("");
        salaryField.setValue(null);
        expensesField.setValue(null);
        loanAmountField.setValue(null);
        downPaymentField.setValue(null);
        interestRateField.setValue(null);
        loanTermField.setValue(null);
        resultArea.setText("");
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Retrieve user input
                String name = nameField.getText();
                double salary = ((Number) salaryField.getValue()).doubleValue();
                double expenses = ((Number) expensesField.getValue()).doubleValue();
                double loanAmount = ((Number) loanAmountField.getValue()).doubleValue();
                double downPayment = ((Number) downPaymentField.getValue()).doubleValue();
                double interestRate = ((Number) interestRateField.getValue()).doubleValue();
                int loanTermYears = ((Number) loanTermField.getValue()).intValue();

                // Create User and LoanDetails instances
                User user = new User(name, salary, expenses);
                LoanDetails loanDetails = new LoanDetails(loanAmount, downPayment, interestRate, loanTermYears);

                // Calculate monthly payment
                double monthlyInterestRate = loanDetails.getMonthlyInterestRate();
                double loanAmountAfterDownPayment = loanDetails.calculateLoanAfterDownPayment();
                int loanTermMonths = loanDetails.getLoanTermMonths();
                double monthlyPayment = calculateMonthlyPayment(loanAmountAfterDownPayment, monthlyInterestRate, loanTermMonths);

                // Display results
                resultArea.setText(String.format("Monthly Payment: $%.2f\n%s", monthlyPayment,
                        generateRecommendations(user, monthlyPayment)));
            } catch (NumberFormatException | NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values.");
            }
        }
    }

    // Method to calculate the monthly payment
    public static double calculateMonthlyPayment(double loanAmount, double monthlyInterestRate, int loanTermMonths) {
        return (loanAmount * monthlyInterestRate) /
                (1 - Math.pow(1 + monthlyInterestRate, -loanTermMonths));
    }

    // Method to generate recommendations
    public static String generateRecommendations(User user, double monthlyPayment) {
        double disposableIncome = user.getSalary() - user.getExpenses();
        if (monthlyPayment < disposableIncome * 0.3) {
            return "Your mortgage is affordable based on your financial details.";
        } else if (monthlyPayment < disposableIncome * 0.5) {
            return "Your mortgage payments are manageable but consider lowering other expenses.";
        } else {
            return "Your mortgage payments may be too high; consider a larger down payment or a lower loan amount.";
        }
    }

    // Inner LoanDetails class
    public class LoanDetails {
        private double loanAmount;
        private double downPayment;
        private double interestRate;
        private int loanTermYears;

        // Constructor
        public LoanDetails(double loanAmount, double downPayment, double interestRate, int loanTermYears) {
            this.loanAmount = loanAmount;
            this.downPayment = downPayment;
            this.interestRate = interestRate;
            this.loanTermYears = loanTermYears;
        }

        // Method to get the monthly interest rate
        public double getMonthlyInterestRate() {
            return (interestRate / 100) / 12;
        }

        // Method to calculate the loan amount after down payment
        public double calculateLoanAfterDownPayment() {
            return loanAmount - downPayment;
        }

        // Method to get loan term in months
        public int getLoanTermMonths() {
            return loanTermYears * 12;
        }
    }

    // Inner User class
    public class User {
        private String name;
        private double salary;
        private double expenses;

        // Constructor
        public User(String name, double salary, double expenses) {
            this.name = name;
            this.salary = salary;
            this.expenses = expenses;
        }

        // Getters
        public double getSalary() {
            return salary;
        }

        public double getExpenses() {
            return expenses;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MortgageCalculatorGUI calculatorGUI = new MortgageCalculatorGUI();
            calculatorGUI.setVisible(true);
            calculatorGUI.setLocationRelativeTo(null);
        });
    }
}