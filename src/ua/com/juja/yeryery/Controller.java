package ua.com.juja.yeryery;

import ua.com.juja.yeryery.commands.Clear;
import ua.com.juja.yeryery.commands.Command;
import ua.com.juja.yeryery.commands.Connect;
import ua.com.juja.yeryery.commands.List;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Controller {

    private View view;
    private Command[] commands;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {new Connect(view, manager),
                                       new Clear(view, manager),
                                       new List(view, manager)};
    }

    public void run() {
        view.write("Hello, user!");
        view.write("Please, enter: " +
                "connect|database|username|password");

        String input = view.read();

        for (Command command :
                commands) {
            if (command.canProcess(input)) {
                command.process(input);
                break;
            }
        }
        view.write("type the commands (or help)");
    }
}
