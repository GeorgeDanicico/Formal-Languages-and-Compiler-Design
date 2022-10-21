import model.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symTable = new SymbolTable(10);

        symTable.add("v");
        symTable.add("'abc'");

        System.out.println(symTable);
    }
}