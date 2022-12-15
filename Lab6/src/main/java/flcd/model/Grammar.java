package flcd.model;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class Grammar {
    private final String start;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Map<String, Set<List<String>>> productions;
    private final boolean isContextFree;

    public static Grammar provideGrammar(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        var nonTerminals = new HashSet<String>();
        var terminals = new HashSet<String>();
        var productions = new HashMap<String, Set<List<String>>>();
        var isCFG = true;
        var line = bufferedReader.readLine();
        String start = null;

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
            if (start == null) start = nonTerminal;

            var currentProductions = tokens[1].split("\\|");
            for (var production: currentProductions) {
                productions.computeIfAbsent(nonTerminal, k -> new HashSet<>());
                var rhsTokens = Stream.of(production.trim().split(" "))
                        .map(String::trim)
                        .toList();
                var updatedTokens = new ArrayList<String>();

                for (var token: rhsTokens) {
                    var length = token.length();
                    if (length >= 2 && token.charAt(0) == '"' && token.charAt(length - 1) == '"') {
                        var trimmedTerminal = token.substring(1, length - 1);
                        terminals.add(trimmedTerminal);
                        updatedTokens.add(trimmedTerminal);
                    }
                    else {
                        updatedTokens.add(token);
                    }
                }

                productions.get(nonTerminal).add(updatedTokens);
            }

            line = bufferedReader.readLine();
        }

        return new Grammar(start, nonTerminals, terminals, productions, isCFG);
    }

    private Grammar(String start, Set<String> nonTerminals, Set<String> terminals, Map<String, Set<List<String>>> productions,
                    boolean isCFG) {
        this.start = start;
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.isContextFree = isCFG;
    }

    public Set<List<String>> getProductions(String nonTerminal) {
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

    public Map<String, Set<List<String>>> getProductions() {
        return productions;
    }

    public boolean isContextFree() {
        return isContextFree;
    }
}