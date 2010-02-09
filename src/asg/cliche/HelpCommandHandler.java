/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package asg.cliche;

import java.util.ArrayList;
import java.util.List;

/**
 * Help command handler (usually prefixed by '?').
 * @author ASG
 */
public class HelpCommandHandler implements ShellDependent {

    private Shell owner;

    public void cliSetShell(Shell theShell) {
        this.owner = theShell;
    }
    
    @Command(description="List all commands",
            header=COMMAND_LIST_HEADER)
    public List<String> listAll() {
        List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
        List<String> result = new ArrayList<String>(commands.size());
        for (ShellCommand command : commands) {
            result.add(formatCommandShort(command));
        }
        return result;
    }

    @Command(description="List all commands with no prefix",
            header=COMMAND_LIST_HEADER)
    public List<String> list() {
        List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
        List<String> result = new ArrayList<String>(commands.size());
        for (ShellCommand command : commands) {
            if (command.getPrefix() == null || command.getPrefix().isEmpty()) {
                result.add(formatCommandShort(command));
            }
        }
        return result;
    }

    @Command(description="List all available commands starting with given string",
            header=COMMAND_LIST_HEADER)
    public List<String> list(
            @Param(name="startsWith", description="Pattern to show commands starting with") String startsWith) {

        List<ShellCommand> commands = owner.getCommandTable().getCommandTable();
        List<String> result = new ArrayList<String>(commands.size());
        for (ShellCommand command : commands) {
            if (command.startsWith(startsWith)) {
                result.add(formatCommandShort(command));
            }
        }
        return result;
    }

    @Command(description="Show info on using the UI")
    public Object help() {
        return
                "This is Cliche shell (http://cliche.sourceforge.net/).\n" +
                "To list all available commands enter ?list or ?list-all, " +
                "the latter will also show you system commands. To get detailed info " +
                " on a command enter ?help command-name.";
    }

    @Command(description="Show detailed info on all commands with given name")
    public Object help(
            @Param(name="command-name", description="Command name you want help on") String commandName) {
        List<ShellCommand> commands = owner.getCommandTable().commandsByName(commandName);
        StringBuilder result = new StringBuilder();
        for (ShellCommand command : commands) {
            result.append(formatCommandLong(command));
            result.append("\n");
        }
        return result;
    }

    private static final String COMMAND_LIST_HEADER = "abbrev\tname\tparams";

    private static String formatCommandShort(ShellCommand command) {
        boolean hasAbbr = command.getAbbreviation() != null;
        return String.format("%s%s\t%s%s\t%s",
                hasAbbr ? command.getPrefix() : "",
                hasAbbr ? command.getAbbreviation() : "",
                command.getPrefix(),
                command.getName(),
                formatCommandParamsShort(command));
    }

    private static String formatCommandParamsShort(ShellCommand command) {
        ShellCommandParamSpec[] paramSpecs = command.getParamSpecs();
        StringBuilder result = new StringBuilder("(");

        boolean first = true;
        for (ShellCommandParamSpec paramSpec : paramSpecs) {
            if (!first) {
                result.append(", ");
            }
            result.append(paramSpec.getName());
            first = false;
        }
        if (command.getMethod().isVarArgs()) {
            result.append("...");
        }

        result.append(")");
        
        return result.toString();
    }
    
    private static String formatCommandLong(ShellCommand command) {
        StringBuilder sb = new StringBuilder(String.format(
                "Command: %s\n" +
                "Abbrev:  %s\n" +
                "Params:  %s\n" +
                "Description: %s\n",
                command.getPrefix() + command.getName(),
                command.getAbbreviation() != null ? command.getPrefix() + command.getAbbreviation() : "(none)",
                formatCommandParamsShort(command),
                command.getDescription()));
        if (command.getArity() > 0) {
            sb.append(String.format("Number of parameters: %d\n", command.getArity()));
            Class[] paramTypes = command.getMethod().getParameterTypes();
            ShellCommandParamSpec[] paramSpecs = command.getParamSpecs();
            if (paramSpecs != null) {
                for (int i = 0; i < paramTypes.length; i++) {
                    if (paramSpecs[i] != null) {
                        sb.append(String.format("%s\t%s\t%s\n", paramSpecs[i].getName(), paramTypes[i].getSimpleName(),
                                paramSpecs[i].getDescription()));
                    }
                }
            }
            if (command.getMethod().isVarArgs()) {
                sb.append("This command is varargs on its last parameter.\n");
            }
        } else {
            sb.append("No parameters.\n");
        }
        return sb.toString();
    }

}
