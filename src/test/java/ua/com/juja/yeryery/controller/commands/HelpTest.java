package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class HelpTest {

    private View view = Mockito.mock(View.class);
    private Command command = new Help(view);

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Help(view);
    }

    @Test
    public void TestCanProcessHelpString() {
        //when
        boolean canProcess = command.canProcess("help");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessHelpWrongString() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessHelp() {
        //given

        //when
        command.process("help");

        //then
        shouldPrint("[Content of commands:, " +
                "\tconnect|database|username|password, " +
                "\t\tConnect to Database, " +
                "\ttables, " +
                "\t\tDisplay a list of available tables, " +
                "\tcreate, " +
                "\t\tCreate new table, " +
                "\tdisplay, " +
                "\t\tDisplay records of the table, " +
                "\tinsert, " +
                "\t\tInsert new record in the table, " +
                "\tdelete, " +
                "\t\tFind required record and remove it from the table, " +
                "\tupdate, " +
                "\t\tFind required record and update it in the table, " +
                "\tclear, " +
                "\t\tClear the table after confirmation, " +
                "\tdrop, " +
                "\t\tDelete the table after confirmation, " +
                "\texit, " +
                "\t\tProgram exit, " +
                "\thelp, " +
                "\t\tPrint all available commands]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
