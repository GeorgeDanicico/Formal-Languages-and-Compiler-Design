package flcd.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LLParserTest extends TestCase {
    private Grammar grammar;
    private final String EPSILON = "eps";

    @Test
    public void testComputeFirst1() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar1.txt");
        var first = LLParser.computeFirst(grammar);

        System.out.println(first);
        assertEquals(2, first.get("C").size());
        assertTrue(first.get("C").contains("*"));
        assertTrue(first.get("C").contains(EPSILON));
        assertFalse(first.get("C").contains("a"));

        assertEquals(2, first.get("B").size());
        assertTrue(first.get("A").contains("("));
        assertTrue(first.get("A").contains("int"));
        assertFalse(first.get("A").contains(EPSILON));
    }

    @Test
    public void testComputeFirst2() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar2.txt");
        var first = LLParser.computeFirst(grammar);

        assertEquals(2, first.get("C").size());
        assertTrue(first.get("C").contains("*"));
        assertTrue(first.get("C").contains(EPSILON));
        assertFalse(first.get("C").contains("a"));

        assertEquals(2, first.get("B").size());
        assertTrue(first.get("B").contains("("));
        assertTrue(first.get("B").contains("a"));
        assertFalse(first.get("B").contains(EPSILON));
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

        assertEquals(4, follow.size());
        assertTrue(follow.containsKey("A"));
        assertEquals(3, follow.get("A").size());
        assertEquals(2, follow.get("S").size());
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

        assertEquals(5, follow.size());
        assertTrue(follow.containsKey("A"));
        assertEquals(3, follow.get("A").size());
        assertEquals(2, follow.get("S").size());
    }
}