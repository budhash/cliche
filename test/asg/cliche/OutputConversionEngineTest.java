/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package asg.cliche;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class OutputConversionEngineTest {

    @Before
    public void setUp() {
        converter = new OutputConversionEngine();
        converter.addDeclaredConverters(this);
    }

    OutputConversionEngine converter;

    public static final OutputConverter[] CLI_OUTPUT_CONVERTERS = {
        new OutputConverter() {
            public Object convertOutput(Object toBeFormatted) {
                if (toBeFormatted instanceof String) {
                    return String.format("(%s)", (String)toBeFormatted);
                } else {
                    return null;
                }
            }
        },
        new OutputConverter() {
            public Object convertOutput(Object toBeFormatted) {
                if (toBeFormatted instanceof String) {
                    return String.format("[%s]", (String)toBeFormatted);
                } else {
                    return null;
                }
            }
        },
    };


    /**
     * This method tests the order of applying converters, as other methods of
     * OutputConversionEngine are rather trivial.
     */
    @Test
    public void testConvertOutput() {
        System.out.println("convertOutput");
        String toBeConverted = "a";
        String expected = "([a])";
        assertEquals(expected, converter.convertOutput(toBeConverted));
    }


}