package flcd.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class LLParser {
    /**
     * This function returns all the productions in which the given non terminal appear in the right hand side.
     * @param nonTerminal -> the given nonterminal
     * @param grammar -> the given grammar
     * @return
     */
    private static Map<String, String> getProductions(String nonTerminal, Grammar grammar) {
        Map<String, Set<String>> productions = grammar.getProductions();
        Map<String, String> requiredProductions = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : productions.entrySet()) {

            // iterate through the production of every nonterminal and check if
            // the given nonterminal appears in the right hand side of the production.
            for (String production : entry.getValue()) {
                if (production.contains(nonTerminal)) {
                    requiredProductions.put(entry.getKey(), production);
                }
            }
        }

        return requiredProductions;
    }

    /**
     * @param nonTerminals -> non terminals in the grammar
     * @param grammar
     * @return -> a map that has as key a non terminal, and as value a map containing all the productions where
     *          the key is in the right hand side.
     */
    private static Map<String, Map<String, String>> getAllProductions(Set<String> nonTerminals, Grammar grammar) {
        Map<String, Map<String, String>> nonTerminalProductions = new HashMap<>();

        for (var nonTerminal : nonTerminals) {
            Map<String, String> productions = getProductions(nonTerminal, grammar);
            nonTerminalProductions.put(nonTerminal, productions);
        }

        return nonTerminalProductions;
    }

    private static String getFirstItemAfter(String productionRHS, String nonTerminal) {
        String[] nonTerminals = productionRHS.split(" ");
        // Return the index of the nonTerminal
        int index = IntStream.range(0, nonTerminals.length)
                .filter(i -> nonTerminal.equals(nonTerminals[i]))
                .findFirst()
                .getAsInt(); // it is known that non terminal is present in productionRHS

        if (index == nonTerminals.length - 1) {
            return "";
        } else {
            return nonTerminals[index + 1];
        }

    }

    public Map<String, Set<String>> generateFirst(Grammar grammar) {
        return null;
    }

    public static Map<String, Set<String>> generateFollow(Grammar grammar, Map<String, Set<String>> first) {


        Map<String, Set<String>> follow = new HashMap<>();
        Set<String> nonTerminals = grammar.getNonTerminals();
        var nonTerminalProductions = getAllProductions(nonTerminals, grammar);

        for (String nonTerminal : grammar.getNonTerminals()) {
            follow.put(nonTerminal, null);
        }

        follow.put(grammar.getStart(), new HashSet<>(){{
            add("");
        }});

        boolean changed = true;

        while (changed) {

            for (String nonTerminal : nonTerminals) {
                var productions = nonTerminalProductions.get(nonTerminal);

                for (var entry : productions.entrySet()) {

                }
            }

        }

        return follow;
    }

}
