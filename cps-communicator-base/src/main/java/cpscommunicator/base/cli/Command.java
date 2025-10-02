package cpscommunicator.base.cli;

import org.apache.commons.cli.*;

public interface Command {
    void execute(String[] args);

    String getName();

    Options getOptions();

    void run(CommandLine cmd);

    void printHelp();
}

