/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package asg.cliche;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class InputConversionEngineTest {

    @Before
    public void setUp() {
        converter = new InputConversionEngine();
    }

    @After
    public void tearDown() {
    }

    private InputConversionEngine converter;

    @Test
    public void testElementaryTypes() throws Exception {
        System.out.println("testElementaryTypes");
        String[] strings = {
            "aString",
            "some-object",
            "1243",
            "12432141231256",
            "12.6",
            "12.6",
            "true",
        };
        Class[] classes = {
            String.class,
            Object.class,
            Integer.class,
            Long.class,
            Double.class,
            Float.class,
            Boolean.class,
        };
        Object[] results = {
            "aString",
            (Object)"some-object",
            new Integer(1243),
            new Long(12432141231256l),
            new Double(12.6),
            new Float(12.6f),
            new Boolean(true),
        };

        for (int i = 0; i < strings.length; i++) {
            assertEquals(results[i], converter.convertInput(strings[i], classes[i]));
        }

    }

    private static final int MAGIC_INT = 234;

    private static final InputConverter testInputConverter = new InputConverter() {
        public Object convertInput(String original, Class toClass) throws Exception {
            if (toClass.equals(Integer.class)) {
                return MAGIC_INT; // no matter what was the text, for test purposes
            } else {
                return null;
            }
        }
    };

    @Test
    public void testConverterRegistration() throws Exception {
        System.out.println("testConverterRegistration");
        InputConversionEngine otherConverter = new InputConversionEngine();
        otherConverter.addConverter(testInputConverter);
        assertEquals(new Integer(MAGIC_INT), otherConverter.convertInput("10", Integer.class));
        otherConverter.removeConverter(testInputConverter);
        assertEquals(new Integer(MAGIC_INT), otherConverter.convertInput(
                Integer.toString(MAGIC_INT), Integer.class));
    }

    public static InputConverter[] CLI_INPUT_CONVERTERS = {
        testInputConverter,
    };

    @Test
    public void testDeclaredConverterRegistration() throws Exception {
        System.out.println("testDeclaredConverterRegistration");
        InputConversionEngine otherConverter = new InputConversionEngine();
        otherConverter.addDeclaredConverters(this);
        assertEquals(new Integer(MAGIC_INT), otherConverter.convertInput("10", Integer.class));
    }

}