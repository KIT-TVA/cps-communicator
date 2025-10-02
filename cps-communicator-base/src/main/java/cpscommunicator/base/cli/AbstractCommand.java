package cpscommunicator.base.cli;

import org.apache.commons.cli.*;

public abstract class AbstractCommand implements Command {
    @Override
    public void execute(String[] args) {
        Options options = getOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            run(cmd);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(getName(), options);
        }
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName(), getOptions());
    }
}
