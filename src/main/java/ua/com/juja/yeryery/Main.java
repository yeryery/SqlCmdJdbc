package ua.com.juja.yeryery;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.manager.JdbcManager;
import ua.com.juja.yeryery.view.Console;
import ua.com.juja.yeryery.view.View;

public class Main {

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);

        View view = new Console();
        DatabaseManager manager = new JdbcManager();
        Controller controller = new Controller(view, manager);

        controller.run();
    }
}
// connect|yeryery|postgres|postgrespass
