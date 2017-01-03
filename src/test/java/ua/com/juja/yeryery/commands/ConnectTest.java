package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yeryery.ConnectException;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(view, manager);
    }

    @Test
    public void TestCanProcessConnect() {
        //when
        boolean canProcess = command.canProcess("connect|");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWrongString() {
        //when
        boolean canProcess = command.canProcess("wrong|");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessConnectCommand() {
        //when
        command.process("connect|argument2|argument3|argument4");

        //then
        verify(view).write("Success!");
    }

    @Test
    public void TestProcessConnectCommandWithOneArgument() {
        //when
        try {
            command.process("connect|");
            fail("Expect ConnectException");
        } catch (ConnectException e) {
            assertEquals("Error! Wrong number of parameters. Expected 4, and you have entered 1\n" +
                    "Try again", e.getMessage());
        }
    }

    @Test
    public void TestProcessConnectCommandWith5Args() {
        //when
        try {
            command.process("connect|argument2|argument3|argument4|argument5");
            fail("Expect ConnectException");
        } catch (ConnectException e) {
            assertEquals("Error! Wrong number of parameters. Expected 4, and you have entered 5\n" +
                    "Try again", e.getMessage());
        }
    }
}

