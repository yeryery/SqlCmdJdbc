package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;

public class List implements Command {

    private View view;
    private DatabaseManager manager;

    public List(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("list");
    }

    @Override
    public void process(String input) {
        String[] tableNames = manager.getTableNames();
        Arrays.sort(tableNames);
        view.write(Arrays.toString(tableNames));
    }
}
