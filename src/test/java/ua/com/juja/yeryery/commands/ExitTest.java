package ua.com.juja.yeryery.commands;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;

public class ExitTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void TestCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWrongString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessExitCommand_ThrowsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing
        }

        //then
        Mockito.verify(view).write("See you!");
    }
}
