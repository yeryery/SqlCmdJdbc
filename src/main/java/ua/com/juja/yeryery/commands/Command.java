package ua.com.juja.yeryery.commands;

public interface Command {
    boolean canProcess(String input);

    void process(String input);
}
