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
    private static Map<String, String> getNonTerminalProductions(String nonTerminal, Grammar grammar) {
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

    private static String getFirstItemAfter(String productionRHS, String nonTerminal) {
        String[] nonTerminals = productionRHS.split(" ");
        // Return the index of the nonTerminal
        int index = IntStream.range(0, nonTerminals.length)
                .filter(i -> nonTerminal.equals(nonTerminals[i]))
                .findFirst()
                .getAsInt(); // it is known that non terminal is present in productionRHS

        // If there is no item after the given nonterminal, return empty string (epsilon)
        // else return the next item.
        if (index == nonTerminals.length - 1) {
            return "\"\"";
        } else {
            return nonTerminals[index + 1];
        }

    }

    public Map<String, Set<String>> generateFirst(Grammar grammar) {
        return null;
    }

    public static Map<String, Set<String>> generateFollow(Grammar grammar, Map<String, Set<String>> firstSet) {
        Map<String, Set<String>> follow = new HashMap<>();
        Set<String> nonTerminals = grammar.getNonTerminals();

        for (String nonTerminal : grammar.getNonTerminals()) {
            follow.put(nonTerminal, new HashSet<>());
        }

        follow.put(grammar.getStart(), new HashSet<>(){{add("\"\"");}});
        boolean isFollowChanged = true;

        while (isFollowChanged) {
            isFollowChanged = false;
            Map<String, Set<String>> newFollowColumn = new HashMap<>();

            for (String nonTerminal : nonTerminals) {
                newFollowColumn.put(nonTerminal, new HashSet<>());
                var productions = getNonTerminalProductions(nonTerminal, grammar);
                for (var production : productions.entrySet()) {
                    var firstItemAfter = getFirstItemAfter(production.getValue(), nonTerminal);
                    for (var terminal : firstSet.get(firstItemAfter)) {
                        if (terminal.equals("\"\"")) {
                            newFollowColumn.get(nonTerminal).addAll(follow.get(production.getKey()));
                        } else {
                            newFollowColumn.get(nonTerminal).addAll(follow.get(nonTerminal));
                            newFollowColumn.get(nonTerminal).addAll(firstSet.get(firstItemAfter));
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
