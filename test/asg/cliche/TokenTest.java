/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package asg.cliche;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class TokenTest {

    /**
     * Test of tokenize method, of class Token.
     */
    @Test
    public void testTokenize() {
        System.out.println("tokenize");
        String[] cases = {
            "",
            "aSingleToken",
            "a b c",
            "an's g'ri # quotation test",
            "Shell instance = new Shell(new ShellTest(), System.out",
            "dir \"E:\\ASG\\!dynamic\\projects\" \t-l 3492.9  ",
            "a b c ''",
            " \"\" "
        };

        int[] sizes = {
            0, 1, 3, 1, 7, 4, 4, 1
        };

        for (int i = 0; i < cases.length; i++) {
            List<Token> result = Token.tokenize(cases[i]);
            System.out.println("case: " + cases[i]);
            for (Token t : result) {
                System.out.print(t + " ");
            }
            System.out.println();

            assertEquals(sizes[i], result.size());
        }
    }

    /**
     * Test of escapeString method, of class Token.
     */
    @Test
    public void testEscapeString() {
        System.out.println("escapeString");
        String[] cases = {
            "aSingleToken",
            "a b c",
            "an's g'ri # quotation test",
            "Shell instance = new Shell(new ShellTest(), System.out",
            "dir \"E:\\ASG\\!dynamic\\projects\" \t-l 3492.9  "
        };

        for (int i = 0; i < cases.length; i++) {
            String escaped = Token.escapeString(cases[i]);
            List<Token> result = Token.tokenize(escaped);
            System.out.println("case: " + cases[i]);
            System.out.println("escaped: " + escaped);

            assertEquals(1, result.size());
            assertEquals(cases[i], result.get(0).getString());
        }
    }

}