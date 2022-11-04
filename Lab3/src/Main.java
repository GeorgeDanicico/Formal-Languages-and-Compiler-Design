import model.JavaScanner;
import model.SymbolTable;

public class Main {
    public static void main(String[] args) {
        JavaScanner javaScanner = new JavaScanner();
        String pathP1 = "src/tests/p1.txt";
        String pathP2 = "src/tests/p2.txt";
        String pathP3 = "src/tests/p3.txt";
        String pathP1err = "src/tests/p1err.txt";

        try {
            javaScanner.scan(pathP1err);
            System.out.println("Program is lexically correct.");
        } catch (Exception e) {
            System.out.println("Program is not lexically correct.\n" + e);
        }

        javaScanner.writeToFile("STerr.out", "PIFerr.out");

    }
}