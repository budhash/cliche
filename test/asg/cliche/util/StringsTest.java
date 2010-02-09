/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package asg.cliche.util;

import asg.cliche.util.Strings;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class StringsTest {

    /**
     * Test of fixCase method, of class Strings.
     */
    @Test
    public void testFixCase() {
        System.out.println("fixCase");
        String[] cases = {
            "",
            "a",
            "A",
            "Abc",
            "ABC",
            "ABc"
        };
        String[] results = {
            "",
            "a",
            "a",
            "abc",
            "ABC",
            "ABc"
        };
        for (int i = 0; i < cases.length; i++) {
            assertEquals(results[i], Strings.fixCase(cases[i]));
        }
    }

    /**
     * Test of joinStrings method, of class Strings.
     */
    @Test
    public void testJoinStrings() {
        System.out.println("JoinStrings");
        String[] cases = {
            "a",
            "a|b|c",
            ""
        };
        String[] results = {
            "a",
            "a-b-c",
            ""
        };
        for (int i = 0; i < cases.length; i++) {
            assertEquals(results[i], Strings.joinStrings(Arrays.asList(results[i].split("\\|")), false,'-'));
        }
    }

    /**
     * Test of splitJavaIdentifier method, of class Strings.
     */
    @Test
    public void testSplitJavaIdentifier() {
        System.out.println("splitJavaIdentifier");
        String[] cases = {
            "",
            "void",
            "splitJavaIdentifier",
            "NUnit",
            "feedURL",
            "ConvertURLClass"
        };
        String[] dashJoined = {
            "",
            "void",
            "split-java-identifier",
            "n-unit",
            "feed-URL",
            "convert-URL-class"
        };
        for (int i = 0; i < cases.length; i++) {
            assertEquals(dashJoined[i], Strings.joinStrings(Strings.splitJavaIdentifier(cases[i]), true,'-'));
        }
    }

}