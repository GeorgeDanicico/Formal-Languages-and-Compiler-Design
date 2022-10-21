import model.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symTable = new SymbolTable(10);

        symTable.add("v");
        symTable.add("'abc'");
        symTable.add("v1");
        symTable.add("'abc1'");
        symTable.add("v2");
        symTable.add("'abc2'");
        symTable.add("v3");
        symTable.add("'abc3'");
        symTable.add("v4");
        symTable.add("'abc4'");
        symTable.add("v5");
        symTable.add("'abc5'");
        symTable.add("v6");
        symTable.add("'abc6'");

        System.out.println(symTable);
    }
}