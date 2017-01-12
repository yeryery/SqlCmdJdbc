package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class Content implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "content";

    public Content(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> tableNames = manager.getTableNames();
        view.write(tableNames.toString());
    }
}
