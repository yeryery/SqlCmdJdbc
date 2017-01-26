package ua.com.juja.yeryery.controller;

import ua.com.juja.yeryery.controller.commands.*;
import ua.com.juja.yeryery.controller.commands.Utility.CancelException;
import ua.com.juja.yeryery.controller.commands.Utility.ConnectException;
import ua.com.juja.yeryery.controller.commands.Utility.ExitException;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Controller {

    private View view;
    private Command[] commands;
    
    private static final String connectToDatabase = "Please, enter: " +
            "'connect|database|username|password' or use command 'help'";
    private static final String typeCommand = "Type command or 'help'";

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(view, manager),
                new Help(view),
                new Exit(view),
                new IsConnected(manager),
                new Clear(view, manager),
                new Create(view, manager),
                new Drop(view, manager),
                new Tables(view, manager),
                new Insert(view, manager),
                new Display(view, manager),
                new Update(view, manager),
                new Delete(view, manager),
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

                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        view.write(typeCommand);
                        break;
                    }
                } catch (Exception e) {

                    if (e instanceof CancelException) {
                        view.write(e.getMessage());
                        view.write(typeCommand);
                        break;
                    }

                    if (e instanceof ExitException) {
                        return;
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

        if (e instanceof ConnectException) {
            view.write(connectToDatabase);
        } else {
            view.write(typeCommand);
        }
    }
}
