package ua.com.juja.yeryery;

import ua.com.juja.yeryery.commands.*;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Controller {

    private View view;
    private Command[] commands;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {new Connect(view, manager),
                                       new Clear(view, manager),
                                       new List(view, manager),
                                       new Exit(view)};
    }

    public void run() {
        view.write("Hello, user!");
        view.write("Please, enter: " +
                "connect|database|username|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    printError(e);
                    break;
                }
            }
            view.write("type the commands (or help)");
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("Error! " + message);
        view.write("Try again");
    }
}
