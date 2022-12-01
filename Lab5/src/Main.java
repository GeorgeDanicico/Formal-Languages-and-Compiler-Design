import model.FiniteAutomaton;
import model.Grammar;
import model.JavaScanner;
import model.SymbolTable;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void showMenu() {
        System.out.println("Menu: ");
        System.out.println("1. Print the set of terminals.");
        System.out.println("2. Print the set of nonterminals.");
        System.out.println("3. Print the set of productions.");
        System.out.println("4. Print the produtions for a given nonterminal.");
        System.out.println("5. CFG Check");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) throws IOException {
        Grammar grammar = Grammar.provideGrammar("src/io/bnf-syntax.txt");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            System.out.println(">> ");
            int command = Integer.parseInt(scanner.nextLine());

            if (command == 0) break;

            switch (command) {
                case 1:
                    System.out.println("The set of terminals is:\n" + grammar.getTerminals());
                    break;
                case 2:
                    System.out.println("The set of nonterminals is:\n" + grammar.getNonTerminals());
                    break;
                case 3:
                    System.out.println("The set of productions is:\n" + grammar.getProductions());
                    break;
                case 4:
                    System.out.println("Insert a nonterminal>>");
                    String nonTerminal = scanner.nextLine();
                    System.out.println("The set of production for " + nonTerminal + " is: " +
                            grammar.getProductions(nonTerminal));
                    break;
                case 5:
                    System.out.println("Is this grammar a CFG? " + grammar.isCFG() +
                            "\n");
                    break;


            }
        }

    }
}