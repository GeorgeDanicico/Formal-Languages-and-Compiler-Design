import model.FiniteAutomaton;
import model.JavaScanner;
import model.SymbolTable;

import java.util.Scanner;

public class Main {
    public static void showMenu() {
        System.out.println("Menu: ");
        System.out.println("1. Print the set of states.");
        System.out.println("2. Print the alphabet");
        System.out.println("3. Print the transitions.");
        System.out.println("4. Print the initial state.");
        System.out.println("5. Print the final states.");
        System.out.println("6. Check if sequence is accepted by the FA.");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) {
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton("src/identifier.csv");
        JavaScanner javaScanner = new JavaScanner();

        try {
            javaScanner.scan("src/tests/p1.txt");
            System.out.println("Program is lexically correct.");
        } catch (Exception e) {
            System.out.println("Program is not lexically correct.\n" + e);
        }

        javaScanner.writeToFile("ST.out", "PIF.out");

    }
}