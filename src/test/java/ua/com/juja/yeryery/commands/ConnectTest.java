package ua.com.juja.yeryery.commands;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;

public class ConnectTest {

    private View view = Mockito.mock(View.class);
    private DatabaseManager manager = Mockito.mock(DatabaseManager.class);

    @Test
    public void TestCanProcessConnect() {
        //given
        Command command = new Connect(view, manager);

        //when
        boolean canProcess = command.canProcess("connect|");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWrongString() {
        //given
        Command command = new Connect(view, manager);

        //when
        boolean canProcess = command.canProcess("wrong|");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessConnectCommand() {
        //given
        Command command = new Connect(view, manager);

        //when
        command.process("connect|argument2|argument3|argument4");

        //then
        Mockito.verify(view).write("Success!");
    }

    @Test
    public void TestProcessConnectCommand_ThrowsExitException() {
        //given
        Command command = new Connect(view, manager);

        try {
            command.process("connect|argument2|argument3");
            fail("IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            view.write("Error! Wrong number of parameters. Expected 4, and you have entered 3\n" +
                        "Try again.");
        }

        //then
        Mockito.verify(view).write("Error! Wrong number of parameters. Expected 4, and you have entered 3\n" +
                                    "Try again.");
    }
}

