# Final Project

## Amazon Routing Challenge

This is the project that was settled on after another project was underway. See details of the partial project
described in the [Initial Project section](#initial-project). Also see the source and build-configs under the
`initial project` directory.

### Conext of Amazon Routing Challenge

Current prediction models of delivery routes are (knowingly) naive and inaccurate.
The current models are a simple product of the theoretical length, duration, or cost.

The model's job is to output a sequence of stops, given an input of stops that must be made in this route.

The current model's predictions are off because a lot more than mere theoretical cost is relevant
in this problem than would be in a pure graph-based shortest-path algorithm you might encounter in a math class.

The real routes differ (when the driver knows the area well enough) because of factors such as rush hour,
ease of parking, even factors like the time of the year (e.g. during the holiday season).

The goal is to design a model that can make more accurate predictions to align with what a good driver's route
actually looks like. This way Amazon can leverage such a model to plan out better routes for its drivers.

### Contributions

My contribution to the project is on the theoretical side.

Currently, we are fleshing out the details of a 2-layer hierarchical clustering approach.
The first layer will be unsupervised and will effectively learn the "pre-conditions" or "context" of a route.
The second layer will be supervised and will sub-divide each classification into 3 different quality-rankings,
which is the provided response variable (a qualitative "high", "medium", or "low" quality rating).

The model will take new input to be first assigned to the top-layer classification, then will construct a
route sequence that best matches something in the "good" sub-classification.

For the first stage, we will be classifying per an iterative k-means clustering algorithm, where we change the
"K" value in increments of 1, checking against the test data, until the the model stops improving its fit.

K-means clustering takes a fixed "K" integer and begins with K centroids in random (but distant) locations in the
feature-space. All points are assigned to the centroid that it is closest to. Then the centroids are reassigned to
the center of all the points assigned to them and all of the points are re-evaluated to see if they belong to
another centroid now. This is repeated until the points settle in their category and the centroids don't
reposition anymore.

An alternative consideration is principal component analysis, which would trim the size of the feature vector.
We might also consider adding an additional step for running an autoencoder to learn the relevant features of the
entire feature set, and then clustering with only those features under consideration.

An autoencoder is a special case of a neural network (which may include a convolution layer, in which case it's a
deep-learning neural network). The job of an autoencoder is to learn its input, essentially. In doing so, it produces
the feature set that is necessary for it to recognize the provided input (and strips out the irrelevant noisy
features).

The second clustering phase is going to (most likely) implement linear discriminant analysis. Some alternative
considerations still on the table are support vector machines and neural networks (potentially a deep-learning
neural network).

Linear Discriminant Analysis learns P(Y=k|X=x) by learning P(X=x|Y=k) and applying Bayes' theorem.
Here "Y=k" is the event that the point falls into class "k" (where "k" is one of "high", "medium", or "low"
route-quality). "X=x" is the event that the data point under consideration is "x" (where "x" is an instantiation
of the feature set). What the model concretely learns is a series of decision boundaries. It would be like drawing
lines across a coordinate plane, some of which may be infinite at both ends, infinite at one end, or entirely finite,
and the finite endpoints would be fixed to another line. These lines would divide the space into its classification.
The one difference is that the space is a hyperdimensional plane and the lines are drawn across its surface.

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
