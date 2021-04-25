package finalproject.client.cli.state;

import finalproject.client.cli.parsetree.CommandNode;
import finalproject.client.cli.parsetree.OptionArgNode;
import finalproject.client.cli.parsetree.OptionNode;

public interface State {
    void visit(CommandNode node);
    void visit(OptionArgNode node);
    void visit(OptionNode node);
}
