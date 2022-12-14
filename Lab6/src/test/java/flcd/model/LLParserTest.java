package flcd.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LLParserTest extends TestCase {

    Grammar grammar;

    public LLParserTest() throws IOException {
        grammar = Grammar.provideGrammar("src/main/java/flcd/io/bnf-syntax.txt");
    }

    @Test
    public void testGenerateFollow() {
        Map<String, Set<String>> follow = LLParser.generateFollow(grammar, null);

        System.out.println(follow);
    }
}