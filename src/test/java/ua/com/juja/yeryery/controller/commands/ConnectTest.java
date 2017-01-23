package ua.com.juja.yeryery.controller.commands;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Utility.ConnectException;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.IllegalArgumentException;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Connect.class)
public class ConnectTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        command = new Connect(view, manager);
    }

    @Test
    public void testConnectCommand() throws Exception {
        //given
        String input = "connect|argument2|argument3|argument4";
        String[] splitInput = new String[]{"connect", "argument2", "argument3", "argument4"};
        String commandSample = "connect|database|username|password";

        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.splitConnectInput(input, commandSample)).thenReturn(splitInput);

        //when
        command.process(input);

        //then
        verify(view).write("Success!");
    }

    @Test
    public void testConnectCommandWithFiveArguments() throws Exception {
        //given
        String input = "connect|argument2|argument3|argument4|argument5";
        String commandSample = "connect|database|username|password";

        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        IllegalArgumentException exception = new IllegalArgumentException(
                "Wrong number of parameters. Expected 4 parameters, and you have entered 5");
        when(dialog.splitConnectInput(input, commandSample)).thenThrow(exception);

        //when
        try {
            command.process(input);
            fail("Expect ConnectException");
        } catch (ConnectException e) {
            assertEquals("Error! Wrong number of parameters. Expected 4 parameters, and you have entered 5\n" +
                    "Try again", e.getMessage());
        }
    }

    @Test
    public void testConnectCommandWithOneArgument() throws Exception {
        //given
        String input = "connect|";
        String commandSample = "connect|database|username|password";

        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        IllegalArgumentException exception = new IllegalArgumentException(
                "Wrong number of parameters. Expected 4 parameters, and you have entered 1");
        when(dialog.splitConnectInput(input, commandSample)).thenThrow(exception);

        //when
        try {
            command.process(input);
            fail("Expect ConnectException");
        } catch (ConnectException e) {
            assertEquals("Error! Wrong number of parameters. Expected 4 parameters, and you have entered 1\n" +
                    "Try again", e.getMessage());
        }
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
}
