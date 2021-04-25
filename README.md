# Final Project

## Amazon Routing Challenge

This is the project that was settled on after another project was underway. See details of the partial project
described in the `Initial Project` section

## Initial Project

The `initial project` directory is where preliminary work on this project was done before the decision was made to focus efforts on the Amazon Last Mile Routing Challenge.

The initial plan for that project was a git-like CLI tool that would be extensible to another user interface (such as a GUI like source-tree for instance).
At the point of switching project focuses, the initial project's focus was amended to be a generic CLI-parser developer's tool,
something be be used and configured by a Java developer who desired a CLI in their application.

The shift to a CLI focus had not yet been implemented, so there are still artifacts of the original version control system intent.

It would be configurable in 1 of 2 ways. The first option is to configure the CLI via a set of publicly exposed methods that add
each valid command option, valid options, required/optional flagging of each option, and specifying argument(s) as well as restrictions on the argument(s) themselves/itself.
The second config option would be via one or many config files that would be parsed by the CLI-builder.

### Patterns and Design Details

The bridge pattern would be used to decouple the parser configuration from the parser implementation.
This would be what supports the alternate configuration schemes, as well as permitting implementation details
of the command-parser to easily evolve or support alternative parsings in future development.
e.g. to support a simpler vocabulary like `cmd --flag --opt=arg --otherOpt arg -o arg -f`
as well as a more complex vocabulary that supports sub-commands and sub-sub-commands like `cmd subCmd subSubCmd --flag`, where all sub-commands would need there own independent configuration, possibly even a shared "common options" configuration.

The bridge pattern would also be used to decouple the command-parsing from the command-input.
This would allow supporting the project evolving to handle different command-input schemes such as a GUI.

The singleton pattern was used to ensure that there was only one instance of the `ClientApi` class.
Factory Method was also used to provide the `ClientApi` in the first place, although either this or the singleton
was (potentially) about to be dropped because the new direction made the `ClientApi` and `CommandLineApi` classes
into internal APIs, not what was to be exposed to the consumer of this tool.

Interpreter would be used to parse the commands that were input (after configuration was complete)
This took place in the `CommandParser` class. To handle the end-of-line continuation `/` there would need to be some
cross-talk between the `InputCommand` (which was going to call `CommandLineApi`, to enable multiple input-schemes)
and the `CommandParser`. This would be handled by the mediator pattern, to keep the `CommandParser`'s
structure-awareness (discussed next) decoupled from the `InputCommand`'s user-interface awareness
(i.e. CLI vs GUI, etc).

An input command would first be built into a parsetree The parsetree leverages the composite pattern
with the command at its root (i.e. the `cmd` in `cmd --flag --opt=arg`). Flags and options would compose the children
and arguments (if any) would be leaves of options.

From here, the parse-tree would be traversed using the visitor pattern, coupled with the state pattern and the
builder pattern to construct a syntax tree of the commands, or throwing an exception if an invalid state were
encountered (to be caught in another context, where pre-configured or default error output would be generated).

Again employing the visitor pattern, the command would be built into its final form, an object encapsulating the
command, as per the Command pattern. After the tool is configured by the user, the output to a `parseCommand` call
would be either this command object, or an "error" command. If configured for CLI-mode, then the error message is
still output to the command line, but it's also provided as an "error" command (output) in case the user wants to take
additional action when errors occur (especially necessary for more complex GUI modes).

Back on the configuration side of this tool, besides the bridge pattern that was previously discussed,
the "config file" mode would also need to employ its own interpreter to translate the file contents into
command objects (again employing the Command pattern) to encapsulate the configuration requests that would need
to be made. Likewise, in the in-code configuration mode, method calls would also create command objects.

The builder pattern would be responsible for turning each command object into a key-value pair for a dictionary
of valid commands names (the key), paired with a value representing the valid options/flags (including short and long
option/flag name, whether or not it's required or optional if it's an option, and any restrictions on option-argument
regex that's required to match for a valid input, etc.).

In the manual configuration mode, each command will be build and stored into the dictionary in situ, or an
exception will be thrown if the configuration is invalid or if the configuration clashes with a previously-set
configuration.

In the config file mode, commands will be accumulated into a private collection structure exposed through the
Iterator pattern. The class that owns the hidden collection will be responsible for verifying that the configuration
commands are all valid and don't clash with one another. An exception is thrown if invalid, or else the exposed
iterator is iterated by the builder that constructs all entries into the dictionary.

The dictionary is what is used by the `CommandParser` (along with the State pattern) to turn the parse tree into a
syntax tree (or throw an exception if it violates the definition described in the dictionary).

### Overview of the Assignment

The purpose of this assignment is to design and implement a larger (for the scope of the class) software application in Java that demonstrates knowledge of the concepts from class. Some examples of possible projects include: 

  1. An HTTP API deployed to the cloud or runnable as a container
  2. A command-line tool with a high-quality interface
  3. An Android mobile application
  4. A Java lambda service in something like AWS Lambda
  5. Some other thing that has equivalent functionality / complexity

Requirements for all projects:

  1. High-quality written documentation to use, understand, and enhance the project
  2. Good tests
  3. Well thought-out architecture
  

There are no restrictions on what type of application you write. However, you must appropriately scope your application to the time available for the assignment and deliver a finished product. Your application must be reasonably _complete_ at the end of the assignment. In addition, your application must have tests to demonstrate that it works. 

### Present Your Project

In one of the last two live sessions you will give a presentation on your project. Ask the instrutor how long your presentation should be. You should explain: 1) what the application is, 2) the overall architecture, and 3) what were the interesting challenges / solutions / tools involved. Your presentation should include a demo of your application. You should include a couple of visuals (e.g., slides), demonstrating the overall architecture and concepts. Please be selective in what code you show so that the audience can follow your presentation -- you don't need to walk through the application line-by-line.

### README File ###

You must replace this README file in your repo with your own (make a copy first!). Your README should describe each of the following: 1) what your application is, 2) how it is designed, and 3) instructions and/or sample data to build / run it. Make sure and explain how the concepts map to the seven weeks that you chose.

### Test Development ###

Your application must have tests that demonstrate that the application works as expected. When you scope the application, you should take into account the tests that you need to write. A carefully designed and tested smaller application is better than an untested or poorly thought out complex application. Build something simple and beautiful that is well-tested. 

***Your tests should run automatically on every commit using GitHub actions.***


### Concluding Remarks

This assignment is intentionally open-ended. It is essential that you take time up-front to think through what you can reasonably design, build, test, and document in the allotted time. You should scale the complexity of what you do to your programming skills. 

### REMINDERS:

* Start early and ask questions.
  
* Build something simple and beautiful: https://www.infoq.com/presentations/Simple-Made-Easy/
