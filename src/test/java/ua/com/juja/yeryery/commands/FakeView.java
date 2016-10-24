package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.view.View;

public class FakeView implements View {

    private String messages = "";

    @Override
    public void write(String message) {
        messages += message + "\n";
    }

    @Override
    public String read() {
        return null;
    }

    public String getContent() {
        return messages;
    }
}
