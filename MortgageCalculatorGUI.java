/**
 * A program that calculates mortgage payments and provides financial recommendations.
 *
 * @author Shannon Burns and Marina Potapova
 * @version 11/25/2004
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * A GUI-based mortgage calculator that allows users to input financial details and calculates 
 * their monthly mortgage payments along with recommendations.
 */
public class MortgageCalculatorGUI extends JFrame {
    private JTextField nameField;
    private JFormattedTextField salaryField, expensesField, loanAmountField, downPaymentField, interestRateField, loanTermField;
    private JTextArea resultArea;

    /**
     * Constructs the MortgageCalculatorGUI and initializes the components.
     */
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

    /**
     * Resets all input fields and clears the result area.
     */
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

    /**
     * Listener class for the Calculate button. Handles user input, calculates the mortgage payment, 
     * and generates recommendations.
     */
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

    /**
     * Calculates the monthly mortgage payment using the formula.
     *
     * @param loanAmount the loan amount after down payment
     * @param monthlyInterestRate the monthly interest rate as a decimal
     * @param loanTermMonths the loan term in months
     * @return the calculated monthly payment
     */
    public static double calculateMonthlyPayment(double loanAmount, double monthlyInterestRate, int loanTermMonths) {
        return (loanAmount * monthlyInterestRate) /
                (1 - Math.pow(1 + monthlyInterestRate, -loanTermMonths));
    }

    /**
     * Generates financial recommendations based on the user's financial details and the mortgage payment.
     *
     * @param user the user details (name, salary, expenses)
     * @param monthlyPayment the calculated monthly payment
     * @return a string containing the recommendation
     */
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

    /**
     * A class representing loan details for the mortgage calculation.
     */
    public class LoanDetails {
        private double loanAmount;
        private double downPayment;
        private double interestRate;
        private int loanTermYears;

        /**
         * Constructs a LoanDetails object with the specified parameters.
         *
         * @param loanAmount the total loan amount
         * @param downPayment the down payment amount
         * @param interestRate the annual interest rate as a percentage
         * @param loanTermYears the loan term in years
         */
        public LoanDetails(double loanAmount, double downPayment, double interestRate, int loanTermYears) {
            this.loanAmount = loanAmount;
            this.downPayment = downPayment;
            this.interestRate = interestRate;
            this.loanTermYears = loanTermYears;
        }

        /**
         * Calculates the monthly interest rate from the annual interest rate.
         *
         * @return the monthly interest rate as a decimal
         */
        public double getMonthlyInterestRate() {
            return (interestRate / 100) / 12;
        }

        /**
         * Calculates the loan amount after subtracting the down payment.
         *
         * @return the remaining loan amount after down payment
         */
        public double calculateLoanAfterDownPayment() {
            return loanAmount - downPayment;
        }

        /**
         * Converts the loan term from years to months.
         *
         * @return the loan term in months
         */
        public int getLoanTermMonths() {
            return loanTermYears * 12;
        }
    }

    /**
     * A class representing the user and their financial details.
     */
    public class User {
        private String name;
        private double salary;
        private double expenses;

        /**
         * Constructs a User object with the specified parameters.
         *
         * @param name the name of the user
         * @param salary the monthly salary of the user
         * @param expenses the monthly expenses of the user
         */
        public User(String name, double salary, double expenses) {
            this.name = name;
            this.salary = salary;
            this.expenses = expenses;
        }

        /**
         * Retrieves the user's monthly salary.
         *
         * @return the salary
         */
        public double getSalary() {
            return salary;
        }

        /**
         * Retrieves the user's monthly expenses.
         *
         * @return the expenses
         */
        public double getExpenses() {
            return expenses;
        }
    }

    /**
     * Main method to launch the Mortgage Calculator GUI.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MortgageCalculatorGUI calculatorGUI = new MortgageCalculatorGUI();
            calculatorGUI.setVisible(true);
            calculatorGUI.setLocationRelativeTo(null);
        });
    }
}
