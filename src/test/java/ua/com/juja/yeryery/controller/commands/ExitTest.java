package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.yeryery.controller.commands.Exceptions.ExitException;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ExitTest {

    private View view;
    private Command command;
    private static final String ACTION = "exit";

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        //when
        boolean canProcess = command.canProcess(ACTION);

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
            command.process(ACTION);
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing
        }

        //then
        verify(view).write("See you!");
    }
}

