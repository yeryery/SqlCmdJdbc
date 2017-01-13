package ua.com.juja.yeryery.controller;

import ua.com.juja.yeryery.controller.commands.*;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Controller {

    private View view;
    private Command[] commands;
    private String connectToDatabase = "Please, enter: " +
            "'connect|database|username|password' or use command 'help'";

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{new Connect(view, manager),
                new Clear(view, manager),
                new Create(view, manager),
                new Drop(view, manager),
                new Content(view, manager),
                new Insert(view, manager),
                new Display(view, manager),
                new Update(view, manager),
                new Delete(view, manager),
                new Exit(view),
                new Help(view),
                new Unknown(view)};
    }

    public void run() {
        view.write("Hello, user!");
        view.write(connectToDatabase);

        try {
            execute();
        } catch (ExitException e) {
            //Do nothing
        }
    }

    private void execute() {
        while (true) {
            String input = view.read();

            for (Command command : commands) {
                String typeCommand = "Type command or 'help'";

                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        view.write(typeCommand);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ConnectException) {
                        view.write(String.format(e.getMessage(), input));
                        view.write(connectToDatabase);
                        break;
                    }

                    if (e instanceof ExitException) {
                        return;
                    }

                    if (e instanceof CancelException) {
                        view.write("Command execution is canceled");
                        view.write(typeCommand);
                        break;
                    }

                    printError(e);
                    break;
                }
            }
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();

        view.write("Error! " + message);
        view.write("Try again");
    }
}
