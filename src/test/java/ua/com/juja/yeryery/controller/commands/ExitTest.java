package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.yeryery.controller.commands.Utility.ExitException;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ExitTest {

    private View view = Mockito.mock(View.class);
    private Command command;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWrongString() {

        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_ThrowsExitException() {
        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing
        }

        //then
        verify(view).write("See you!");
    }
}

