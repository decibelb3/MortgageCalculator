/**
 * A program that calculates mortgage payments and provides financial recommendations.
 *
 * @author Shannon Burns and Marina Potapova
 * @version 11/25/2004
 */
import java.util.Scanner;

public class MortgageCalculator {

    /**
     * The main method to run the mortgage calculator program. It collects user input,
     * calculates mortgage details, and provides financial recommendations.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Gather user information
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Gather mortgage details
        System.out.print("Enter loan amount: ");
        double loanAmount = scanner.nextDouble();

        System.out.print("Enter annual interest rate (as a percentage, e.g., 5 for 5%): ");
        double annualInterestRate = scanner.nextDouble();
        double monthlyInterestRate = (annualInterestRate / 100) / 12;

        System.out.print("Enter loan term (in years): ");
        int loanTermYears = scanner.nextInt();
        int loanTermMonths = loanTermYears * 12;

        System.out.print("Enter down payment amount: ");
        double downPayment = scanner.nextDouble();

        // Calculate loan after down payment
        double loanAfterDownPayment = loanAmount - downPayment;

        // Calculate the monthly mortgage payment
        double monthlyPayment = calculateMonthlyPayment(loanAfterDownPayment, monthlyInterestRate, loanTermMonths);

        // Display monthly payment result
        System.out.printf("\nHello %s, your monthly mortgage payment is: $%.2f\n", name, monthlyPayment);

        // Total cost of the loan
        double totalCostOfLoan = monthlyPayment * loanTermMonths;
        System.out.printf("Total cost of the loan over %d years: $%.2f\n", loanTermYears, totalCostOfLoan);

        // Offer personalized financial recommendations
        System.out.print("Enter your monthly salary: ");
        double salary = scanner.nextDouble();

        System.out.print("Enter your monthly expenses: ");
        double expenses = scanner.nextDouble();

        provideFinancialRecommendations(salary, expenses, monthlyPayment);
        
        scanner.close();
    }

    /**
     * Calculates the monthly mortgage payment using the standard mortgage formula.
     *
     * @param loanAmount the loan amount after the down payment
     * @param monthlyInterestRate the monthly interest rate as a decimal
     * @param loanTermMonths the term of the loan in months
     * @return the calculated monthly payment
     */
    public static double calculateMonthlyPayment(double loanAmount, double monthlyInterestRate, int loanTermMonths) {
        if (monthlyInterestRate == 0) {  // If the interest rate is 0%
            return loanAmount / loanTermMonths;
        } else {
            return loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermMonths) /
                   (Math.pow(1 + monthlyInterestRate, loanTermMonths) - 1);
        }
    }

    /**
     * Provides financial recommendations based on the user's salary, expenses, and mortgage payment.
     * Includes disposable income calculation and savings suggestions.
     *
     * @param salary the user's monthly salary
     * @param expenses the user's monthly expenses
     * @param monthlyPayment the calculated monthly mortgage payment
     */
    public static void provideFinancialRecommendations(double salary, double expenses, double monthlyPayment) {
        double disposableIncome = salary - expenses;

        System.out.printf("\nYour disposable income each month: $%.2f\n", disposableIncome);

        if (disposableIncome >= monthlyPayment) {
            System.out.println("You can comfortably afford the monthly mortgage payment.");
        } else {
            System.out.printf("You need to save an additional $%.2f each month to afford the mortgage payment.\n", monthlyPayment - disposableIncome);
        }

        // Suggest savings goals for home buying
        double recommendedSavings = salary * 0.2;  // Assuming saving 20% of the salary is recommended
        System.out.printf("It's recommended that you save at least $%.2f per month.\n", recommendedSavings);

        if (recommendedSavings < monthlyPayment) {
            System.out.println("Consider adjusting your financial goals or exploring homes in a lower price range.");
        } else {
            System.out.println("You are on track to afford a home within your financial reach!");
        }
    }
}
