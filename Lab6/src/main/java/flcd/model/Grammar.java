package flcd.model;

import java.io.*;
import java.util.*;

public class Grammar {
    private final String start;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Map<String, Set<String>> productions;
    private final boolean isCFG;

    public static Grammar provideGrammar(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        var nonTerminals = new HashSet<String>();
        var terminals = new HashSet<String>();
        var productions = new HashMap<String, Set<String>>();
        var isCFG = true;
        var line = bufferedReader.readLine();

        if (line == null) {
            throw new RuntimeException("The format of the grammar file is not valid!");
        }

        while (line != null) {
            var tokens = line.split(" = ");

            if (tokens.length != 2) {
                throw new RuntimeException("The format of the grammar file is not valid!");
            }

            var nonTerminal = tokens[0].trim();
            if (nonTerminal.split(" ").length != 1) isCFG = false;
            nonTerminals.add(nonTerminal);

            var currentProductions = tokens[1].split("\\|");
            for (var production: currentProductions) {
                productions.computeIfAbsent(nonTerminal, k -> new HashSet<>());
                productions.get(nonTerminal).add(production.trim());

                var elements = production.split(" ");
                for (var element: elements) {
                    var length = element.length();
                    if (length >= 3 && element.charAt(0) == '"' && element.charAt(length - 1) == '"') {
                        terminals.add(element.trim());
                    }
                }
            }

            line = bufferedReader.readLine();
        }

        return new Grammar(line, nonTerminals, terminals, productions, isCFG);
    }

    private Grammar(String start, Set<String> nonTerminals, Set<String> terminals, Map<String, Set<String>> productions,
                    boolean isCFG) {
        this.start = start;
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.isCFG = isCFG;
    }

    public Set<String> getProductions(String nonTerminal) {
        return productions.get(nonTerminal);
    }

    public String getStart() {
        return start;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Map<String, Set<String>> getProductions() {
        return productions;
    }

    public boolean isCFG() {
        return isCFG;
    }
}