package model;

import java.util.HashMap;
import java.util.Map;

public class TokenTable {
    private Map<String, Integer> tokenTable;

    private void initializeTokenTable() {
        tokenTable.put("+", 1);
        tokenTable.put("-", 2);
        tokenTable.put("*", 3);
        tokenTable.put("/", 4);
        tokenTable.put("%", 5);
        tokenTable.put("<", 6);
        tokenTable.put("<=", 7);
        tokenTable.put(">=", 8);
        tokenTable.put("!=", 9);
        tokenTable.put("==", 10);
        tokenTable.put(":=", 11);
        tokenTable.put(";", 12);;
        tokenTable.put("]", 13);
        tokenTable.put("[", 14);
        tokenTable.put("(", 15);
        tokenTable.put(")", 16);
        tokenTable.put("/n", 17);
        tokenTable.put(" ", 18);
        tokenTable.put("int", 19);
        tokenTable.put("char", 20);
        tokenTable.put("while", 21);
        tokenTable.put("do", 22);
        tokenTable.put("read", 23);
        tokenTable.put("write", 24);
        tokenTable.put("for", 25);
        tokenTable.put("if", 26);
        tokenTable.put("constant", 27);
        tokenTable.put(",", 28);

    }

    public TokenTable() {
        tokenTable = new HashMap<>();
        tokenTable.put("identifier", 0);
        initializeTokenTable();
    }

    public Integer getTokenCode(String key) {
        return this.tokenTable.get(key);
    }

    public Boolean searchToken(String key) {
        return this.tokenTable.containsKey(key);
    }
}
