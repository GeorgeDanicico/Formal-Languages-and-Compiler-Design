package model;

import java.io.*;

public class JavaScanner {
    private TokenTable tokenTable;
    private ProgramInternalForm programInternalForm;
    private SymbolTable symbolTable;

    private FiniteAutomaton integerFA, identifierFA;
    private static final int SYMBOL_TABLE_SIZE = 10;

    private Integer findIdentifier(String line, int startIndex) {
        int endIndex = startIndex + 1;
        int length = line.length();

        while (endIndex < line.length() && (isCharDigit(line.charAt(endIndex)) || isCharLetter(line.charAt(endIndex)))) endIndex++;

        // check if the current word is a keyword that is present in the token table.
        if (tokenTable.searchToken(line.substring(startIndex, endIndex))) return endIndex;

//        if (endIndex < length && (line.charAt(endIndex) == '+' || line.charAt(endIndex) == '-')) {
//            if (endIndex + 1 < length) {
//                if (isCharLetter(line.charAt(endIndex)) || isCharDigit(line.charAt(endIndex)))
//                    endIndex = findIdentifier(line, endIndex);
//            }
//        }

        return endIndex;
    }

    // Check if a character is a digit
    private boolean isCharDigit(char ch) {
        int value = ch - 48;

        return (value >= 0 && value <= 9);
    }

    // Check if a character is a lower or upper case letter
    private boolean isCharLetter(char ch) {
        int letter = ch - 97;
        int capitalLetter = ch - 65;

        return (letter >= 0 && letter <= 25) || (capitalLetter >= 0 && capitalLetter <= 25);
    }

    public JavaScanner() {
        this.tokenTable = new TokenTable();
        this.symbolTable = new SymbolTable(SYMBOL_TABLE_SIZE);
        this.programInternalForm = new ProgramInternalForm();
        this.integerFA = new FiniteAutomaton("src/integer.csv");
        this.identifierFA = new FiniteAutomaton("src/identifier.csv");
    }

