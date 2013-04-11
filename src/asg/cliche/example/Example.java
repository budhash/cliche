/*
 * This example code is in public domain.
 */

package asg.cliche.example;

import asg.cliche.Command;
import asg.cliche.InputConverter;
import asg.cliche.OutputConverter;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellDependent;
import asg.cliche.ShellFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 'more advanced' example.
 *
 * @author ASG
 */
public class Example implements ShellDependent {


    @Command(description="Varargs example")
    public Integer add(
            @Param(name="numbers", description="some numbers to add")
            Integer... numbers) {

        int result = 0;
        for (int i : numbers) {
            result += i;
        }
        return result;
    }

    @Command(description="setPath example")
    public void setPath(String path) {
        List<String> pathComps = new ArrayList<String>();
        pathComps.add(path);
        this.shell.setPath(pathComps);
    }
    
    public static final InputConverter[] CLI_INPUT_CONVERTERS = {

        // You can use Input Converters to support named constants
        new InputConverter() {
            public Integer convertInput(String original, Class toClass) throws Exception {
                if (toClass.equals(Integer.class)) {
                    if (original.equals("one")) return 1;
                    if (original.equals("two")) return 2;
                    if (original.equals("three")) return 3;
                }
                return null;
            }
        }

    };

    public static final OutputConverter[] CLI_OUTPUT_CONVERTERS = {
        
        new OutputConverter() {
            public Object convertOutput(Object o) {
                if (o.getClass().equals(Integer.class)) {
                    int num = (Integer) o;

                    if (num == 1) return "one";
                    if (num == 2) return "two";
                    if (num == 3) return "three";
                }
                return null;
            }
        }

    };


    // The shell which runs us. Needed to create subshells.
    private Shell shell;

    // to get the shell field set
    public void cliSetShell(Shell shell) {
        this.shell = shell;
    }

    @Command(description="Illustrates the concept of subshells, that can be used " +
            "to create a tree-like navigation")
    public void Hello() throws IOException {
        ShellFactory.createSubshell("hello", shell, "That 'Hello, World!' example", new HelloWorld())
                .commandLoop();
    }


    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("example", "The Cliche Shell example\n" +
                "Enter ?l to list available commands.", new Example())
                .commandLoop();
    }

}
