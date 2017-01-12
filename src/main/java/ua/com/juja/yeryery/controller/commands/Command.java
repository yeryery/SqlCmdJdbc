package ua.com.juja.yeryery.controller.commands;

public interface Command {
    boolean canProcess(String input);

    void process(String input);
}
