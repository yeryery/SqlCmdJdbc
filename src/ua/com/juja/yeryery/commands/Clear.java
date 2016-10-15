package ua.com.juja.yeryery.commands;

public class Clear implements Command {
    @Override
    public boolean canProcess(String input) {
        return false;
    }

    @Override
    public void process(String input) {

    }
}
