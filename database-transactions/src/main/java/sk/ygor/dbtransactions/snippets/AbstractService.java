package sk.ygor.dbtransactions.snippets;

public abstract class AbstractService {

    DatabaseSession getDatabaseSession() {
        throw new RuntimeException("This was always somehow available and working fine (almost) all the time");
    }

}
