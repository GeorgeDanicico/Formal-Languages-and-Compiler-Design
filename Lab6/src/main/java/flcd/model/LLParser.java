package flcd.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LLParser {
    public static Map<String, Set<String>> computeFirst(Grammar grammar) {
        var first = new HashMap<String, Set<String>>();
        var toAdd = new HashMap<String, Queue<String>>();

        var terminals = grammar.getTerminals();
        var nonTerminals = grammar.getNonTerminals();

        for (var terminal: terminals) {
            first.computeIfAbsent(terminal, k -> new HashSet<>());
            first.get(terminal).add(terminal);
        }

        for (var nonTerminal: nonTerminals) {
            first.computeIfAbsent(nonTerminal, k -> new HashSet<>());
            var nonTerminalProductions = grammar.getProductions(nonTerminal);

            for (var rhsTokens: nonTerminalProductions) {
                if (terminals.contains(rhsTokens.get(0))) {
                    first.get(nonTerminal).add(rhsTokens.get(0));
                }
            }
        }

        var done = false;
        while (done) {
            done = true;

            for (var nonTerminal: nonTerminals) {
                var productions = grammar.getProductions(nonTerminal);

                for (var production: productions) {

                }
            }
        }
        return null;
    }

    private static Set<String> computeConcatenationsOfLengthOne(Map<String, Set<String>> first,
                                                                List<String> rhsTokens) {
        int rhsSize = rhsTokens.size();
        List<String> concatenation = IntStream.of(rhsSize).mapToObj(_value -> (String)null).toList();

//        int index = 0;
//        while (index >= 0 && index < rhsSize) {
//            var currentToken = rhsTokens.get(0);
//            var iterator = first.get(currentToken).iterator();
//
//
//        }

        return null;
    }

    /**
     * This function returns all the productions in which the given non terminal appear in the right hand side.
     * @param nonTerminal -> the given nonterminal
     * @param grammar -> the given grammar
     * @return
     */
    private static Map<String, Set<List<String>>> getNonTerminalProductions(String nonTerminal, Grammar grammar) {
        Map<String, Set<List<String>>> productions = grammar.getProductions();
        Map<String, Set<List<String>>> requiredProductions = new HashMap<>();

        for (Map.Entry<String, Set<List<String>>> entry : productions.entrySet()) {
            // iterate through the production of every nonterminal and check if
            // the given nonterminal appears in the right hand side of the production.
            for (List<String> production : entry.getValue()) {
                if (production.contains(nonTerminal)) {
                    requiredProductions.putIfAbsent(entry.getKey(), new HashSet<>());
                    requiredProductions.get(entry.getKey()).add(production);
                }
            }
        }

        return requiredProductions;
    }

    private static String getFirstItemAfter(List<String> productionRHS, String nonTerminal) {
        // Return the index of the nonTerminal
        int index = productionRHS.indexOf(nonTerminal); // it is known that non terminal is present in productionRHS

        // If there is no item after the given nonterminal, return empty string (epsilon)
        // else return the next item.
        if (index == productionRHS.size() - 1) {
            return "";
        } else {
            return productionRHS.get(index + 1);
        }
    }

    public static Map<String, Set<String>> computeFollow(Grammar grammar, Map<String, Set<String>> firstSet) {
        Map<String, Set<String>> follow = new HashMap<>();
        Set<String> nonTerminals = grammar.getNonTerminals();

        for (String nonTerminal : grammar.getNonTerminals()) {
            follow.put(nonTerminal, new HashSet<>());
        }

        follow.put(grammar.getStart(), new HashSet<>(){{add("");}});
        boolean isFollowChanged = true;

        while (isFollowChanged) {
            isFollowChanged = false;
            Map<String, Set<String>> newFollowColumn = new HashMap<>();
            // iterate through all the nonterminals
            for (String nonTerminal : nonTerminals) {
                // initialize the new follow column
                newFollowColumn.put(nonTerminal, new HashSet<>());
                // get all the productions where the nonterminal appears in the RHS
                var nonTerminalProductions = getNonTerminalProductions(nonTerminal, grammar);
                // iterate through all the valid productions
                for (var validProductions : nonTerminalProductions.entrySet()) {
                    for (var production : validProductions.getValue()) {
                        // get the first item after the non terminal whether it is a terminal, nonterminal or epsilon.
                        var firstItemAfter = getFirstItemAfter(production, nonTerminal);
                        for (var terminal : firstSet.get(firstItemAfter)) {
                            if (terminal.equals("")) {
                                newFollowColumn.get(nonTerminal).addAll(follow.get(validProductions.getKey()));
                            } else {
                                newFollowColumn.get(nonTerminal).addAll(follow.get(nonTerminal));
                                newFollowColumn.get(nonTerminal).addAll(firstSet.get(firstItemAfter));
                            }
                        }
                    }
                }

                if (!newFollowColumn.get(nonTerminal).equals(follow.get(nonTerminal))) {
                    isFollowChanged = true;
                }
            }
            follow.putAll(newFollowColumn);
        }

        return follow;
    }

}
