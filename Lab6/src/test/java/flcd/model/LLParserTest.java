package flcd.model;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LLParserTest extends TestCase {

    Grammar grammar;

    @Test
    public void testComputeFollow1() throws IOException {

        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar1.txt");
        Map<String, Set<String>> firstSet = new HashMap<>();
        firstSet.put("", new HashSet<>(){{add("");}});
        firstSet.put("(", new HashSet<>(){{add("(");}});
        firstSet.put("+", new HashSet<>(){{add("+");}});
        firstSet.put(")", new HashSet<>(){{add(")");}});
        firstSet.put("*", new HashSet<>(){{add("*");}});
        firstSet.put("int", new HashSet<>(){{add("int");}});
        firstSet.put("S", new HashSet<>(){{add("(");add("int");}});
        firstSet.put("A", new HashSet<>(){{add("(");add("int");}});
        firstSet.put("B", new HashSet<>(){{add("+");add("");}});
        firstSet.put("C", new HashSet<>(){{add("*");add("");}});

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
        firstSet.put("", new HashSet<>(){{add("");}});
        firstSet.put("(", new HashSet<>(){{add("(");}});
        firstSet.put("+", new HashSet<>(){{add("+");}});
        firstSet.put(")", new HashSet<>(){{add(")");}});
        firstSet.put("*", new HashSet<>(){{add("*");}});
        firstSet.put("int", new HashSet<>(){{add("a");}});
        firstSet.put("S", new HashSet<>(){{add("(");add("a");}});
        firstSet.put("A", new HashSet<>(){{add("+");add("");}});
        firstSet.put("B", new HashSet<>(){{add("(");add("a");}});
        firstSet.put("C", new HashSet<>(){{add("*");add("");}});
        firstSet.put("D", new HashSet<>(){{add("(");add("a");}});


        Map<String, Set<String>> follow = LLParser.computeFollow(grammar, firstSet);

        assertEquals(5, follow.size());
        assertTrue(follow.containsKey("A"));
        assertEquals(2, follow.get("A").size());
        assertEquals(4, follow.get("D").size());
        assertTrue(follow.get("D").contains("*"));
    }
}