    // Formats the line
    private String[] formatLine(String line) {
        String LINE_SEPARATOR = "~";
        StringBuilder sb = new StringBuilder();
        int startIndex = 0;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            // ignore spaces.
            if (ch == ' ') continue;

            // Check if there is the one of the symbols that is compound of 2 characters
            // Else check if the character is a quote, which would mark te beginning of a char constant
            // Else check if there is an identifier or a number that starts with +/-
            // Else check if the character is a valid token.
            if (i < line.length() - 1 && (ch == '<' || ch == '>' || ch == ':' || ch == '=')) {
                char nextCh = line.charAt(i + 1);
                if (nextCh == '=') {
                    sb.append(ch).append(nextCh).append(LINE_SEPARATOR);
                    i++;
                } else {
                    sb.append(ch).append(LINE_SEPARATOR);
                }
            } else if (ch == '"') {
                startIndex = i;
                i++;
                while (i < line.length() && line.charAt(i) != '"') i++;

                // get the substring that constitutes the string and append it to the stringbuilder along with
                // the separator
                if (i < line.length()) {
                    String string = line.substring(startIndex, i + 1);
                    sb.append(string).append(LINE_SEPARATOR);
                }
            } else if (isCharLetter(ch) || isCharDigit(ch) || ch == '+' || ch == '-') {
                startIndex = i;
                int endIndex = findIdentifier(line, startIndex);

                if (endIndex < line.length()) {
                    String string = line.substring(startIndex, endIndex);
                    i = endIndex - 1;
                    sb.append(string).append(LINE_SEPARATOR);
                }
            } else if (tokenTable.searchToken(String.valueOf(ch))) {
                sb.append(ch).append(LINE_SEPARATOR);
            }
        }
        return sb.toString().split(LINE_SEPARATOR);
    }
    public void scan(String file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file)));
        String line;
        int lineNumber = 0;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = this.formatLine(line);
                parseLine(elements, lineNumber);

                lineNumber += 1;
            }
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    private void parseLine(String[] lineElements, int lineNumber) throws Exception{
        // iterate through the elements of a line and checks if all of them represent valid tokens

        if (lineElements.length == 1 && lineElements[0].equals("")) {
            return;
        }

        for (String token : lineElements) {
            Boolean isLineLexicalCorrect = true;
            String unknownToken = "";
            if (!tokenTable.searchToken(token)) {
                if (isCharConstant(token) || integerFA.checkIfSequenceIsValid(token)) {
                    symbolTable.add(token);
                    Pair<Integer, Integer> symbolTablePosition = symbolTable.position(token);
                    programInternalForm.add("constant", 27, symbolTablePosition);
                } else {
                    String tk = isIdentifier(token);

                    if (tk != null) {
                        char firstChar = tk.charAt(0);
                        if (firstChar == '+' || firstChar == '-') {
                            symbolTable.add(String.valueOf(firstChar));
                            tk = tk.substring(1);
                            programInternalForm.add(String.valueOf(firstChar), tokenTable.getTokenCode(String.valueOf(firstChar)), symbolTable.position(String.valueOf(firstChar)));
                        }

                        symbolTable.add(tk);
                        Pair<Integer, Integer> symbolTablePosition = symbolTable.position(tk);
                        programInternalForm.add("identifier", 0, symbolTablePosition);
                    } else {
                        isLineLexicalCorrect = false;
                        unknownToken = token;
                    }
                }
            } else {
                int code = tokenTable.getTokenCode(token);
                symbolTable.add(token);
                Pair<Integer, Integer> symbolTablePosition = symbolTable.position(token);
                programInternalForm.add(token, code, symbolTablePosition);
            }

            if (!isLineLexicalCorrect) {
                throw new Exception("Unknown token " + unknownToken + " at line: " + lineNumber);
            }
        }
    }

    private Boolean isCharConstant(String token) {
        // Checks if a string from the line represents a string constant
        int tokenLength = token.length();

        if (tokenLength < 2) return false;
        if (token.charAt(0) == '"' && token.charAt(tokenLength - 1) != '"') return false;
        if (token.charAt(0) != '"' && token.charAt(tokenLength - 1) == '"') return false;
        if (token.charAt(0) != '"' || token.charAt(tokenLength - 1) != '"') return false;

        for (int i = 1; i < tokenLength - 1; i++) {
            if (token.charAt(i) == '"') {
                return false;
            }
        }

        return true;
    }

    private Boolean isIntConstant(String token) {
        int tokenLength = token.length();
        if (tokenLength == 0) return false;

        if (isCharConstant(token)) return false;

        for (int i = 0; i < tokenLength; i++) {
            char ch = token.charAt(i);
            // check the special cases when the token is a string such as "+1212", "-3231"
            if (i == 0 && (ch == '+' || ch == '-')) continue;

            if (tokenTable.searchToken(String.valueOf(ch)) || isCharLetter(ch)) {
                return false;
            }
        }

        return true;
    }

    private String isIdentifier(String token) {
        // Identifiers must start with a letter!
        int tokenLength = token.length();
        if (tokenLength == 0) return null;

        // Treat the special case when there is + or - before the name of an identifier
        String savedToken = token;
        if (token.charAt(0) == '+' || token.charAt(0) == '-') savedToken = token.substring(1);

        if (identifierFA.checkIfSequenceIsValid(savedToken)) {
            if (tokenTable.searchToken(savedToken)) return null;

            return token;
        }

        return null;
    }

    public String getState() {
        return programInternalForm.toString() + "\n" + symbolTable.toString();
    }

    public void writeToFile(String stFile, String pifFile) {
        try {
            BufferedWriter stBufferedWriter = new BufferedWriter(new FileWriter(stFile));
            BufferedWriter pifBufferedWriter = new BufferedWriter(new FileWriter(pifFile));

            stBufferedWriter.write(symbolTable.toString());
            pifBufferedWriter.write(programInternalForm.toString());

            stBufferedWriter.close();
            pifBufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
