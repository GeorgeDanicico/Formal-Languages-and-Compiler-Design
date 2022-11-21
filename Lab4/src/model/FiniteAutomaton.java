package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FiniteAutomaton {
    private String initialState;
    private Set<String> alphabet;
    private Set<String> states;
    private Set<String> finalStates;

    private Map<Pair<String, String>, Set<String>> transitions;

    public FiniteAutomaton(String fileName) {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
        readFile(fileName);
    }

    private void readFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue;

                String[] lineElements = line.split(",");

                if (lineElements.length == 2) {
                    if (lineElements[1].equals("initial")) {
                        initialState = lineElements[0];
                        states.add(lineElements[0]);
                    } else {
                        finalStates.add(lineElements[0]);
                        states.add(lineElements[0]);
                    }
                } else {
                    String transitionState1 = lineElements[0];
                    String transitionState2 = lineElements[2];
                    String alphabetElement = lineElements[1];

                    states.add(transitionState1);
                    states.add(transitionState2);
                    alphabet.add(alphabetElement);

                    Pair<String, String> pair = new Pair<String, String>(transitionState1, alphabetElement);

                    if (transitions.get(pair) != null) {
                        transitions.get(pair).add(transitionState2);
                    } else {
                        Set<String> destinationTransitions = new HashSet<>();
                        destinationTransitions.add(transitionState2);
                        transitions.put(pair, destinationTransitions);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public String getInitialState() {
        return initialState;
    }

    public String getTransitions() {
        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<Pair<String, String>, Set<String>> entry : transitions.entrySet()) {

            for (String val : entry.getValue()) {
                String str = "\uD835\uDEFF(" + entry.getKey().getFirst() + ", " + entry.getKey().getSecond() + ") = " + val;
                stringBuilder.append(str).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public boolean checkIfSequenceIsValid(String sequence) {
        String state = this.initialState;

        if (sequence.length() == 0) return false;
        int i = 0;
        while (i < sequence.length()) {
            char c = sequence.charAt(i);
            Pair<String, String> pair = new Pair<>(state, String.valueOf(c));

            Set<String> possibleNextStates = transitions.get(pair);

            if (possibleNextStates == null) return false;

            state = possibleNextStates.iterator().next();
            i++;
        }

        return finalStates.contains(state);
    }
}
