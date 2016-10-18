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
                                       new Insert(view, manager),
                                       new Exit(view),
                                       new Help(view)};
    }

    public void run() {
        view.write("Hello, user!");
        String connectToDatabase = "Please, enter: " +
                "'connect|database|username|password' or use command 'help'";
        view.write(connectToDatabase);

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof NullPointerException) {
                        view.write(String.format("You can`t use '%s' unless you are not connected.", input));
                        view.write(connectToDatabase);
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
