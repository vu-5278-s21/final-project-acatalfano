package finalproject.client.clientapi;

import finalproject.vcs.VcsFactory;

public class ClientApiFactory {
    public static ClientApi getCli() {
        return new CommandLineApi(VcsFactory.getVcs());
    }
}
