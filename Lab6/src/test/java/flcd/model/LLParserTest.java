package flcd.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LLParserTest extends TestCase {

    Grammar grammar;

    public LLParserTest() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/grammar1.txt");
    }

    @Test
    public void testGenerateFollow() {

        Map<String, Set<String>> firstSet = new HashMap<>();
        firstSet.put("\"\"", new HashSet<>(){{add("\"\"");}});
        firstSet.put("\"(\"", new HashSet<>(){{add("\"(\"");}});
        firstSet.put("\"+\"", new HashSet<>(){{add("\"+\"");}});
        firstSet.put("\")\"", new HashSet<>(){{add("\")\"");}});
        firstSet.put("\"*\"", new HashSet<>(){{add("\"*\"");}});
        firstSet.put("\"int\"", new HashSet<>(){{add("\"int\"");}});
        firstSet.put("S", new HashSet<>(){{add("\"(\"");add("\"int\"");}});
        firstSet.put("A", new HashSet<>(){{add("\"(\"");add("\"int\"");}});
        firstSet.put("B", new HashSet<>(){{add("\"+\"");add("\"\"");}});
        firstSet.put("C", new HashSet<>(){{add("\"*\"");add("\"\"");}});

        Map<String, Set<String>> follow = LLParser.generateFollow(grammar, firstSet);

        assertEquals(4, follow.size());
        assertEquals(true, follow.containsKey("A"));
    }
}