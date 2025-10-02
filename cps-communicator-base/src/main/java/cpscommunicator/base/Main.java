package cpscommunicator.base;

import cpscommunicator.base.cli.Command;

import java.util.*;

public class Main {
    private final Map<String, Command> commands;

    public Main() {
        commands = loadCommands();
    }

    private Map<String, Command> loadCommands() {
        Map<String, Command> discoveredCommands = new HashMap<>();
        ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
        for (Command cmd : loader) {
            if (!discoveredCommands.containsKey(cmd.getName())) {
                discoveredCommands.put(cmd.getName(), cmd);
            }
        }
        return discoveredCommands;
    }

    public void run(String[] args) {
        if (args.length == 0 || args[0].equals("help") || args[0].equals("-help") || args[0].equals("--help") || args[0].equals("?")) {
            printHelp();
            return;
        }

        String commandName = args[0];
        Command command = commands.get(commandName);

        if (command == null) {
            System.err.println("Error: Unknown command '" + commandName + "'");
            printHelp();
            return;
        }

        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        if (commandArgs[0].equals("--help") || commandArgs[0].equals("-h") || commandArgs[0].equals("-help") || commandArgs[0].equals("--h")) {
            command.printHelp();
        } else {
            command.execute(commandArgs);
        }
    }

    private void printHelp() {
        System.out.println("\nAvailable commands:");
        if (commands.isEmpty()) {
            System.out.println("  (No commands discovered)");
        } else {
            for (Command cmd : commands.values()) {
                System.out.println(cmd.getName());
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run(args);
    }
}

