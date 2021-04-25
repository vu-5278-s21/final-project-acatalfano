package finalproject.client.clientapi;

import finalproject.client.cli.CommandParser;
import finalproject.vcs.command.error.ErrorCommand;
import finalproject.vcs.command.request.RequestCommand;
import finalproject.vcs.command.output.OutputCommand;
import finalproject.vcs.VersionControlSystem;

import java.io.Console;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.regex.Pattern;

class CommandLineApi extends ClientApi {
    private final PrintWriter cout;
    private final PrintStream cerr;
    private final Console console;

    private final CommandParser parser;

    private CommandLineApi() {
        super(null);
        cout = null;
        cerr = null;
        console = null;
        parser = null;
    }

    CommandLineApi(VersionControlSystem vcs) {
        super(vcs);
        console = System.console();
        if (console == null) {
            //TODO: throw error and handle this with "handleError" method
            System.err.println("Could not find a Command-Line Interface in the current environment");
            System.exit(1);
        }
        cout = console.writer();
        cerr = System.err;
        parser = new CommandParser();
        ClientApi.initialize(this);
    }

    @Override
    public RequestCommand getCommand() {
        StringBuilder line = new StringBuilder("\\");
        final Pattern continuationPattern = Pattern.compile(".*\\$");
        while (continuationPattern.matcher(line).matches()) {
            String nextLine = console.readLine();
            //TODO: use with mediator
        }

//        String line = console.readLine();
//        parser.parse(line);
//        String line = console.readLine();
//        console.writer().println(line);
        // @ToDo: eventually use interpreter pattern with command pattern for this

        // TODO: vvv
        return null;
    }

    @Override
    public void handleError(ErrorCommand error) {
        //TODO: use cerr
    }

    @Override
    public void handleOutput(OutputCommand output) {
        //TODO: use cout
    }
}
