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
                "\tcontent, " +
                "\t\tContent of tables, " +
                "\tcreate, " +
                "\t\tCreate new table, " +
                "\tdelete, " +
                "\t\tDelete rows in require table, " +
                "\tdisplay, " +
                "\t\tDisplay require table, " +
                "\tinsert, " +
                "\t\tInsert new row in require table, " +
                "\tupdate, " +
                "\t\tUpdate rows in require table, " +
                "\tclear, " +
                "\t\tClear require table, " +
                "\tdrop, " +
                "\t\tDrop require table, " +
                "\texit, " +
                "\t\tProgram exit, " +
                "\thelp, " +
                "\t\tAll commands]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
