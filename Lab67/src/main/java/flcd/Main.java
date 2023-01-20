package flcd;

import flcd.model.Grammar;
import flcd.model.JavaScanner;
import flcd.model.LLParser;
import flcd.model.ParserOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    public static List<String> readSequence(String filename) {
        List<String> sequence = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();
            while (line != null) {
                var tokens = List.of(line.split(" "));
                sequence.addAll(tokens);
                line = reader.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return sequence;
    }

    public static List<String> readPIFSequence(String filename) {
        try {
            List<String> tokens = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null){
                List<String> splitTokens = Arrays.asList(line.split(" "));
                tokens.add(splitTokens.get(0).strip());
                line = reader.readLine();
            }
            reader.close();
            return tokens;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) throws IOException {
//        JavaScanner javaScanner = new JavaScanner();
//        String pathP1 = "src/main/java/flcd/io/p1.txt";
//
//        try {
//            javaScanner.scan(pathP1);
//            System.out.println("Program is lexically correct.");
//        } catch (Exception e) {
//            System.out.println("Program is not lexically correct.\n" + e);
//        }
//
//        javaScanner.writeToFile("src/main/java/flcd/io/ST.out", "src/main/java/flcd/io/PIF.out");

        Grammar grammar = Grammar.provideGrammar("src/main/java/flcd/io/g2.txt");

//        List<String> sequence = readSequence("src/main/java/flcd/io/seq.txt");
        List<String> sequence = readPIFSequence("src/main/java/flcd/io/PIF.out");

        List<Integer> transitions = LLParser.parseSequence(grammar, sequence);
        String transitionsStr = transitions.stream().map(Object::toString).reduce("", (str, e) -> str + e + " ");
        System.out.println("Transitions: " + transitionsStr);

//        ParserOutput parserOutput = new ParserOutput(grammar, sequence, "src/main/java/flcd/out/out1.txt");
        ParserOutput parserOutput = new ParserOutput(grammar, sequence, "src/main/java/flcd/out/out2.txt");
        parserOutput.printTree();
    }
}