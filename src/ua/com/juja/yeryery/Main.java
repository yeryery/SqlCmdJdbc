package ua.com.juja.yeryery;

import ua.com.juja.yeryery.view.Console;
import ua.com.juja.yeryery.view.View;

public class Main {

    public static void main(String[] args) {
        View view = new Console();
        JDBCManager manager = new JDBCManager();
        Controller controller = new Controller(view, manager);

        controller.run();
    }

}
// connect|yeryery|postgres|postgrespass
