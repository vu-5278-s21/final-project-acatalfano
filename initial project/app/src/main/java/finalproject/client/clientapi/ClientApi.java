package finalproject.client.clientapi;

import finalproject.vcs.command.error.ErrorCommand;
import finalproject.vcs.command.request.RequestCommand;
import finalproject.vcs.command.output.OutputCommand;
import finalproject.vcs.VersionControlSystem;

public abstract class ClientApi {
    private static ClientApi singleton;
    private final VersionControlSystem vcs;

    private ClientApi() {
        // @ToDo
        vcs = null;
    }

    protected ClientApi(VersionControlSystem vcs) {
        this.vcs = vcs;
    }

    public static ClientApi instance() throws IllegalStateException {
        if (singleton == null) {
            throw new IllegalStateException(
                    "must call ClientApi.initialize before retrieving the instance"
            );
        }
        return singleton;
    }

    static void initialize(ClientApi clientApi) {
        if (singleton == null) {
            singleton = clientApi;
        }
    }

    public abstract RequestCommand getCommand();

    public abstract void handleError(ErrorCommand error);

    public abstract void handleOutput(OutputCommand output);
}
