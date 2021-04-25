package finalproject.client.cli;

import finalproject.client.InputCommand;

import java.util.regex.Pattern;

public class CommandParser {
    private final Pattern commandPattern = Pattern.compile("$[\\w\\D]\\w*^");
    private final String nonTerminatingFlagPattern = "^(?:(?:-)(?<short>\\w)|(?:--)(?<long>\\w{2,}))";
    private final Pattern flagPattern = Pattern.compile(nonTerminatingFlagPattern + '$');
    private final Pattern optionPattern = Pattern.compile(nonTerminatingFlagPattern + "(?:[\\s&&[^\\n]]+|=)(?<arg>\\w+)$");

    public CommandParser() {
    }

    public InputCommand parse(String command) {
        String[] strs = tokens(command);
        for (String str : strs) {
            System.out.println(str);
        }

        //nonterm-flag = ^(?:(?:-)(?<short>\w)|(?:--)(?<long>\w{2,}))
        //flag = nonterm-flag + "$"
        //option = nonterm-flag + "(?:[\s&&[^\n]]+|=)(?<arg>\w+)$"
        //TODO: need to request a new line if '\' character at line-end!
        //TODO: DO IT LIKE THIS!!! IF YOU HAVE A TERMINAL '\', THEN
        //      ASK FOR A NEW LINE (AND KEEP DOING SO UNTIL NO TERMINAL '\')
        //      THEN STRIP THE '\' CHARACTERS AND TREAT IT AS ONE LINE!
        //TODO: use the mediator pattern here

        return null;
    }

    private String[] tokens(String command) {
        return command.split("[\\s&&[^\\n]]+"); //("\\s"); //("[\\s[^\\n]]");
    }
}
