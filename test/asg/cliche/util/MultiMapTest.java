/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package asg.cliche.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class MultiMapTest {

    public MultiMapTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void test() {
        System.out.println("test");
        MultiMap<String, String> map = new ArrayHashMultiMap<String, String>();
        map.put("k1", "v11");
        map.put("k2", "v21");
        map.put("k1", "v12");
        assertEquals(3, map.size());
        map.remove("k1", "v12");
        assertEquals(2, map.size());
        map.put("k2", "v22");
        map.removeAll("k2");
        assertEquals(1, map.size());
    }

}