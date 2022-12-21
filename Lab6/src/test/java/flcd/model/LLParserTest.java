package flcd.model;

import flcd.common.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class LLParserTest {
    private Grammar grammar;
    private final String EPSILON = "eps";

    @Test
    public void testComputeFirst1() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar1.txt");
        var first = LLParser.computeFirst(grammar);

        System.out.println(first);
        Assert.assertEquals(2, first.get("C").size());
        Assert.assertTrue(first.get("C").contains("*"));
        Assert.assertTrue(first.get("C").contains(EPSILON));
        Assert.assertFalse(first.get("C").contains("a"));

        Assert.assertEquals(2, first.get("B").size());
        Assert.assertTrue(first.get("A").contains("("));
        Assert.assertTrue(first.get("A").contains("int"));
        Assert.assertFalse(first.get("A").contains(EPSILON));
    }

    @Test
    public void testComputeFirst2() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar2.txt");
        var first = LLParser.computeFirst(grammar);

        Assert.assertEquals(2, first.get("C").size());
        Assert.assertTrue(first.get("C").contains("*"));
        Assert.assertTrue(first.get("C").contains(EPSILON));
        Assert.assertFalse(first.get("C").contains("a"));

        Assert.assertEquals(2, first.get("B").size());
        Assert.assertTrue(first.get("B").contains("("));
        Assert.assertTrue(first.get("B").contains("a"));
        Assert.assertFalse(first.get("B").contains(EPSILON));
    }

    @Test
    public void testComputeFollow1() throws IOException {

        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar1.txt");
        Map<String, Set<String>> firstSet = new HashMap<>();
        firstSet.put(EPSILON, new HashSet<>(){{add(EPSILON);}});
        firstSet.put("(", new HashSet<>(){{add("(");}});
        firstSet.put("+", new HashSet<>(){{add("+");}});
        firstSet.put(")", new HashSet<>(){{add(")");}});
        firstSet.put("*", new HashSet<>(){{add("*");}});
        firstSet.put("int", new HashSet<>(){{add("int");}});
        firstSet.put("S", new HashSet<>(){{add("(");add("int");}});
        firstSet.put("A", new HashSet<>(){{add("(");add("int");}});
        firstSet.put("B", new HashSet<>(){{add("+");add(EPSILON);}});
        firstSet.put("C", new HashSet<>(){{add("*");add(EPSILON);}});

        Map<String, Set<String>> follow = LLParser.computeFollow(grammar, firstSet);

        Assert.assertEquals(4, follow.size());
        Assert.assertTrue(follow.containsKey("A"));
        Assert.assertEquals(3, follow.get("A").size());
        Assert.assertEquals(2, follow.get("S").size());
    }

    @Test
    public void testComputeFollow2() throws IOException {

        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar2.txt");
        Map<String, Set<String>> firstSet = new HashMap<>();
        firstSet.put(EPSILON, new HashSet<>(){{add(EPSILON);}});
        firstSet.put("(", new HashSet<>(){{add("(");}});
        firstSet.put("+", new HashSet<>(){{add("+");}});
        firstSet.put(")", new HashSet<>(){{add(")");}});
        firstSet.put("*", new HashSet<>(){{add("*");}});
        firstSet.put("int", new HashSet<>(){{add("a");}});
        firstSet.put("S", new HashSet<>(){{add("(");add("a");}});
        firstSet.put("A", new HashSet<>(){{add("+");add(EPSILON);}});
        firstSet.put("B", new HashSet<>(){{add("(");add("a");}});
        firstSet.put("C", new HashSet<>(){{add("*");add(EPSILON);}});
        firstSet.put("D", new HashSet<>(){{add("(");add("a");}});


        Map<String, Set<String>> follow = LLParser.computeFollow(grammar, firstSet);

        Assert.assertEquals(5, follow.size());
        Assert.assertTrue(follow.containsKey("A"));
        Assert.assertEquals(2, follow.get("A").size());
        Assert.assertEquals(4, follow.get("D").size());
        Assert.assertTrue(follow.get("D").contains("*"));
    }

    @Test
    public void testComputeParsingTable1() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar2.txt");

        var parsingTable = LLParser.computeParseTable(grammar);

        var productions = grammar.getProductions();

        assertNotNull(parsingTable);

        Stack<String> inputStack = new Stack<>();
        inputStack.push(EPSILON);
        inputStack.push(")");
        inputStack.push("a");
        inputStack.push("+");
        inputStack.push("a");
        inputStack.push("(");
        inputStack.push("*");
        inputStack.push("a");
        String transitions = "";
        Stack<String> workStack = new Stack<>();
        workStack.push(EPSILON);
        workStack.add("S");

        while (!workStack.isEmpty()) {
            String row = workStack.peek();
            String column = inputStack.peek();
            if (row.equals(column)) {
                workStack.pop();
                inputStack.pop();
            } else {
                Pair<String, Integer> pair = parsingTable.get(new Pair<>(row, column));
                var prod = pair.getFirst().split(" ");
                workStack.pop();
                for (int i = prod.length - 1; i >= 0; i--) {
                    if (!prod[i].equals(EPSILON)) {
                        workStack.push(prod[i]);
                    }
                }
                var index = pair.getSecond();
                transitions += index;
            }
        }

        assertEquals(16, transitions.length());
        assertEquals("6375863742374141", transitions);
    }
}