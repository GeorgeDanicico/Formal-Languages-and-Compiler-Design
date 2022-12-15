package flcd.model;

import java.util.*;

public class LLParser {
    private static final String EPSILON = "eps";

    public static Map<String, Set<String>> computeFirst(Grammar grammar) {
        var nonTerminals = grammar.getNonTerminals();
        var terminals = grammar.getTerminals();
        var first = computeInitialFirst(grammar);

        var hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            var newFirst = new HashMap<String, Set<String>>();

            for (var nonTerminal: nonTerminals) {
                var toAdd = computeToAddForNonTerminal(nonTerminal, grammar, first);

                if (!toAdd.equals(first.get(nonTerminal))) {
                    hasChanged = true;
                }
                newFirst.put(nonTerminal, toAdd);
            }
            first = newFirst;
        }

        for (var terminal: terminals) {
            first.computeIfAbsent(terminal, k -> new HashSet<>());
            first.get(terminal).add(terminal);
        }

        return first;
    }

    private static HashMap<String, Set<String>> computeInitialFirst(Grammar grammar) {
        var first = new HashMap<String, Set<String>>();
        var terminals = grammar.getTerminals();
        var nonTerminals = grammar.getNonTerminals();

        for (var nonTerminal: nonTerminals) {
            first.computeIfAbsent(nonTerminal, k -> new HashSet<>());
            var nonTerminalProductions = grammar.getProductions(nonTerminal);

            for (var production: nonTerminalProductions) {
                if (terminals.contains(production.get(0))) {
                    first.get(nonTerminal).add(production.get(0));
                }
            }
        }

        return first;
    }

    private static Set<String> computeToAddForNonTerminal(String nonTerminal,
                                                          Grammar grammar,
                                                          HashMap<String, Set<String>> first) {
        var nonTerminals = grammar.getNonTerminals();
        var productions = grammar.getProductions(nonTerminal);
        var toAdd = new HashSet<>(first.get(nonTerminal));

        for (var production: productions) {
            List<String> rhsNonTerminals = new ArrayList<>();
            String rhsTerminal = null;
            for (String symbol : production)
                if (nonTerminals.contains(symbol))
                    rhsNonTerminals.add(symbol);
                else {
                    rhsTerminal = symbol;
                    break;
                }
            toAdd.addAll(computeConcatenationsOfLengthOne(first, rhsNonTerminals, rhsTerminal));
        }

        return toAdd;
    }

    private static Set<String> computeConcatenationsOfLengthOne(Map<String, Set<String>> first,
                                                                List<String> nonTerminals,
                                                                String terminal) {
        if (nonTerminals.size() == 0) {
            return new HashSet<>();
        }

        if (nonTerminals.size() == 1) {
            return first.get(nonTerminals.iterator().next());
        }

        var concatenation = new HashSet<String>();
        var allEpsilon = true;

        for (var nonTerminal : nonTerminals) {
            if (!first.get(nonTerminal).contains(EPSILON)) {
                allEpsilon = false;
            }
        }
        if (allEpsilon) {
            concatenation.add(Objects.requireNonNullElse(terminal, EPSILON));
        }

        for (var nonTerminal : nonTerminals) {
            var noEpsilons = true;
            for (var s : first.get(nonTerminal)) {
                if (s.equals(EPSILON)) {
                    noEpsilons = false;
                }
                else {
                    concatenation.add(s);
                }
            }

            if (noEpsilons) break;
        }
        return concatenation;
    }

    /**
     * This function returns all the productions in which the given non terminal appear in the right-hand side.
     * @param nonTerminal -> the given non terminal
     * @param grammar -> the given grammar
     * @return all the productions
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
            return EPSILON;
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

        follow.put(grammar.getStart(), new HashSet<>(){{add(EPSILON);}});
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
                            if (terminal.equals(EPSILON)) {
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
