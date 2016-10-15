package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.view.View;
import ua.com.juja.yeryery.JDBCManager;

public class Connect {
    private View view;
    private JDBCManager manager;

    public Connect(View view, JDBCManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void process(String input) {

        String[] data = input.split("\\|");

        manager.connect(data[1], data[2], data[3]);
    }
}
