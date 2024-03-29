package flcd.model;

import flcd.common.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.*;

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

        List<String> sequence = Arrays.asList("a", "*", "(", "a", "+", "a", ")");

        List<Integer> transitions = LLParser.parseSequence(grammar, sequence);
        String transitionsStr = transitions.stream().map(Object::toString).reduce("", (str, e) -> str + e);

        assertEquals("6375863742374141", transitionsStr);

        ParserOutput parserOutput = new ParserOutput(grammar, sequence, "src/main/java/flcd/out/grammar2.txt");
        parserOutput.printTree();
    }

    @Test
    public void testComputeParsingTable2() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/bnf-syntax.txt");
        var first = LLParser.computeFirst(grammar);
        var follow = LLParser.computeFollow(grammar, first);
        String sequenceString = "prgrm lab1 ; var v1 : int , v2 : int , v3 : int , max : int ; begin read v1 ; read v2 ; read v3 ; max = v1 ; if max < v2 then max = v2 ; " +
                "if max < v3 then max = v3 ; write max ; end .";
//        prgrm test;
//
//        var lftc: int;
//
//        begin
//             lftc=1;
//             write lftc;
//        end.

        List<String> sequence = Arrays.asList(sequenceString.split(" "));
        List<Integer> transitions = LLParser.parseSequence(grammar, sequence);
//        String transitionsStr = transitions.stream().map(Object::toString).reduce("", (str, e) -> str + e);
//
//        assertEquals("6375863742374141", transitionsStr);
//
//        ParserOutput parserOutput = new ParserOutput(grammar, sequence, "src/main/java/flcd/out/bnf-syntax.txt");
//        parserOutput.printTree();
    }
}